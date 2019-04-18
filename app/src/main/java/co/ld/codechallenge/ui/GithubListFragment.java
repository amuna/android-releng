/*
 * Copyright (c) 2019 Yoganandh Durvasulu. All rights reserved.
 */

package co.ld.codechallenge.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import co.ld.codechallenge.R;
import co.ld.codechallenge.common.BaseFragment;
import co.ld.codechallenge.model.search.Repo;
import co.ld.codechallenge.network.Response;
import co.ld.codechallenge.ui.adapter.RepoListAdapter;
import co.ld.codechallenge.ui.factory.OnItemClickListener;
import co.ld.codechallenge.util.AppConstant;
import co.ld.codechallenge.util.EspressoIdlingResource;
import co.ld.codechallenge.viewmodel.GithubViewModel;

import static androidx.navigation.Navigation.findNavController;

/**
 * Main View for listing repositories for the searched query.
 */
public class GithubListFragment extends BaseFragment<GithubViewModel>
        implements OnItemClickListener<Repo> {

    /* Initial query */
    private static final String RAW_QUERY = "javascript";
    //private static final String RAW_QUERY = "java";
    /* Adapter holding data. */
    public RepoListAdapter mAdapter;
    private ProgressBar progressIndicator;
    private SwipeRefreshLayout mySwipeRefreshLayout;
    RecyclerView repoRecycler;
    public static List<Repo> data;

    @LayoutRes
    @Override
    protected int getLayout() {
        return R.layout.github_list_fragment;
    }

    @NonNull
    @Override
    public Class<GithubViewModel> getViewModel() {
        return GithubViewModel.class;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("YESS", "3");

        // Initialize Listeners
        initListener();
        Log.d("YESS", "5");

        // Fetch/Update Data
        getData();
        Log.d("YESS", "7");
    }

    @Override
    protected void initViews(@NonNull View view) {
        progressIndicator = view.findViewById(R.id.progress_bar);
        mySwipeRefreshLayout = view.findViewById(R.id.refresh);

        repoRecycler = view.findViewById(R.id.repo_list);

        // For performance
        repoRecycler.setHasFixedSize(true);
        // Set layout type
        repoRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        // Create Image Renderer
        RequestManager glide = Glide.with(this);
        // Create Adapter
        mAdapter = new RepoListAdapter(glide);
        // Set adapter
        repoRecycler.setAdapter(mAdapter);
    }

    /**
     * Initialize event listeners
     */
    private void initListener() {
        mAdapter.setOnItemClickLister(this);
        mySwipeRefreshLayout.setOnRefreshListener(this::getData);
        Log.d("YESS", "4");
    }

    /**
     * Fetch data
     */
    private void getData() {
        Log.d("YESS", "6.1");
        EspressoIdlingResource.increment();
        progressIndicator.setVisibility(View.VISIBLE);
        mViewModel.getRepos(RAW_QUERY)
                .observe(getViewLifecycleOwner(), this::consumeData);
        Log.d("YESS", "6.11");
    }

    /**
     * Handler for data received
     *
     * @param response data
     */
    private void consumeData(@NonNull Response<List<Repo>> response) {
        Log.d("YESS", "6.21");
        // Update indicators
        progressIndicator.setVisibility(View.GONE);
        mySwipeRefreshLayout.setRefreshing(false);

        // Get result
        data = response.getData();

        // Get error
        Throwable error = response.getError();
        if (error != null) {
            // Show error toast
            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
        } else {
            // Update data.
            mAdapter.submitList(data);
            Log.d("YESS", "6.22");
        }
        Log.d("YESS", "6.23");
        EspressoIdlingResource.decrement();
    }

    @Override
    public void onClick(@NonNull Repo item, @NonNull View view, int position) {
        // Create bundle to pass data.
        Bundle args = new Bundle();
        args.putParcelable(AppConstant.DATA_ITEM, item);

        // Get Navigation controller
        findNavController(view)
                // Navigate to next view with predefined action & data.
                .navigate(R.id.action_githubListFragment_to_githubDetailFragment, args);
    }


}
