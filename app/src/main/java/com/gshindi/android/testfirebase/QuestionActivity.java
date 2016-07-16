package com.gshindi.android.testfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class QuestionActivity extends BaseActivity {

    private int current_index = 0;
    private int max_index = 0;
    private JSONArray jArray;
    TextView questionTextView, secondsRemaining;
    CheckBox[] options = new CheckBox[4];
    Button nextButton, previousButton;
    private int totalScore;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        questionTextView = (TextView) findViewById(R.id.quesion_text);
        secondsRemaining = (TextView) findViewById(R.id.seconds_remaining);
        options[0] = (CheckBox) findViewById(R.id.option_1);
        options[1] = (CheckBox) findViewById(R.id.option_2);
        options[2] = (CheckBox) findViewById(R.id.option_3);
        options[3] = (CheckBox) findViewById(R.id.option_4);
        nextButton = (Button) findViewById(R.id.next_button);
        previousButton = (Button) findViewById(R.id.previous_button);


        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                secondsRemaining.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                startResultActivity();
            }

        }.start();

        int ctr;
        String questionSetName = getIntent().getExtras().get("selectedQuestionPaper").toString();
        try {
            int resourceId = this.getResources().getIdentifier(questionSetName, "raw", this.getPackageName());
            InputStream inputStream = getResources().openRawResource(resourceId);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ctr = inputStream.read();
            while (ctr != -1) {
                byteArrayOutputStream.write(ctr);
                ctr = inputStream.read();
            }
            inputStream.close();

            Log.v("Text Data", byteArrayOutputStream.toString());
            JSONObject jObject = new JSONObject(byteArrayOutputStream.toString());
            jArray = jObject.getJSONArray("questions");
            max_index = jArray.length() - 1;
            setNextData(current_index);

            nextButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.d("NextButton", "nextButton tapped");
                    for(int index = 0; index < 4; index++) {
                        if (options[index].isChecked()) {
                            answers_.put(current_index, index);
                        }
                    }
                    current_index++;
                    setNextData(current_index);
                }
            });
            previousButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.d("PreviousButton", "previousButton tapped");
                    for(int index = 0; index < 4; index++) {
                        if (options[index].isChecked()) {
                            answers_.put(current_index, index);
                        }
                    }
                    current_index--;
//                    try {
//                        if (options[jArray.getJSONObject(current_index).getInt("answer") - 1].isChecked()) {
//                            totalScore++;
//                        }
//                        current_index--;
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                    setNextData(current_index);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setNextData(int i) {
        JSONObject currentObject = null;
        try {
            if (i <= max_index) {
                currentObject = jArray.getJSONObject(i);
                questionTextView.setText(currentObject.getString("text"));
                JSONArray currentOptions = currentObject.getJSONArray("options");
                options[0].setText(currentOptions.getString(0));
                options[1].setText(currentOptions.getString(1));
                options[2].setText(currentOptions.getString(2));
                options[3].setText(currentOptions.getString(3));
                options[0].setChecked(false);
                options[1].setChecked(false);
                options[2].setChecked(false);
                options[3].setChecked(false);
            } else {
                startResultActivity();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void startResultActivity() {
        Intent myIntent = new Intent(QuestionActivity.this, ResultActivity.class);
        myIntent.putExtra("totalScore", totalScore);
        QuestionActivity.this.startActivity(myIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem item = menu.findItem(R.id.logout);
        item.setVisible(true);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.logout) {
            signOut();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateUI(Object o) {
        Intent myIntent = new Intent(QuestionActivity.this, LoginActivity.class);
        QuestionActivity.this.startActivity(myIntent);
        finish();
    }
}
