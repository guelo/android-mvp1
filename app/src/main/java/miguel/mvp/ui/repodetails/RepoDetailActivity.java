package miguel.mvp.ui.repodetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import miguel.mvp.R;
import miguel.mvp.model.Repo;
import miguel.mvp.network.ServerError;
import miguel.mvp.ui.MVPBase.BaseMVPActivity;
import miguel.mvp.ui.repodetails.RepoDetailContract.Presenter;
import miguel.mvp.ui.repodetails.RepoDetailContract.View;

public class RepoDetailActivity extends BaseMVPActivity<Presenter> implements View {

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
	protected Presenter instantiatePresenter() {
		return new RepoDetailsPresenter(repoUrl);
	}

	@Override
	public void displayLoading() {
		tvRepoString.setText("loading...");
	}

	@Override
	public void displayError(Throwable error) {
		if (error instanceof ServerError) {
			tvRepoString.setText(error.getMessage());
		} else if (error instanceof IOException) {
			tvRepoString.setText("network error");
		}
	}

	@Override
	public void displayContent(Repo data) {
		tvRepoString.setText(data.toString());
	}
}
