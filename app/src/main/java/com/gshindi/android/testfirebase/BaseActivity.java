package com.gshindi.android.testfirebase;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by abhinavgarg on 10/07/16.
 */
public class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";
    public String answerSheetName_ = null;
    public static Map<Integer, Integer> answers_ = new HashMap<>();

    public static String userEmail_ = "dummy_user";

    public ProgressDialog mProgressDialog;
    public FirebaseAuth mAuth;

    public Firebase ref;

    public FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        mAuth = FirebaseAuth.getInstance();
        ref = new Firebase(Config.FIREBASE_URL);
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }

    public void signOut() {
        mAuth.signOut();
        updateUI(null);
    }

    public void updateUI(Object o) {
        return;
    }

}
