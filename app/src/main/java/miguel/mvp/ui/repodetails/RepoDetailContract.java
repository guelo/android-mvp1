package miguel.mvp.ui.repodetails;

import miguel.mvp.model.Repo;
import miguel.mvp.ui.MVPBase.LoadingContentErrorView;

public interface RepoDetailContract {
	interface View extends LoadingContentErrorView<Repo> { }

	interface Presenter extends miguel.mvp.ui.MVPBase.Presenter<View> { }

}
