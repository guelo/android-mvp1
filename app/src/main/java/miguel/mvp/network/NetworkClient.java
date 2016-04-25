package miguel.mvp.network;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkClient {
	private static NetworkClient ourInstance = new NetworkClient();
	public static NetworkClient getInstance() {
		return ourInstance;
	}

	private final Retrofit retrofit;
	private NetworkClient() {
		retrofit = new Builder()
				.baseUrl("https://api.github.com/")
				.addConverterFactory(GsonConverterFactory.create(new Gson()))
				.build();

	}

	public Retrofit getRetrofit() {
		return retrofit;
	}
}
