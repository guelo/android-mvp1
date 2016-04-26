package miguel.mvp.ui.main;

import junit.framework.Assert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import miguel.mvp.model.Repo;
import miguel.mvp.model.SearchResult;
import miguel.mvp.network.GitHubService;
import miguel.mvp.network.ServerError;
import miguel.mvp.ui.main.MainContract.View;
import miguel.mvp.ui.main.MockGithubService.EnqueRunnable;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public class MainPresenterTest {

	@org.junit.Test
	public void testReload() throws Exception {
		final List<Repo> repo = new ArrayList<>();
		MockGithubService service = new MockGithubService();
		service.enqueRunnable = new EnqueRunnable() {
			@Override
			public void run(Callback callback, Call call) {
				callback.onResponse(call, Response.success(repo));
			}
		};

		MainPresenter presenter = new MainPresenter(service);

		MockView view = new MockView();
		presenter.viewAttached(view);
		presenter.load();
		Assert.assertEquals(view.data, repo);
	}

	@org.junit.Test
	public void testReloadNetworkFailure() throws Exception {
		final List<Repo> repo = new ArrayList<>();
		MockGithubService service = new MockGithubService();

		service.enqueRunnable = new EnqueRunnable() {
			@Override
			public void run(Callback callback, Call call) {
				callback.onFailure(call, new IOException());
			}
		};
		MainPresenter presenter = new MainPresenter(service);
		MockView view = new MockView();
		presenter.viewAttached(view);
		presenter.load();
		Assert.assertTrue(view.error instanceof IOException);
	}

	@org.junit.Test
	public void testReloadServerFailure() throws Exception {
		final List<Repo> repo = new ArrayList<>();
		MockGithubService service = new MockGithubService();
		service.enqueRunnable = new EnqueRunnable() { @Override public void run(Callback callback, Call call) {
				callback.onResponse(call, Response.<List<Repo>>error(500, ResponseBody.create(null, "server errr")));
			} };

		MainPresenter presenter = new MainPresenter(service);
		MockView view = new MockView();
		presenter.viewAttached(view);

		presenter.load();
		Assert.assertTrue(view.error instanceof ServerError);
	}

	@org.junit.Test
	public void testViewAttached() throws Exception {
		// simulate network data already loaded
		MockGithubService service = new MockGithubService();
		service.enqueRunnable = new EnqueRunnable() {
			@Override
			public void run(Callback callback, Call call) {
				callback.onResponse(call, Response.success(new ArrayList<>()));
			}
		};
		MainPresenter presenter = new MainPresenter(service);
		presenter.load();

		MockView mockView = new MockView();
		presenter.viewAttached(mockView);
		Assert.assertNull(mockView.error);
		Assert.assertFalse(mockView.loading);
		Assert.assertNotNull(mockView.data);


		// simulate still waiting on the network
		service.enqueRunnable = new EnqueRunnable() {
			@Override
			public void run(Callback callback, Call call) {
				//do nothing simulating waiting on the network
			}
		};
		presenter.load();
		mockView = new MockView();
		presenter.viewAttached(mockView);
		Assert.assertNull(mockView.error);
		Assert.assertTrue(mockView.loading);
		Assert.assertNull(mockView.data);

		// simulate error from network
		service.enqueRunnable = new EnqueRunnable() {
			@Override
			public void run(Callback callback, Call call) {
				callback.onFailure(call, new IOException());
			}
		};
		presenter.load();
		mockView = new MockView();
		presenter.viewAttached(mockView);
		Assert.assertNotNull(mockView.error);
		Assert.assertFalse(mockView.loading);
		Assert.assertNull(mockView.data);

	}

	class MockView implements View {
		List<Repo> data;
		Throwable error;
		boolean loading;

		@Override
		public void displayLoading() {
			loading = true;
		}

		@Override
		public void displayError(Throwable error) {
			this.error = error;
		}

		@Override
		public void displayContent(List<Repo> data) {
			this.data = data;
		}
	}


}