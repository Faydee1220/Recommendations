package com.rq.recommendations;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.plus.PlusOneButton;
import com.google.android.gms.plus.PlusShare;
import com.rq.recommendations.api.Etsy;
import com.rq.recommendations.google.GoogleServicesHelper;
import com.rq.recommendations.model.ActiveListings;
import com.rq.recommendations.model.Listing;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Created by Faydee on 2018/3/6.
 */

public class ListingAdapter extends RecyclerView.Adapter<ListingAdapter.ListingViewHolder>
implements Callback<ActiveListings>, GoogleServicesHelper.GoogleServicesListener {

    public static final int REQUEST_CODE_PLUS_ONE = 10;
    public static final int REQUEST_CODE_SHARE = 11;

    private MainActivity activity;
    private ActiveListings activeListings;
    private boolean isGooglePlayServicesAvailable = false;

    public ListingAdapter(MainActivity activity) {
        this.activity = activity;
    }

    public class ListingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.listingImageView) ImageView listingImageView;
        @BindView(R.id.listingTitleTextView) TextView titleTextView;
        @BindView(R.id.listingShopNameTextView) TextView shopNameTextView;
        @BindView(R.id.listingPriceTextView) TextView priceTextView;
        @BindView(R.id.listingPlusOneButton) PlusOneButton plusOneButton;
        @BindView(R.id.listingShareImageButton) ImageButton shareImageButton;

        public ListingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void bindView(int position) {
            Listing listing = activeListings.results[position];
            titleTextView.setText(listing.title);
            shopNameTextView.setText(listing.Shop.shop_name);
            priceTextView.setText(listing.price);

            Picasso.with(listingImageView.getContext())
                    .load(listing.Images[0].url_570xN)
                    .into(listingImageView);

            if (isGooglePlayServicesAvailable) {
                plusOneButton.setVisibility(View.VISIBLE);
                plusOneButton.initialize(listing.url, REQUEST_CODE_PLUS_ONE);
                plusOneButton.setAnnotation(PlusOneButton.ANNOTATION_NONE);
            }
            else {
                plusOneButton.setVisibility(View.GONE);
            }
        }

        @OnClick(R.id.listingShareImageButton) void shareButtonPressed() {
            Listing listing = activeListings.results[getAdapterPosition()];
            if (isGooglePlayServicesAvailable) {
                Intent intent = new PlusShare.Builder(activity)
                        .setType("text/plain")
                        .setText("Check out this item on Etsy" + listing.title)
                        .setContentUrl(Uri.parse(listing.url))
                        .getIntent();
                activity.startActivityForResult(intent, REQUEST_CODE_SHARE);
            }
            else {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, "Check out this item on Etsy" +
                        listing.title +
                        " " +
                        listing.url);
                intent.setType("text/plain");
                activity.startActivityForResult(Intent.createChooser(intent, "Share"),
                        REQUEST_CODE_SHARE);
            }
        }

        @Override
        public void onClick(View v) {
            Intent openListing = new Intent(Intent.ACTION_VIEW);
            openListing.setData(Uri.parse(activeListings.results[getAdapterPosition()].url));
            activity.startActivity(openListing);
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

    @Override
    public void onConnected() {
        if (getItemCount() == 0) {
            Etsy.getActiveListing(this);
        }
        isGooglePlayServicesAvailable = true;
        notifyDataSetChanged();
    }

    @Override
    public void onDisconnected() {
        if (getItemCount() == 0) {
            Etsy.getActiveListing(this);
        }
        isGooglePlayServicesAvailable = false;
        notifyDataSetChanged();
    }
}
