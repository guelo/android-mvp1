package miguel.mvp.ui.repodetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import miguel.mvp.R;
import miguel.mvp.model.Repo;
import miguel.mvp.ui.BaseMVPActivity;

public class RepoDetailActivity extends BaseMVPActivity<RepoDetailsPresenter> implements RepoDetailsView {

	private String repoUrl;

	@Bind(R.id.repostring) TextView tvRepoString;

	public static Intent startIntent(Repo repo, Context context) {
		Intent i = new Intent(context, RepoDetailActivity.class);
		i.putExtra("EXTRA_REPO_URL", repo.getUrl());
		return i;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.repodetail);
		ButterKnife.bind(this);

		repoUrl = getIntent().getStringExtra("EXTRA_REPO_URL");
	}

	@Override
	protected RepoDetailsPresenter instantiatePresenter() {
		return new RepoDetailsPresenterImpl(repoUrl);
	}

	@Override
	public void displayLoading() {
		tvRepoString.setText("loading...");
	}

	@Override
	public void displayError(String message) {
		tvRepoString.setText(message);
	}

	@Override
	public void displayContent(Repo data) {
		tvRepoString.setText(data.toString());
	}
}
