package com.gshindi.android.testfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class WelcomeActivity extends BaseActivity implements View.OnClickListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        findViewById(R.id.new_users_link).setOnClickListener(this);
        findViewById(R.id.existing_users_link).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_users_link:
                Intent myIntent = new Intent(WelcomeActivity.this, RegisterUserActivity.class);
                WelcomeActivity.this.startActivity(myIntent);
                finish();
                break;
            case R.id.existing_users_link:
                Intent myIntent1 = new Intent(WelcomeActivity.this, LoginActivity.class);
                WelcomeActivity.this.startActivity(myIntent1);
                finish();
                break;
        }
    }
}
