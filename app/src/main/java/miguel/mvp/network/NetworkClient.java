package miguel.mvp.network;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
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

		OkHttpClient client = new OkHttpClient.Builder()
/*
			.addNetworkInterceptor(new Interceptor() {
				@Override
				public Response intercept(Chain chain) throws IOException {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					return chain.proceed(chain.request());
				}
			})
*/
			.build();
		retrofit = new Builder()
				.baseUrl("https://api.github.com/")
				.addConverterFactory(GsonConverterFactory.create(new Gson()))
				.client(client)
				.build();

	}

	public Retrofit getRetrofit() {
		return retrofit;
	}
}
