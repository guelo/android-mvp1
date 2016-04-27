package miguel.mvp.ui.search;

import android.support.annotation.Nullable;

import java.io.IOException;
import java.lang.ref.WeakReference;

import miguel.mvp.model.SearchResult;
import miguel.mvp.network.GitHubService;
import miguel.mvp.network.NetworkClient;
import miguel.mvp.network.ServerError;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchPresenter implements SearchContract.SearchPresenter {
	private final GitHubService github;
	private WeakReference<SearchContract.SearchView> viewRef;
	private SearchResult results;
	private Throwable errorMessage;

	public SearchPresenter(GitHubService gitHubService) {
		this.github = gitHubService;
	}

	@Nullable
	public SearchContract.SearchView getView() {
		return viewRef == null ? null : viewRef.get();
	}

	public boolean isViewAttached() {
		return viewRef != null && viewRef.get() != null;
	}

	@Override
	public void viewAttached(SearchContract.SearchView view) {
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
						showError(new ServerError("HTTP Status: " + response.message() +"\n" + response.errorBody().string()));
					} catch (IOException e) {
						showError(e);
					}
				}
			}

			@Override
			public void onFailure(Call<SearchResult> call, Throwable t) {
				showError(t);
			}
		});
	}

	private void showError(Throwable error) {
		errorMessage = error;
		if (isViewAttached()) {
			getView().displayError(error);
		}
	}

	private void showData(SearchResult response) {
		results = response;
		if (isViewAttached()) {
			getView().displayContent(response);
		}

	}

	private void showLoading() {
		if (isViewAttached()) {
			getView().displayLoading();
		}

	}


}
