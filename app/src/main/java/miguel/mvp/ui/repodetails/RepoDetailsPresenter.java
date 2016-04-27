package miguel.mvp.ui.repodetails;

import android.support.annotation.Nullable;

import java.io.IOException;
import java.lang.ref.WeakReference;

import javax.inject.Inject;

import miguel.mvp.model.Repo;
import miguel.mvp.network.GitHubService;
import miguel.mvp.network.NetworkClient;
import miguel.mvp.network.ServerError;
import miguel.mvp.ui.MVPBase.BasePresenter;
import miguel.mvp.ui.repodetails.RepoDetailContract.Presenter;
import miguel.mvp.ui.repodetails.RepoDetailContract.View;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RepoDetailsPresenter extends BasePresenter<View> implements Presenter {

	@Inject
	GitHubService github;

	private final String repoUrl;

	public RepoDetailsPresenter(String repoUrl) {
		this.repoUrl = repoUrl;
	}


	@Override
	public void viewAttached(View view) {
		super.viewAttached(view);

		load();
	}

	private void load() {
		showLoading();

		Call<Repo> call = github.getRepo(repoUrl);
		call.enqueue(new Callback<Repo>() {
			@Override
			public void onResponse(Call<Repo> call, Response<Repo> response) {
				if (response.isSuccessful()) {
					if (getView() != null) {
						getView().displayContent(response.body());
					}
				} else {
					try {
						showError(new ServerError("HTTP Status: " + response.message() +"\n" + response.errorBody().string()));
					} catch (IOException e) {
						showError(e);
					}

				}
			}

			@Override
			public void onFailure(Call<Repo> call, Throwable t) {
				if (getView() != null) {
					getView().displayError(t);
				}
			}
		});
	}

	private void showLoading() {
		if (getView() != null) {
			getView().displayLoading();
		}
	}

	private void showError(Throwable error) {
		if (getView() != null) {
			getView().displayError(error);
		}
	}

}
