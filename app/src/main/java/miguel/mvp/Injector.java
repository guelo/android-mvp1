package miguel.mvp;

import miguel.mvp.network.GitHubService;
import miguel.mvp.network.NetworkClient;

public class Injector {
	public static boolean DEBUG_MODE = false;

	public static GitHubService provideGithubService() {
		if (DEBUG_MODE) {
			return mockGithhubService();
		} else {
			return gitHubService();
		}
	}

	private static GitHubService mockGithhubService() {
		return null;
	}

	private static GitHubService gitHubService() {
		return NetworkClient.getInstance().getRetrofit().create(GitHubService.class);
	}



}
