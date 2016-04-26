package miguel.mvp.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import miguel.mvp.Injector;
import miguel.mvp.R;
import miguel.mvp.model.Repo;
import miguel.mvp.network.ServerError;
import miguel.mvp.ui.MVPBase.BaseMVPActivity;
import miguel.mvp.ui.main.MainContract.Presenter;
import miguel.mvp.ui.main.MainContract.View;
import miguel.mvp.ui.repodetails.RepoDetailActivity;
import miguel.mvp.ui.search.SearchActivity;

public class MainActivity extends BaseMVPActivity<Presenter> implements View {

	@Bind(R.id.recycler) RecyclerView recyclerView;
	@Bind(R.id.errormessage) TextView tvErrormessage;
	@Bind(R.id.progressbar) ProgressBar progressBar;
	@Bind(R.id.my_toolbar) Toolbar toolbar;

	private Adapter adapter;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);

		setSupportActionBar(toolbar);

		LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
		recyclerView.setLayoutManager(mLayoutManager);
		adapter = new Adapter();
		recyclerView.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_search) {
			startActivity(new Intent(this, SearchActivity.class));
			return true;
		}
		if (id == R.id.action_refresh) {
			presenter.load();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected Presenter instantiatePresenter() {
		return new MainPresenter(Injector.provideGithubService());
	}

	public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

		private List<Repo> repos = Collections.emptyList();

		public void setData(List<Repo> repos) {
			this.repos = repos;
			notifyDataSetChanged();
		}

		@Override
		public Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			android.view.View v = LayoutInflater.from(parent.getContext())
					.inflate(R.layout.repo_row, parent, false);
			return new ViewHolder(v);
		}

		@Override
		public void onBindViewHolder(Adapter.ViewHolder holder, int position) {
			Repo repo = repos.get(position);
			holder.tvName.setText(repo.getName());

		}

		@Override
		public int getItemCount() {
			return repos.size();
		}

		public class ViewHolder extends RecyclerView.ViewHolder {
			public TextView tvName;

			public ViewHolder(android.view.View itemView) {
				super(itemView);

				tvName = (TextView) itemView.findViewById(R.id.tvName);

				itemView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(android.view.View view) {
						int itemPosition = recyclerView.getChildPosition(view);
						Repo repo = repos.get(itemPosition);
						startActivity(RepoDetailActivity.startIntent(repo, MainActivity.this));
					}
				});
			}
		}
	}

	@Override
	public void displayLoading() {
		recyclerView.setVisibility(android.view.View.GONE);
		tvErrormessage.setVisibility(android.view.View.GONE);
		progressBar.setVisibility(android.view.View.VISIBLE);
	}

	@Override
	public void displayError(Throwable error) {
		recyclerView.setVisibility(android.view.View.GONE);
		tvErrormessage.setVisibility(android.view.View.VISIBLE);
		progressBar.setVisibility(android.view.View.GONE);

		if (error instanceof ServerError) {
			tvErrormessage.setText(error.getMessage());
		} else if (error instanceof IOException) {
			tvErrormessage.setText("network error");
		}
	}

	@Override
	public void displayContent(List<Repo> data) {
		recyclerView.setVisibility(android.view.View.VISIBLE);
		tvErrormessage.setVisibility(android.view.View.GONE);
		progressBar.setVisibility(android.view.View.GONE);

		adapter.setData(data);

	}
}
