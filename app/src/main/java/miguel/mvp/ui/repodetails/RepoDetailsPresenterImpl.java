package miguel.mvp.ui.repodetails;

import android.support.annotation.Nullable;

import java.io.IOException;
import java.lang.ref.WeakReference;

import miguel.mvp.model.Repo;
import miguel.mvp.network.GitHubService;
import miguel.mvp.network.NetworkClient;
import miguel.mvp.ui.search.SearchView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RepoDetailsPresenterImpl implements RepoDetailsPresenter {

	private final String repoUrl;
	private WeakReference<RepoDetailsView> viewRef;
	@Nullable
	public RepoDetailsView getView() {
		return viewRef == null ? null : viewRef.get();
	}

	public boolean isViewAttached() {
		return viewRef != null && viewRef.get() != null;
	}


	public RepoDetailsPresenterImpl(String repoUrl) {
		this.repoUrl = repoUrl;
	}

	@Override
	public void viewAttached(RepoDetailsView view) {
		viewRef = new WeakReference<>(view);


		load();
	}

	private void load() {
		showLoading();

		Call<Repo> call = github().getRepo(repoUrl);
		call.enqueue(new Callback<Repo>() {
			@Override
			public void onResponse(Call<Repo> call, Response<Repo> response) {
				if (response.isSuccessful()) {
					if (isViewAttached()) {
						getView().displayContent(response.body());
					}
				} else {
					try {
						showError("HTTP Status: " + response.message() +"\n" + response.errorBody().string());
					} catch (IOException e) {
						showError("network Error");
					}

				}
			}

			@Override
			public void onFailure(Call<Repo> call, Throwable t) {
				getView().displayError(t.toString());
			}
		});
	}

	private void showLoading() {
		if (isViewAttached()) {
			getView().displayLoading();
		}
	}

	private void showError(String error) {
		if (isViewAttached()) {
			getView().displayError(error);
		}
	}

	//TODO remove
	private GitHubService github() {

		return NetworkClient.getInstance().getRetrofit().create(GitHubService.class);

	}

}
