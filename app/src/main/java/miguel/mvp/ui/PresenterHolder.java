package miguel.mvp.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

public class PresenterHolder {

	private static PresenterHolder singleton;

	private Map<Integer, Presenter> presenterMap;

	private PresenterHolder() {
		this.presenterMap = new HashMap<>();
	}

	public static PresenterHolder getSingleton() {
		if (singleton == null) {
			synchronized (PresenterHolder.class) {
				if (singleton == null) {
					singleton = new PresenterHolder();
				}
			}
		}
		return singleton;
	}

	public void putPresenter(@NonNull Presenter p, @NonNull Intent intent) {
		presenterMap.put(intent.hashCode(), p);
	}

	@Nullable public Presenter getPresenter(@NonNull Intent intent) {
		return presenterMap.get(intent.hashCode());
	}

	public void remove(Intent intent) {
		presenterMap.remove(intent.hashCode());
	}

}
