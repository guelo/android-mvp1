package miguel.mvp.ui;

public interface LoadingContentErrorView<T> extends PresenterView {
	void displayLoading();
	void displayError(String message);
	void displayContent(T data);
}
