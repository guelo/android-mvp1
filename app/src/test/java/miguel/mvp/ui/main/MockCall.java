package miguel.mvp.ui.main;

import java.io.IOException;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MockCall<T> implements Call<T> {
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
