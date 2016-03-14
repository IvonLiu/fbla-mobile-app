package com.ivon.fbla;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Owner on 3/14/2016.
 */
public class AccountActivity extends AppCompatActivity {

    public static final String EXTRA_TYPE = "type";
    public static final int TYPE_LOGIN = 0;
    public static final int TYPE_SIGNUP = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        int type = getIntent().getIntExtra(EXTRA_TYPE, TYPE_LOGIN);
        switch(type) {
            case TYPE_LOGIN:
            default:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.login_frame, new LoginFragment())
                        .commit();
                break;
            case TYPE_SIGNUP:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.login_frame, new SignupFragment())
                        .commit();
                break;
        }

    }

}
