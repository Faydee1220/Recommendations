package com.rq.recommendations;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rq.recommendations.model.ActiveListings;
import com.rq.recommendations.model.Listing;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Created by Faydee on 2018/3/6.
 */

public class ListingAdapter extends RecyclerView.Adapter<ListingAdapter.ListingViewHolder>
implements Callback<ActiveListings> {

    private MainActivity activity;
    private ActiveListings activeListings;

    public ListingAdapter(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public void success(ActiveListings activeListings, Response response) {
        this.activeListings = activeListings;
        notifyDataSetChanged();
        activity.showList();
    }

    @Override
    public void failure(RetrofitError error) {
        activity.showError();
    }

    public class ListingViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.listingImageView) ImageView listingImageView;
        @BindView(R.id.listingTitleTextView) TextView listingTitleTextView;
        @BindView(R.id.listingShopNameTextView) TextView listingShopNameTextView;
        @BindView(R.id.listingPriceTextView) TextView listingPriceTextView;

        public ListingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindView(int position) {
            Listing listing = activeListings.results[position];
            listingTitleTextView.setText(listing.title);
            listingShopNameTextView.setText(listing.Shop.shop_name);
            listingPriceTextView.setText(listing.price);

            Picasso.with(listingImageView.getContext())
                    .load(listing.Images[0].url_570xN)
                    .into(listingImageView);
        }
    }

    @NonNull
    @Override
    public ListingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_listing, parent, false);
        ListingViewHolder listingViewHolder = new ListingViewHolder(view);
        return listingViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListingViewHolder holder, int position) {
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        if (activeListings == null || activeListings.results == null) {
            return 0;
        }
        else {
            return activeListings.results.length;
        }
    }



    public ActiveListings getActiveListings() {
        return activeListings;
    }
}
