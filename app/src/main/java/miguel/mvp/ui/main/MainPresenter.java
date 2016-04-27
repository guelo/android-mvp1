package miguel.mvp.ui.main;

import java.io.IOException;
import java.util.List;

import miguel.mvp.model.Repo;
import miguel.mvp.network.GitHubService;
import miguel.mvp.network.ServerError;
import miguel.mvp.ui.MVPBase.BasePresenter;
import miguel.mvp.ui.main.MainContract.View;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPresenter extends BasePresenter<MainContract.View> implements MainContract.Presenter {

	private enum VIEW_STATE {
		LOADING, ERROR, SHOWING_DATA
	}

	private final GitHubService github;

	private VIEW_STATE viewState;
	private List<Repo> repos;
	private Throwable error;

	public MainPresenter(GitHubService service) {
		this.github = service;
	}

	@Override
	public void load() {
		showLoading();

		Call<List<Repo>> reposCall = github.listRepos("guelo");
		reposCall.enqueue(new Callback<List<Repo>>() {
			@Override
			public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {
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
			public void onFailure(Call<List<Repo>> call, Throwable t) {
				showError(t);
			}
		});
	}

	private void showError(Throwable error) {
		this.error = error;
		if (getView() != null) {
			getView().displayError(error);
		}
		viewState = VIEW_STATE.ERROR;
	}

	private void showData(List<Repo> response) {
		repos = response;
		if (getView() != null) {
			getView().displayContent(repos);
		}

		viewState = VIEW_STATE.SHOWING_DATA;
	}

	private void showLoading() {
		if (getView() != null) {
			getView().displayLoading();
		}

		viewState = VIEW_STATE.LOADING;
	}

	@Override
	public void viewAttached(View mainView) {
		super.viewAttached(mainView);

		if (viewState==null) {
			load();
		} else {

			switch (viewState) {
				case LOADING:
					showLoading();
					break;
				case SHOWING_DATA:
					showData(repos);
					break;
				case ERROR:
					showError(error);
					break;
			}
		}
	}

}