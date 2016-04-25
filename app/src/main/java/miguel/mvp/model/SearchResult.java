package miguel.mvp.model;

import java.util.List;

public class SearchResult {
	private List<SearchItem> items;
	private int total_count;
	private boolean incomplete_results;

	public static class SearchItem {
		private String name;
		private String path;
		private String url;
		private Repo repository;

		public String getName() {
			return name;
		}

		public String getPath() {
			return path;
		}

		public String getUrl() {
			return url;
		}

		public Repo getRepository() {
			return repository;
		}
	}

	public List<SearchItem> getItems() {
		return items;
	}

	public int getTotal_count() {
		return total_count;
	}

	public boolean isIncomplete_results() {
		return incomplete_results;
	}
}
