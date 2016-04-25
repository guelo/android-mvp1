package miguel.mvp.ui;

import android.support.v7.app.AppCompatActivity;

abstract public class BaseMVPActivity<P extends Presenter> extends AppCompatActivity implements PresenterView {

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

	@Override
	public Object onRetainCustomNonConfigurationInstance() {
		return presenter;
	}

	protected abstract P instantiatePresenter();

}
