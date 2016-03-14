package com.ivon.fbla;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

/**
 * Created by Owner on 3/14/2016.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    private Firebase mFirebaseRef;

    private EditText mEmailText;
    private EditText mPasswordText;

    private Button mLoginButton;
    private Button mSignupButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        mEmailText = (EditText) rootView.findViewById(R.id.email);
        mPasswordText = (EditText) rootView.findViewById(R.id.password);
        mLoginButton = (Button) rootView.findViewById(R.id.login_button);
        mSignupButton = (Button) rootView.findViewById(R.id.signup_button);

        mLoginButton.setOnClickListener(this);
        mSignupButton.setOnClickListener(this);

        mFirebaseRef = new Firebase("https://fbla-mobile-app.firebaseio.com");

        return rootView;
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()) {

            case R.id.login_button:
                login();
                break;
            case R.id.signup_button:
                signup();
                break;

        }

    }

    private void login() {

        String email = mEmailText.getText().toString();
        String password = mPasswordText.getText().toString();

        mFirebaseRef.authWithPassword(email, password, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                Logger.log("Authenticated user with uid: " + authData.getUid());
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                Logger.error("Error authenticating user: " + firebaseError.toString());
                Snackbar.make(getView(), "Email or password is incorrect", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void signup() {
        Intent intent = new Intent(getActivity(), AccountActivity.class);
        intent.putExtra(AccountActivity.EXTRA_TYPE, AccountActivity.TYPE_SIGNUP);
        startActivity(intent);
    }
}
