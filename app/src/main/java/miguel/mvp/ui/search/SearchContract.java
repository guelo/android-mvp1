package miguel.mvp.ui.search;

import miguel.mvp.model.SearchResult;
import miguel.mvp.ui.MVPBase.LoadingContentErrorView;
import miguel.mvp.ui.MVPBase.Presenter;

public interface SearchContract {
	interface SearchView extends LoadingContentErrorView<SearchResult> { }

	interface SearchPresenter extends Presenter<SearchView> {
		void doSearch(String term);
	}
}
