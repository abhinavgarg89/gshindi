package com.gshindi.android.testfirebase;

import android.content.Intent;
import android.graphics.Color;
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
import android.widget.Toast;

import com.gshindi.android.testfirebase.util.JsonFileParseUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

public class QuestionActivity extends BaseActivity {

    private int current_index = 0;
    private int max_index = 0;
    private JSONArray jArray;
    TextView questionTextView, secondsRemaining, questionNumberTextView, yourAnswerView;
    RadioButton[] options = new RadioButton[4];
    Button nextButton, previousButton, commitTestButton;
    private int answer;
    String questionSetName = "questionSetName";
    CountDownTimer countDownTimer_;
    boolean isReview = false;
    RadioGroup radioGroup;

    JsonFileParseUtil jsonFileParseUtil_ = JsonFileParseUtil.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        current_index = 0;
        questionTextView = (TextView) findViewById(R.id.quesion_text);
        secondsRemaining = (TextView) findViewById(R.id.seconds_remaining);
        yourAnswerView = (TextView) findViewById(R.id.your_answer);
        questionNumberTextView = (TextView) findViewById(R.id.quesion_number);
        options[0] = (RadioButton) findViewById(R.id.option_1);
        options[1] = (RadioButton) findViewById(R.id.option_2);
        options[2] = (RadioButton) findViewById(R.id.option_3);
        options[3] = (RadioButton) findViewById(R.id.option_4);
        nextButton = (Button) findViewById(R.id.next_button);
        radioGroup = (RadioGroup) findViewById(R.id.options_radio_group);
        previousButton = (Button) findViewById(R.id.previous_button);
        commitTestButton = (Button) findViewById(R.id.commit_test);

        isReview = getIntent().getBooleanExtra("review", false);
        if (!isReview) {
            countDownTimer_ = new CountDownTimer(2100000, 1000) {
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
        } else {
            commitTestButton.setText("Home");
        }

        questionSetName = getIntent().getStringExtra("selectedQuestionPaper").toLowerCase();
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
                    radioGroup.clearCheck();
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
                    radioGroup.clearCheck();
                    if (current_index == 0) {
                        Toast.makeText(QuestionActivity.this, "You are at first question",
                                Toast.LENGTH_LONG).show();
                    } else {
                        current_index--;
                        setNextData(current_index);
                    }
                }
            });
            commitTestButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.d("PreviousButton", "previousButton tapped");
                    if (isReview) {
                        startHomeActivity();
                    } else {
                        startResultActivity();
                    }
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
                options[0].setText(currentOptions.getString(0));
                options[1].setText(currentOptions.getString(1));
                options[2].setText(currentOptions.getString(2));
                options[3].setText(currentOptions.getString(3));
                if (!isReview && answers_.get(i) != null) {
                    options[answers_.get(i)].setChecked(true);
                }
                String questionNumber = String.valueOf(i + 1);
                String questionNumberViewText = "Question Number : " + questionNumber;
                questionNumberTextView.setText(questionNumberViewText);
                if (isReview) {
                    radioGroup.check(radioGroup.getChildAt(answer - 1).getId());
                    String yourAnswer = "Your Answer : ";
                    String suffix = "Skipped";
                    if (answers_.get(i) != null) {
                        suffix = String.valueOf(answers_.get(i) + 1);
                    }
                    yourAnswerView.setText(yourAnswer + suffix);
                    if (suffix.equals("Skipped") || answers_.get(i) != (answer - 1)) {
                        yourAnswerView.setTextColor(Color.RED);
                    } else {
                        yourAnswerView.setTextColor(Color.GREEN);
                    }
                } else {
                    yourAnswerView.setVisibility(View.GONE);
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

    private void startHomeActivity() {
        Intent myIntent = new Intent(QuestionActivity.this, QuestionSetListActivity.class);
        myIntent.putExtra("answerSheetName", answerSheetName_);
        myIntent.putExtra("questionSheetName", questionSetName);
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
        Intent myIntent = new Intent(QuestionActivity.this, WelcomeActivity.class);
        QuestionActivity.this.startActivity(myIntent);
        finish();
    }
}
