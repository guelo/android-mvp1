package miguel.mvp.ui.main;

import java.util.List;

import miguel.mvp.model.Repo;
import miguel.mvp.ui.MVPBase.LoadingContentErrorView;

public interface MainContract {

	/*
	interface View {
		void displayLoading();
		void displayError(Throwable error);
		void displayContent(List<Repo> data);
	}
	*/


	interface View extends LoadingContentErrorView<List<Repo>> {}


	interface Presenter extends miguel.mvp.ui.MVPBase.Presenter<View> {
		void loadRepos();
	}
}
