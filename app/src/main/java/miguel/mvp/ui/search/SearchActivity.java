package miguel.mvp.ui.search;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import miguel.mvp.App;
import miguel.mvp.R;
import miguel.mvp.model.SearchResult;
import miguel.mvp.model.SearchResult.SearchItem;
import miguel.mvp.network.ServerError;
import miguel.mvp.ui.MVPBase.BaseMVPActivity;

public class SearchActivity extends BaseMVPActivity<SearchContract.SearchPresenter> implements SearchContract.SearchView {

	@Bind(R.id.searchField) EditText searchbox;
	@Bind(R.id.recycler) RecyclerView recycler;
	@Bind(R.id.errormessage) TextView tvErrorMessage;
	@Bind(R.id.progressbar) ProgressBar progressbar;

	private SearchResultsAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		ButterKnife.bind(this);

		setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
		recycler.setLayoutManager(mLayoutManager);
		adapter = new SearchResultsAdapter();
		recycler.setAdapter(adapter);
		recycler.addItemDecoration(new RecyclerView.ItemDecoration() {
			@Override
			public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
				outRect.set(0, 0, 0, 4);
			}
		});


		searchbox.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_DOWN) {
					presenter.doSearch(searchbox.getText().toString());
					return true;
				}

				return false;
			}
		});
	}

	@Override
	public void displayLoading() {
		progressbar.setVisibility(View.VISIBLE);
		tvErrorMessage.setVisibility(View.GONE);
		recycler.setVisibility(View.GONE);

	}

	@Override
	public void displayError(Throwable error) {
		if (error instanceof ServerError) {
			tvErrorMessage.setText(error.getMessage());
		} else if (error instanceof IOException) {
			tvErrorMessage.setText("network error");
		}

		tvErrorMessage.setVisibility(View.VISIBLE);
		recycler.setVisibility(View.GONE);
		progressbar.setVisibility(View.GONE);
	}

	@Override
	public void displayContent(SearchResult data) {
		adapter.setData(data.getItems());

		tvErrorMessage.setVisibility(View.GONE);
		recycler.setVisibility(View.VISIBLE);
		progressbar.setVisibility(View.GONE);

	}

	@Override
	public boolean onSupportNavigateUp() {
		onBackPressed();
		return true;
	}

	@Override
	protected SearchContract.SearchPresenter instantiatePresenter() {
		return ((App)getApplication()).component().searchPresenter();
	}

	public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.ViewHolder> {

		private List<SearchItem> items = Collections.emptyList();

		public void setData(List<SearchItem> items) {
			this.items = items;
			notifyDataSetChanged();
		}

		@Override
		public SearchResultsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result_row, parent, false);
			return new ViewHolder(v);
		}

		@Override
		public void onBindViewHolder(SearchResultsAdapter.ViewHolder holder, int position) {
			SearchItem item = items.get(position);
			holder.tvName.setText(item.getName());
			holder.tvUrl.setText(item.getUrl());
		}

		@Override
		public int getItemCount() {
			return items.size();
		}

		public class ViewHolder extends RecyclerView.ViewHolder {
			@Bind(R.id.tvName) public TextView tvName;
			@Bind(R.id.tvUrl) public TextView tvUrl;

			public ViewHolder(View itemView) {
				super(itemView);

				ButterKnife.bind(this, itemView);

			}
		}
	}
}
