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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import miguel.mvp.R;
import miguel.mvp.model.Repo;
import miguel.mvp.ui.BaseMVPActivity;
import miguel.mvp.ui.repodetails.RepoDetailActivity;
import miguel.mvp.ui.search.SearchActivity;

public class MainActivity extends BaseMVPActivity<MainPresenter> implements MainView {

	@Bind(R.id.recycler) RecyclerView recyclerView;
	@Bind(R.id.errormessage) TextView tvErrormessage;
	@Bind(R.id.progressbar) ProgressBar progressBar;
	@Bind(R.id.my_toolbar) Toolbar toolbar;

	private MyAdapter adapter;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);

		setSupportActionBar(toolbar);

		LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
		recyclerView.setLayoutManager(mLayoutManager);
		adapter = new MyAdapter();
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
			presenter.reload();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected MainPresenter instantiatePresenter() {
		return new MainPresenterImpl();
	}

	public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

		private List<Repo> repos = Collections.emptyList();

		public void setData(List<Repo> repos) {
			this.repos = repos;
			notifyDataSetChanged();
		}

		@Override
		public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			View v = LayoutInflater.from(parent.getContext())
					.inflate(R.layout.repo_row, parent, false);
			return new ViewHolder(v);
		}

		@Override
		public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
			Repo repo = repos.get(position);
			holder.tvName.setText(repo.getName());

		}

		@Override
		public int getItemCount() {
			return repos.size();
		}

		public class ViewHolder extends RecyclerView.ViewHolder {
			public TextView tvName;

			public ViewHolder(View itemView) {
				super(itemView);

				tvName = (TextView) itemView.findViewById(R.id.tvName);

				itemView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						int itemPosition = recyclerView.getChildPosition(view);
						Repo repo = repos.get(itemPosition);
						startActivity(RepoDetailActivity.startIntent(repo, MainActivity.this));
					}
				});
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void displayLoading() {
		recyclerView.setVisibility(View.GONE);
		tvErrormessage.setVisibility(View.GONE);
		progressBar.setVisibility(View.VISIBLE);
	}

	@Override
	public void displayError(String error) {
		recyclerView.setVisibility(View.GONE);
		tvErrormessage.setVisibility(View.VISIBLE);
		progressBar.setVisibility(View.GONE);

		tvErrormessage.setText(error);
	}

	@Override
	public void displayContent(List<Repo> data) {
		recyclerView.setVisibility(View.VISIBLE);
		tvErrormessage.setVisibility(View.GONE);
		progressBar.setVisibility(View.GONE);

		adapter.setData(data);

	}
}
