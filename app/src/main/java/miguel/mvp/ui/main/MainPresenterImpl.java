package miguel.mvp.ui.main;

import android.support.annotation.Nullable;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

import miguel.mvp.model.Repo;
import miguel.mvp.network.GitHubService;
import miguel.mvp.network.NetworkClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPresenterImpl implements MainPresenter {

	private enum VIEW_STATE {
		LOADING, ERROR, SHOWING_DATA
	}

	private final GitHubService github;
	private WeakReference<MainView> viewRef;

	private VIEW_STATE viewState;
	private List<Repo> repos;
	private String errorMessage;

	public MainPresenterImpl() {
		this.github = github();
	}

	MainPresenterImpl(GitHubService service) {
		this.github = service;
	}

	@Override
	public void reload() {
		showLoading();

		Call<List<Repo>> reposCall = github.listRepos("guelo");
		reposCall.enqueue(new Callback<List<Repo>>() {
			@Override
			public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {
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
			public void onFailure(Call<List<Repo>> call, Throwable t) {
				showError("network Error");
			}
		});
	}

	@Nullable
	public MainView getView() {
		return viewRef == null ? null : viewRef.get();
	}

	public boolean isViewAttached() {
		return viewRef != null && viewRef.get() != null;
	}

	private void showError(String error) {
		errorMessage = error;
		if (isViewAttached()) {
			getView().displayError(error);
		}
		viewState = VIEW_STATE.ERROR;
	}

	private void showData(List<Repo> response) {
		repos = response;
		if (isViewAttached()) {
			getView().displayContent(repos);
		}

		viewState = VIEW_STATE.SHOWING_DATA;
	}

	private void showLoading() {
		if (isViewAttached()) {
			getView().displayLoading();
		}

		viewState = VIEW_STATE.LOADING;
	}

	@Override
	public void viewAttached(MainView mainView) {
		viewRef = new WeakReference<>(mainView);

		if (viewState==null) {
			reload();
		} else {

			switch (viewState) {
				case LOADING:
					showLoading();
					break;
				case SHOWING_DATA:
					showData(repos);
					break;
				case ERROR:
					showError(errorMessage);
					break;
			}
		}
	}


	//TODO remove
	private GitHubService github() {

		return NetworkClient.getInstance().getRetrofit().create(GitHubService.class);

	}


}