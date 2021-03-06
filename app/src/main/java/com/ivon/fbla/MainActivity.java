package com.ivon.fbla;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_TYPE = "type";
    public static final int TYPE_MAIN = 0;
    public static final int TYPE_REVIEW = 1;
    public static final int TYPE_SAVE = 2;

    private OnFabClickListener mFabClickListener;
    public void setFabClickListener(OnFabClickListener listener) {
        mFabClickListener = listener;
    }

    private FloatingActionButton mFab;

    public FloatingActionButton getFab() {
        return mFab;
    }

    Firebase mFirebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mFirebaseRef = new Firebase("https://fbla-mobile-app.firebaseio.com");
        AuthData auth = mFirebaseRef.getAuth();
        if (auth == null) {
            Logger.log("Not logged in");
            Intent intent = new Intent(this, AccountActivity.class);
            startActivity(intent);
            finish();
        } else {
            Logger.log("Logged in as: " + auth.getUid());
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (!isTaskRoot()) {
            toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        }

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setBackgroundTintList(new ColorStateList(new int[][]{{0}}, new int[] {Utils.getThemePrimaryColor(this)}));
        mFab.setRippleColor(Utils.getThemeAccentColor(this));
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFabClickListener != null) {
                    mFabClickListener.onFabClick((FloatingActionButton) view);
                }
            }
        });

        int type = getIntent().getIntExtra(EXTRA_TYPE, TYPE_MAIN);
        switch(type) {
            case TYPE_MAIN:
            default:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment, new PhotosFragment())
                        .commit();
                getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
                getFab().setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_camera));
                break;
            case TYPE_REVIEW:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment, ReviewsFragment.newInstance(getIntent().getStringExtra(ReviewsFragment.EXTRA_PHOTO_ID)))
                        .commit();
                getSupportActionBar().setTitle("Reviews");
                getFab().setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_add));
                break;
            case TYPE_SAVE:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment, SavePhotoFragment.newInstance(getIntent().getStringExtra(SavePhotoFragment.EXTRA_IMAGE)))
                        .commit();
                getSupportActionBar().setTitle("Save Photo");
                getFab().setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_done));
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        if (isTaskRoot()) {
            menu.findItem(R.id.action_logout).setVisible(true);
        } else {
            menu.findItem(R.id.action_logout).setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id ==  android.R.id.home) {
            if (!isTaskRoot()) {
                finish();
            }
        } else if (id == R.id.action_logout) {
            mFirebaseRef.unauth();
            Intent intent = new Intent(this, AccountActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
