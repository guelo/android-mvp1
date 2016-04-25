package miguel.mvp.ui;

/**
 * No Android dependencies in our Presenters!
 */
public interface Presenter<T extends PresenterView> {
	void viewAttached(T view);
}
