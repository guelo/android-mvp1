package miguel.mvp.ui.main;

import android.app.Activity;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.MenuItem;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import miguel.mvp.ui.main.MainContract.Presenter;
import miguel.mvp.ui.main.MainContract.View;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

	@Rule
	public final ActivityTestRule<MainActivity> activityTestRule =
			new ActivityTestRule<>(MainActivity.class);

	@Test
	public void test() {
		MainActivity activity = activityTestRule.getActivity();
		final boolean[] loadCalled = {false};
		activity.setPresenter(new Presenter() {
			@Override
			public void loadRepos() {
				loadCalled[0] = true;
			}

			@Override
			public void viewAttached(View view) {

			}
		});
		activity.clickRefresh();

		Assert.assertTrue(loadCalled[0]);
	}

}
