package miguel.mvp.ui.MVPBase;

import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;

abstract public class BasePresenter<View> implements Presenter<View> {
	private WeakReference<View> viewRef;

	@Nullable
	public View getView() {
		return viewRef == null ? null : viewRef.get();
	}

	@Override @CallSuper
	public void viewAttached(View view) {
		viewRef = new WeakReference<>(view);
	}

}
