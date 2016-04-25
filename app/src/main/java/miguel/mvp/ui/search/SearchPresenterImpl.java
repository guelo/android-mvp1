package miguel.mvp.ui.search;

import android.support.annotation.Nullable;

import java.io.IOException;
import java.lang.ref.WeakReference;

import miguel.mvp.model.SearchResult;
import miguel.mvp.network.GitHubService;
import miguel.mvp.network.NetworkClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchPresenterImpl implements SearchPresenter {
	private final GitHubService github;
	private WeakReference<SearchView> viewRef;
	private SearchResult results;
	private String errorMessage;

	public SearchPresenterImpl() {
		this.github = github();
	}

	@Nullable
	public SearchView getView() {
		return viewRef == null ? null : viewRef.get();
	}

	public boolean isViewAttached() {
		return viewRef != null && viewRef.get() != null;
	}

	@Override
	public void viewAttached(SearchView view) {
		viewRef = new WeakReference<>(view);

		if (errorMessage != null) {
			showError(errorMessage);
		}
		if (results != null) {
			view.displayContent(results);
		}
	}

	@Override
	public void doSearch(String term) {
		showLoading();

		github.searchCode("user:guelo " + term).enqueue(new Callback<SearchResult>() {
			@Override
			public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
				if (response.isSuccessful()) {
					showData(response.body());
				} else {
					try {
						showError("HTTP Status: " + response.message() +"\n" + response.errorBody().string());
					} catch (IOException e) {
						showError("network Error");
					}
				}
			}

			@Override
			public void onFailure(Call<SearchResult> call, Throwable t) {
				showError("network Error");
			}
		});
	}

	private void showError(String error) {
		errorMessage = error;
		if (isViewAttached()) {
			getView().displayError(error);
		}
	//	viewState = VIEW_STATE.ERROR;
	}

	private void showData(SearchResult response) {
		results = response;
		if (isViewAttached()) {
			getView().displayContent(response);
		}

	//	viewState = VIEW_STATE.SHOWING_DATA;
	}

	private void showLoading() {
		if (isViewAttached()) {
			getView().displayLoading();
		}

	//	viewState = VIEW_STATE.LOADING;
	}

	//TODO remove
	private GitHubService github() {

		return NetworkClient.getInstance().getRetrofit().create(GitHubService.class);

	}


}
