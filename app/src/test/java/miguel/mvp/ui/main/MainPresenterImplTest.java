package miguel.mvp.ui.main;

import junit.framework.Assert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import miguel.mvp.model.Repo;
import miguel.mvp.model.SearchResult;
import miguel.mvp.network.GitHubService;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public class MainPresenterImplTest {

	@org.junit.Test
	public void testReload() throws Exception {
		final List<Repo> repo = new ArrayList<>();
		MockService service = new MockService();
		service.enqueRunnable = new EnqueRunnable() {
			@Override
			public void run(Callback callback, Call call) {
				callback.onResponse(call, Response.success(repo));
			}
		};

		MainPresenterImpl presenter = new MainPresenterImpl(service);

		MockView view = new MockView();
		presenter.viewAttached(view);
		presenter.reload();
		Assert.assertEquals(view.data, repo);
	}

	@org.junit.Test
	public void testReloadNetworkFailure() throws Exception {
		final List<Repo> repo = new ArrayList<>();
		MockService service = new MockService();

		service.enqueRunnable = new EnqueRunnable() {
			@Override
			public void run(Callback callback, Call call) {
				callback.onFailure(call, new IOException());
			}
		};
		MainPresenterImpl presenter = new MainPresenterImpl(service);
		MockView view = new MockView();
		presenter.viewAttached(view);
		presenter.reload();
		Assert.assertEquals("network Error", view.error);
	}

	@org.junit.Test
	public void testReloadServerFailure() throws Exception {
		final List<Repo> repo = new ArrayList<>();
		MockService service = new MockService();
		service.enqueRunnable = new EnqueRunnable() { @Override public void run(Callback callback, Call call) {
				callback.onResponse(call, Response.<List<Repo>>error(500, ResponseBody.create(null, "server errr")));
			} };

		MainPresenterImpl presenter = new MainPresenterImpl(service);
		MockView view = new MockView();
		presenter.viewAttached(view);

		presenter.reload();
		Assert.assertEquals("HTTP Status: null\nserver errr", view.error);
	}

	@org.junit.Test
	public void testViewAttached() throws Exception {
		// simulate network data already loaded
		MockService service = new MockService();
		service.enqueRunnable = new EnqueRunnable() {
			@Override
			public void run(Callback callback, Call call) {
				callback.onResponse(call, Response.success(new ArrayList<>()));
			}
		};
		MainPresenterImpl presenter = new MainPresenterImpl(service);
		presenter.reload();

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
		presenter.reload();
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
		presenter.reload();
		mockView = new MockView();
		presenter.viewAttached(mockView);
		Assert.assertNotNull(mockView.error);
		Assert.assertFalse(mockView.loading);
		Assert.assertNull(mockView.data);

	}

	class MockView implements MainView {
		List<Repo> data;
		String error;
		boolean loading;

		@Override
		public void displayLoading() {
			loading = true;
		}

		@Override
		public void displayError(String message) {
			this.error = message;
		}

		@Override
		public void displayContent(List<Repo> data) {
			this.data = data;
		}
	}
	public interface EnqueRunnable {

		void run(Callback callback, Call call);
	}

	class MockService implements GitHubService {

		EnqueRunnable enqueRunnable;

		@Override
		public Call<List<Repo>> listRepos(@Path("user") String user) {
			return new MockCall<List<Repo>>() {
				@Override
				public void enqueue(Callback<List<Repo>> callback) {
					enqueRunnable.run(callback, this);
				}
			};
		}

		@Override
		public Call<SearchResult> searchCode(@Query("q") String query) {
			return new MockCall<SearchResult>() {
				@Override
				public void enqueue(Callback<SearchResult> callback) {
					enqueRunnable.run(callback, this);
				}
			};
		}

		@Override
		public Call<Repo> getRepo( @Url String url) {
			return new MockCall<Repo>() {
				@Override
				public void enqueue(Callback<Repo> callback) {
					enqueRunnable.run(callback, this);
				}
			};
		}
	}
	static class MockCall<T> implements Call<T> {
		@Override
		public Response<T> execute() throws IOException { return null; }

		@Override
		public void enqueue(Callback<T> callback) {
		}

		@Override public boolean isExecuted() { return false; }

		@Override
		public void cancel() { }

		@Override
		public boolean isCanceled() {
			return false;
		}

		@Override
		public Call<T> clone() {
			return null;
		}

		@Override
		public Request request() {
			return null;
		}
	};

}