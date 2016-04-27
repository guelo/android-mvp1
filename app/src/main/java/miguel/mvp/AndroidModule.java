package miguel.mvp;

import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import miguel.mvp.network.GitHubService;
import miguel.mvp.ui.main.MainContract.Presenter;
import miguel.mvp.ui.main.MainPresenter;
import miguel.mvp.ui.repodetails.RepoDetailsPresenter;
import miguel.mvp.ui.search.SearchPresenter;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AndroidModule {
	@Provides
	public GitHubService providesGithub(Retrofit retrofit) {
		return retrofit.create(GitHubService.class);
	}

	@Singleton
	@Provides
	public Retrofit providesRetrofit() {
		return new Builder()
				.baseUrl("https://api.github.com/")
				.addConverterFactory(GsonConverterFactory.create(new Gson()))
				.build();
	}

	@Provides
	public MainPresenter provideMainPresenter(GitHubService github) {
		return new MainPresenter(github);
	}

	@Provides
	public SearchPresenter provideSearchPresenter(GitHubService gitHubService) {
		return new SearchPresenter(gitHubService);
	}
}