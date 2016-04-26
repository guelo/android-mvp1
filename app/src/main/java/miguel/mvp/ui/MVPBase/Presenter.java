package miguel.mvp.ui.MVPBase;

/**
 * No Android dependencies in our Presenters!
 */
public interface Presenter<View> {
	void viewAttached(View view);
}
