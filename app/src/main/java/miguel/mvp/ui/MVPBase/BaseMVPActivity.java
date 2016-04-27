package miguel.mvp.ui.MVPBase;

import android.support.v7.app.AppCompatActivity;

abstract public class BaseMVPActivity<P extends Presenter> extends AppCompatActivity {

	protected P presenter;

	@Override
	protected void onStart() {
		super.onStart();
		if (presenter == null) {
			//noinspection unchecked
			presenter = (P) getLastCustomNonConfigurationInstance();
		}
		if (presenter == null) {
			presenter = instantiatePresenter();
		}
		presenter.viewAttached(this);

	}

	@Override public void onDestroy() {
		super.onDestroy();

		presenter = null;
	}


	@Override
	public Object onRetainCustomNonConfigurationInstance() {
		return presenter;
	}

	protected abstract P instantiatePresenter();

	public void setPresenter(P presenter) {
		this.presenter = presenter;
	}
}
