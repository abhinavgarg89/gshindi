package com.gshindi.android.testfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gshindi.android.testfirebase.util.JsonFileParseUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

public class QuestionActivity extends BaseActivity {

    private int current_index = 0;
    private int max_index = 0;
    private JSONArray jArray;
    TextView questionTextView, secondsRemaining;
    RadioButton[] options = new RadioButton[4];
    Button nextButton, previousButton;
    private int answer;
    String questionSetName = "questionSetName";
    CountDownTimer countDownTimer_;
    boolean isReview = false;

    JsonFileParseUtil jsonFileParseUtil_ = JsonFileParseUtil.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        current_index = 0;
        questionTextView = (TextView) findViewById(R.id.quesion_text);
        secondsRemaining = (TextView) findViewById(R.id.seconds_remaining);
        options[0] = (RadioButton) findViewById(R.id.option_1);
        options[1] = (RadioButton) findViewById(R.id.option_2);
        options[2] = (RadioButton) findViewById(R.id.option_3);
        options[3] = (RadioButton) findViewById(R.id.option_4);
        nextButton = (Button) findViewById(R.id.next_button);
        previousButton = (Button) findViewById(R.id.previous_button);

        isReview = getIntent().getBooleanExtra("review", false);
        if (!isReview) {
            countDownTimer_ = new CountDownTimer(20000, 1000) {

                public void onTick(long millisUntilFinished) {
                    long minutesRemaining = (millisUntilFinished / 60000);
                    if (minutesRemaining > 0) {
                        secondsRemaining.setText("Minutes Remaining: " + (int) (millisUntilFinished / 60000));
                    } else {
                        secondsRemaining.setText("Seconds Remaining: " + (int) (millisUntilFinished / 1000));
                    }
                }

                public void onFinish() {
                    startResultActivity();
                }
            };
            countDownTimer_.start();
        }

        questionSetName = getIntent().getStringExtra("selectedQuestionPaper");
        try {
            int resourceId = this.getResources().getIdentifier(questionSetName, "raw", this.getPackageName());
            InputStream inputStream = getResources().openRawResource(resourceId);
            JSONObject jObject = jsonFileParseUtil_.getJsonObjectForFile(inputStream);
            answerSheetName_ = jObject.getString("answerSheetName");
            jArray = jObject.getJSONArray("questions");
            max_index = jArray.length() - 1;
            setNextData(current_index);

            nextButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.d("NextButton", "nextButton tapped");
                    if (!isReview) {
                        for (int index = 0; index < 4; index++) {
                            if (options[index].isChecked()) {
                                answers_.put(current_index, index);
                            }
                        }
                    }
                    current_index++;
                    setNextData(current_index);
                }
            });
            previousButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.d("PreviousButton", "previousButton tapped");
                    if (!isReview) {
                        for (int index = 0; index < 4; index++) {
                            if (options[index].isChecked()) {
                                answers_.put(current_index, index);
                            }
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
//                    RadioGroup radioGroup = (RadioGroup)findViewById(R.id.options_radio_group);
//                    radioGroup.clearCheck();
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
                answer = currentObject.getInt("answer");
                if (!isReview) {
                    options[0].setChecked(false);
                    options[1].setChecked(false);
                    options[2].setChecked(false);
                    options[3].setChecked(false);
                }
//                    options[0].setChecked(false);
//                    options[1].setChecked(false);
//                    options[2].setChecked(false);
//                    options[3].setChecked(false);
//                }
                options[0].setText(currentOptions.getString(0));
                options[1].setText(currentOptions.getString(1));
                options[2].setText(currentOptions.getString(2));
                options[3].setText(currentOptions.getString(3));
//                options[0].setChecked(false);
//                options[1].setChecked(false);
//                options[2].setChecked(false);
//                options[3].setChecked(false);
                RadioGroup radioGroup = (RadioGroup)findViewById(R.id.options_radio_group);

                if (isReview) {
                    radioGroup.check(radioGroup.getChildAt(answer - 1).getId());
//                    options[answer - 1].setChecked(true);
                }
            } else {
                if (!isReview) {
                    startResultActivity();
                } else {
                    Intent myIntent = new Intent(QuestionActivity.this, QuestionSetListActivity.class);
                    QuestionActivity.this.startActivity(myIntent);
                    finish();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void startResultActivity() {
        Intent myIntent = new Intent(QuestionActivity.this, ResultActivity.class);
        myIntent.putExtra("answerSheetName", answerSheetName_);
        myIntent.putExtra("questionSheetName", questionSetName);
        countDownTimer_.cancel();
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
