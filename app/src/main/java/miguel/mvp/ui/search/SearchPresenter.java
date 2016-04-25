package miguel.mvp.ui.search;

import miguel.mvp.ui.Presenter;

public interface SearchPresenter extends Presenter<SearchView> {
	void doSearch(String term);
}
