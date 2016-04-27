package miguel.mvp.ui.MVPBase;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import miguel.mvp.App;

public class BaseDaggerActivity extends AppCompatActivity {
	@Override
	public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
		super.onCreate(savedInstanceState, persistentState);

		// Perform injection so that when this call returns all dependencies will be available for use.
		((App) getApplication()).component().inject(this);

	}
}
