package miguel.mvp.ui.MVPBase;

public interface LoadingContentErrorView<T> {
	void displayLoading();
	void displayError(Throwable error);
	void displayContent(T data);
}
