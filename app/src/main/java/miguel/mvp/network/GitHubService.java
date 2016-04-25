package miguel.mvp.network;

import java.util.List;

import miguel.mvp.model.Repo;
import miguel.mvp.model.SearchResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface GitHubService {
	@GET("users/{user}/repos")
	Call<List<Repo>> listRepos(@Path("user") String user);

	@GET("search/code")
	Call<SearchResult> searchCode(@Query("q") String query);

	@GET
	Call<Repo> getRepo(@Url String url);

}
