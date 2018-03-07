package com.rq.recommendations;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rq.recommendations.api.Etsy;
import com.rq.recommendations.google.GoogleServicesHelper;
import com.rq.recommendations.model.ActiveListings;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String STATE_ACTIVE_LISTINGS = "stateActiveListings";

    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.errorTextView) TextView errorTextView;

    private ListingAdapter adapter;
    private GoogleServicesHelper googleServicesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setRecyclerView();
        setGoogleServicesHelper();

        showLoading();
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_ACTIVE_LISTINGS)) {
                adapter.success((ActiveListings) savedInstanceState.getParcelable(STATE_ACTIVE_LISTINGS), null);
            }
        }

//        if (savedInstanceState == null) {
//            showLoading();
//            Etsy.getActiveListing(adapter);
//        }
//        else {
//            if (savedInstanceState.containsKey(STATE_ACTIVE_LISTINGS)) {
//                adapter.success((ActiveListings) savedInstanceState.getParcelable(STATE_ACTIVE_LISTINGS), null);
//                showList();
//            }
//            else {
//                showLoading();
//                Etsy.getActiveListing(adapter);
//            }
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleServicesHelper.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleServicesHelper.disconnect();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode > 65535) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        googleServicesHelper.handleActivityResult(requestCode, resultCode, data);
        if (requestCode == ListingAdapter.REQUEST_CODE_PLUS_ONE) {
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ActiveListings activeListings = adapter.getActiveListings();
        if (activeListings != null) {
            outState.putParcelable(STATE_ACTIVE_LISTINGS, activeListings);
        }
    }

    private void setRecyclerView() {
        adapter = new ListingAdapter(this);
        recyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(1,
                StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void setGoogleServicesHelper() {
        googleServicesHelper = new GoogleServicesHelper(this, adapter);
    }

    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        errorTextView.setVisibility(View.GONE);
    }

    public void showList() {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        errorTextView.setVisibility(View.GONE);
    }

    public void showError() {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        errorTextView.setVisibility(View.VISIBLE);
    }
}
