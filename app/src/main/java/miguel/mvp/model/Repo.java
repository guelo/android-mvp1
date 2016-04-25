package miguel.mvp.model;

public class Repo {
	private int id;
	private String name;
	private String full_name;
	private String html_url;
	private String description;
	private String url;
	private int size;
	private int stargazers_count;
	private int watchers_count;
	private String language;

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getFull_name() {
		return full_name;
	}

	public String getHtml_url() {
		return html_url;
	}

	public String getDescription() {
		return description;
	}

	public String getUrl() {
		return url;
	}

	public int getSize() {
		return size;
	}

	public int getStargazers_count() {
		return stargazers_count;
	}

	public int getWatchers_count() {
		return watchers_count;
	}

	public String getLanguage() {
		return language;
	}

	@Override
	public String toString() {
		return "Repo{" +
				"id=" + id +
				", name='" + name + '\'' +
				", full_name='" + full_name + '\'' +
				", html_url='" + html_url + '\'' +
				", description='" + description + '\'' +
				", url='" + url + '\'' +
				", size=" + size +
				", stargazers_count=" + stargazers_count +
				", watchers_count=" + watchers_count +
				", language='" + language + '\'' +
				'}';
	}
}
