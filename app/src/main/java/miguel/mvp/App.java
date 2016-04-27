package miguel.mvp;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Component;
import miguel.mvp.ui.MVPBase.BaseDaggerActivity;
import miguel.mvp.ui.main.MainPresenter;
import miguel.mvp.ui.repodetails.RepoDetailsPresenter;
import miguel.mvp.ui.search.SearchPresenter;

public class App extends Application {
	private ApplicationComponent component;

	@Singleton // Constraints this component to one-per-application or unscoped bindings.
	@Component(modules = AndroidModule.class)
	public interface ApplicationComponent {
		void inject(BaseDaggerActivity activity);
		MainPresenter mainPresenter();
		SearchPresenter searchPresenter();
		void inject(RepoDetailsPresenter presenter);
	}

	@Override public void onCreate() {
		super.onCreate();

		component = DaggerApp_ApplicationComponent.builder()
				.build();
	}

	public ApplicationComponent component() {
		return component;
	}
}
