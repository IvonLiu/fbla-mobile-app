package com.ivon.fbla;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by Owner on 2/18/2016.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
