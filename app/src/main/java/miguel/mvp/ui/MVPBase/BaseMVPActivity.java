package miguel.mvp.ui.MVPBase;

abstract public class BaseMVPActivity<P extends Presenter> extends BaseDaggerActivity {

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

}
