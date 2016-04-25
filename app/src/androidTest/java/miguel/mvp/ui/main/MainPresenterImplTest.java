package miguel.mvp.ui.main;

import java.util.List;

import miguel.mvp.model.Repo;
import miguel.mvp.model.SearchResult;
import miguel.mvp.network.GitHubService;
import retrofit2.Call;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class MainPresenterImplTest {

	static final class MockGithub implements GitHubService {

		@Override
		public Call<List<Repo>> listRepos(@Path("user") String user) {
			return null;
		}

		@Override
		public Call<SearchResult> searchCode(@Query("q") String query) {
			return null;
		}
	}
	@org.junit.Test
	public void testReload() throws Exception {

		MockGithub githubService = new MockGithub();
		MainPresenterImpl presenter = new MainPresenterImpl(new GitHubService() {
			@Override
			public Call<List<Repo>> listRepos(@Path("user") String user) {
				return null;
			}

			@Override
			public Call<SearchResult> searchCode(@Query("q") String query) {
				return null;
			}
		});

		presenster.viewAttached();
	}

	@org.junit.Test
	public void testGetView() throws Exception {

	}

	@org.junit.Test
	public void testIsViewAttached() throws Exception {

	}

	@org.junit.Test
	public void testViewAttached() throws Exception {

	}

	void mock() {/*
		NetworkBehavior behavior = NetworkBehavior.create();
		MockRetrofit mockRetrofit = new MockRetrofit.Builder(retrofit)
				.networkBehavior(behavior)
				.build();

		BehaviorDelegate<GitHubService> delegate = mockRetrofit.create(GitHubService.class);
		MockGithub gitHub = new MockGitHub(delegate);


	*/}
}