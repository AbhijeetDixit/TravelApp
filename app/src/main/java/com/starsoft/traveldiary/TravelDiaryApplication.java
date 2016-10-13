package com.starsoft.traveldiary;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Aashish on 10/13/2016.
 */

public class TravelDiaryApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
