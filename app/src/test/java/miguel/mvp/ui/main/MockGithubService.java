package miguel.mvp.ui.main;

import java.util.List;

import miguel.mvp.model.Repo;
import miguel.mvp.model.SearchResult;
import miguel.mvp.network.GitHubService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

class MockGithubService implements GitHubService {

	public interface EnqueRunnable {
		void run(Callback callback, Call call);
	}

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
