package com.gshindi.android.testfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

public class RegisterUserActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "RegisterUserActivity";

    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText mFirstNameField;
    private EditText mLastNameField;
    private EditText mMobileNumberField;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mEmailField = (EditText) findViewById(R.id.register_field_email);
        mPasswordField = (EditText) findViewById(R.id.register_field_password);
        mFirstNameField = (EditText) findViewById(R.id.register_field_first_name);
        mLastNameField = (EditText) findViewById(R.id.register_field_last_name);
        mMobileNumberField = (EditText) findViewById(R.id.register_field_mobile_number);

        findViewById(R.id.register_email_create_account_button).setOnClickListener(this);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                updateUI(user);
            }
        };
    }

    private void createAccount(String email, String password, String firstName, String lastName, String phoneNumber) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Toast.makeText(RegisterUserActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        hideProgressDialog();
                    }
                });
        Map<String , Object> userMap = new HashMap<>();
        userMap.put("firstName", firstName);
        userMap.put("lastName", lastName);
        userMap.put("email", email);
        userMap.put("phoneNumber", phoneNumber);
        ref.child("User").child(String.valueOf(phoneNumber)).updateChildren(userMap);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {
            findViewById(R.id.register_email_password_buttons).setVisibility(View.GONE);
            findViewById(R.id.register_email_password_fields).setVisibility(View.GONE);
            Intent myIntent = new Intent(RegisterUserActivity.this, QuestionSetListActivity.class);
            RegisterUserActivity.this.startActivity(myIntent);
            finish();

        } else {
            findViewById(R.id.register_email_password_buttons).setVisibility(View.VISIBLE);
            findViewById(R.id.register_email_password_fields).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_email_create_account_button:
                createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString(),
                        mFirstNameField.getText().toString(), mLastNameField.getText().toString(), mMobileNumberField.getText().toString());
                break;
        }
    }
}
