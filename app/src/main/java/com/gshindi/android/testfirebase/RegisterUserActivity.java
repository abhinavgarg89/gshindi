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

//    private EditText editTextName;
//    private EditText editTextAddress;
//    private TextView textViewPersons;
//    private Button buttonSave;

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
                // [START_EXCLUDE]
                updateUI(user);
                // [END_EXCLUDE]
            }
        };
    }

//        buttonSave = (Button) findViewById(R.id.buttonSave);
//        editTextName = (EditText) findViewById(R.id.editTextName);
//        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
//
//        textViewPersons = (TextView) findViewById(R.id.textViewPersons);
//
//        buttonSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Creating firebase object
//                Firebase ref = new Firebase(Config.FIREBASE_URL);
//
//                //Getting values to store
//                String name = editTextName.getText().toString().trim();
//                String address = editTextAddress.getText().toString().trim();
//
//                //Creating User object
//                User person = new User();
//
//                //Adding values
//                person.setName(name);
//                person.setAddress(address);
//
//                //Storing values to firebase
//                ref.child("User").setValue(person);
//
//
//                //Value event listener for realtime data update
//                ref.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot snapshot) {
//                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
//                            //Getting the data from snapshot
//                            User person = postSnapshot.getValue(User.class);
//
//                            //Adding it to a string
//                            String string = "Name: "+person.getName()+"\nAddress: "+person.getAddress()+"\n\n";
//
//                            //Displaying it on textview
//                            textViewPersons.setText(string);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(FirebaseError firebaseError) {
//                        System.out.println("The read failed: " + firebaseError.getMessage());
//                    }
//                });
//
//            }
//        });
//    }

    private void createAccount(String email, String password, String firstName, String lastName, String phoneNumber) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(RegisterUserActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
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
    // [END on_start_add_listener]

    // [START on_stop_remove_listener]
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
//            mStatusTextView.setText(getString(R.string.emailpassword_status_fmt, user.getEmail()));
//            mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));

            findViewById(R.id.register_email_password_buttons).setVisibility(View.GONE);
            findViewById(R.id.register_email_password_fields).setVisibility(View.GONE);
            Intent myIntent = new Intent(RegisterUserActivity.this, QuestionSetListActivity.class);
            RegisterUserActivity.this.startActivity(myIntent);
            finish();

        } else {
//            mStatusTextView.setText(R.string.sign_out);
//            mDetailTextView.setText(null);

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
//            case R.id.email_sign_in_button:
//                signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
//                break;
        }
    }
}
