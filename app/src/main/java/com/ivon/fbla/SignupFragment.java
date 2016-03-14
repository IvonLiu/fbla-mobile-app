package com.ivon.fbla;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

/**
 * Created by Owner on 3/14/2016.
 */
public class SignupFragment extends Fragment implements View.OnClickListener {

    private Firebase mFirebaseRef;

    private EditText mUsernameText;
    private EditText mEmailText;
    private EditText mPasswordText;

    private Button mSignupButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_signup, container, false);

        mUsernameText = (EditText) rootView.findViewById(R.id.username);
        mEmailText = (EditText) rootView.findViewById(R.id.email);
        mPasswordText = (EditText) rootView.findViewById(R.id.password);
        mSignupButton = (Button) rootView.findViewById(R.id.signup_button);

        mSignupButton.setOnClickListener(this);

        mFirebaseRef = new Firebase("https://fbla-mobile-app.firebaseio.com");

        return rootView;
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()) {

            case R.id.signup_button:
                signup();
                break;

        }

    }

    private void signup() {

        final String username = mUsernameText.getText().toString();
        final String email = mEmailText.getText().toString();
        final String password = mPasswordText.getText().toString();

        mFirebaseRef.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                Logger.log("Created use account with uid: " + result.get("uid") + ", username: " + username);
                mFirebaseRef.child("usernames").child((String) result.get("uid")).setValue(username);
                getActivity().finish();
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                Logger.error("Error creating user: " + firebaseError.toString());
                Snackbar.make(getView(), "Sign up failed", Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}
