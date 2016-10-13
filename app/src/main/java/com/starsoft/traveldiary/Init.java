package com.starsoft.traveldiary;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.codemybrainsout.onboarder.AhoyOnboarderActivity;
import com.codemybrainsout.onboarder.AhoyOnboarderCard;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aashish on 9/14/2016.
 */
public class Init extends AhoyOnboarderActivity {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private LocationRequest mLocationRequest;
    protected LocationSettingsRequest mLocationSettingsRequest;
    private static final String TAG = Init.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);

        AhoyOnboarderCard ahoyOnboarderCard1 = new AhoyOnboarderCard("City Guide", "Detailed guides to help you plan your trip.", R.drawable.backpack);
        AhoyOnboarderCard ahoyOnboarderCard2 = new AhoyOnboarderCard("Travel Blog", "Share your travel experiences with a vast network of fellow travellers.", R.drawable.chalk);
        AhoyOnboarderCard ahoyOnboarderCard3 = new AhoyOnboarderCard("Chat", "Connect with like minded people and exchange your travel stories.", R.drawable.chat);

        ahoyOnboarderCard1.setBackgroundColor(R.color.black_transparent);
        ahoyOnboarderCard2.setBackgroundColor(R.color.black_transparent);
        ahoyOnboarderCard3.setBackgroundColor(R.color.black_transparent);

        List<AhoyOnboarderCard> pages = new ArrayList<>();

        pages.add(ahoyOnboarderCard1);
        pages.add(ahoyOnboarderCard2);
        pages.add(ahoyOnboarderCard3);

        for (AhoyOnboarderCard page : pages) {
            page.setTitleColor(R.color.white);
            page.setDescriptionColor(R.color.grey_200);
        }

        setFinishButtonTitle("Let's Start");
        showNavigationControls(true);
        setGradientBackground();

        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
        setFont(face);

        sharedPreferences = getApplicationContext().getSharedPreferences("pref_user",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if(sharedPreferences.getBoolean("isFirst",true)) {
            setOnboardPages(pages);
        }
        else{
            if(sharedPreferences.getBoolean("isLogin",true)){
                startActivity(new Intent(this,MActivity.class));
                finish();
            }
            else{
                startActivity(new Intent(this,LoginActivity.class));
                finish();
            }

        }
    }

    @Override
    public void onFinishButtonPressed() {
        //Toast.makeText(this, "Finish Pressed", Toast.LENGTH_SHORT).show();
        editor.putBoolean("isFirst",false);
        editor.commit();
        requestLocationPermission();
    }

    @Override
    protected void onStart() {
        super.onStart();
        buildLocationSettingsRequest();
    }

    protected void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    private void requestLocationPermission(){

        // BEGIN_INCLUDE(location_permission_request)
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)) {
            Log.i(TAG, "Displaying location permission rationale to provide additional context.");
        } else {

            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        //if(requestCode == Manifest.permission.ACCESS_COARSE_LOCATION){

            //If permission is granted
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                //Displaying a toast
                startActivity(new Intent(this,LoginActivity.class));
                finish();

            }else{
                //Displaying another toast if permission is not granted
                Toast.makeText(this,"Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }

    }
}
