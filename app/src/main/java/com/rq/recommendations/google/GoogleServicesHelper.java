package com.rq.recommendations.google;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

/**
 * Created by Faydee on 2018/3/6.
 */

public class GoogleServicesHelper implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    public interface GoogleServicesListener {
        void onConnected();
        void onDisconnected();
    }

    private static final int REQUEST_CODE_RESOLUTION = 100;
    private static final int REQUEST_CODE_AVAILABILITY = 101;
    private static final String CLIENT_ID = "681257884956-aubm88gr1aot23bogs57nvqivotr7vgh.apps.googleusercontent.com";

    private Activity activity;
    private GoogleServicesListener listener;
    private GoogleApiClient googleApiClient;

    public GoogleServicesHelper(Activity activity, GoogleServicesListener listener) {
        this.activity = activity;
        this.listener = listener;
        googleApiClient = new GoogleApiClient.Builder(activity)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API,
                        Plus.PlusOptions.builder()
                                .setServerClientId(CLIENT_ID)
                                .build())
                .build();
    }

    public void connect() {
        if (isGooglePlayServicesAvailable()) {
            googleApiClient.connect();
        }
        else {
            listener.onDisconnected();
        }

    }

    public void disconnect() {
        if (isGooglePlayServicesAvailable()) {
            googleApiClient.disconnect();
        }
        else {
            listener.onDisconnected();
        }
    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int availability = apiAvailability.isGooglePlayServicesAvailable(activity);
        switch (availability) {
            case ConnectionResult.SUCCESS:
                return true;
            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
            case ConnectionResult.SERVICE_DISABLED:
            case ConnectionResult.SERVICE_INVALID:
                apiAvailability.getErrorDialog(activity, availability, REQUEST_CODE_AVAILABILITY).show();
                return false;
            default:
                return false;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        listener.onConnected();
    }

    @Override
    public void onConnectionSuspended(int i) {
        listener.onDisconnected();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(activity, REQUEST_CODE_RESOLUTION);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
                connect();
            }
        }
        else {
            listener.onDisconnected();
        }
    }

    public void handleActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_CODE_AVAILABILITY || requestCode == REQUEST_CODE_RESOLUTION) {
            if (resultCode == Activity.RESULT_OK) {
                connect();
            }
            else {
                listener.onDisconnected();
            }
        }
    }

}
