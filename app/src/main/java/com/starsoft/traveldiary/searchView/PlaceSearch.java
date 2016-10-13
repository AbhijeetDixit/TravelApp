package com.starsoft.traveldiary.searchView;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

/**
 * Created by Aashish on 9/23/2016.
 */
public class PlaceSearch implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private static final String TAG = PlaceSearch.class.getSimpleName();
    private Context context;
    public GoogleApiClient mGoogleApiClient;
    private boolean isConnected = false;
    private static final LatLngBounds BOUNDS_WORLD = new LatLngBounds(
            new LatLng(-85, 180), new LatLng(85, -180));

    public PlaceSearch(Context con){
        this.context = con;
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        connectClient();
    }

    public void init() {
    }



    public GoogleApiClient getmGoogleApiClient(){
     return mGoogleApiClient;
    }

    public LatLngBounds getBoundsWorld(){
        return BOUNDS_WORLD;
    }


    public boolean connectClient(){
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();

            return true;
    }


    public void disconnectClient(){
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
            isConnected = false;
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.e(TAG, "Google API client connected");
        isConnected = true;
        init();
    }



    public boolean isClientConnected(){
        return isConnected;
    }


    @Override
    public void onConnectionSuspended(int i) {
        Log.e(TAG, "Google API client suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());

        // TODO(Developer): Check error code and notify the user of error state and resolution.
        Toast.makeText(context,
                "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();
    }
}
