package com.gshindi.android.testfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.gshindi.android.testfirebase.util.JsonFileParseUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ResultActivity extends BaseActivity implements View.OnClickListener {

    JsonFileParseUtil jsonFileParseUtil_ = JsonFileParseUtil.getInstance();
    private String questionSheetName;
    private int correctAnswer, wrongAnswer, totalAttempted;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        correctAnswer = 0;
        totalAttempted = 0;
        wrongAnswer = 0;

        findViewById(R.id.result_home_page_button).setOnClickListener(this);
        findViewById(R.id.result_review_button).setOnClickListener(this);

        TextView totalQuestiontextView = (TextView) findViewById(R.id.total_Question);
        TextView totalAttemptedtextView = (TextView) findViewById(R.id.total_Attempted);
        TextView totalCorrecttextView = (TextView) findViewById(R.id.total_Correct);
        TextView totalWrongtextView = (TextView) findViewById(R.id.total_Wrong);
        TextView textView = (TextView) findViewById(R.id.total_Score);

        String answerSheetName = getIntent().getStringExtra("answerSheetName");
        questionSheetName = getIntent().getStringExtra("questionSheetName");
        double score = evaluateScore(answers_, answerSheetName);
        String scoreTestViewString = "Total Score : " + score;
        String totalQuestion = "Total Question : 30 ";
        String totalAttemptedViewString = "Total Question Attempted : " + answers_.size();
        String correctAnswerString = "Correct Answer : " + correctAnswer;
        String wrongAnswerString = "Wrong Answer : " + wrongAnswer;

        //Storing values to firebase
        String uid = mAuth.getCurrentUser().getUid();
        Firebase childRef = ref.child("UserPerformanceReport").child(uid).child(questionSheetName);
        Map<String, Object> nickname = new HashMap<String, Object>();
        nickname.put("marks", score);
        nickname.put("date", new Date().toString());
        childRef.updateChildren(nickname);
        totalQuestiontextView.setText(totalQuestion);
        totalAttemptedtextView.setText(totalAttemptedViewString);
        totalCorrecttextView.setText(correctAnswerString);
        totalWrongtextView.setText(wrongAnswerString);
        textView.setText(scoreTestViewString);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ResultActivity.this, QuestionSetListActivity.class));
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
        Intent myIntent = new Intent(ResultActivity.this, WelcomeActivity.class);
        ResultActivity.this.startActivity(myIntent);
        finish();
    }

    private double evaluateScore(Map<Integer, Integer> answerSheet, String answerSheetName) {
        int totalScore = 0;
        int resourceId = this.getResources().getIdentifier(answerSheetName, "raw", this.getPackageName());
        InputStream inputStream = getResources().openRawResource(resourceId);
        JSONObject jObject = jsonFileParseUtil_.getJsonObjectForFile(inputStream);
        JSONArray jsonArray = null;
        try {
            jsonArray = jObject.getJSONArray("answers");
            for (Map.Entry<Integer, Integer> entry : answerSheet.entrySet()) {
                if (entry.getValue() == jsonArray.getInt(entry.getKey()) - 1) {
                    totalScore = totalScore + 3;
                    correctAnswer++;
                } else {
                    totalScore--;
                    wrongAnswer++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalScore * 0.66;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.result_home_page_button:
                Intent myIntent = new Intent(ResultActivity.this, QuestionSetListActivity.class);
                ResultActivity.this.startActivity(myIntent);
                finish();
                break;
            case R.id.result_review_button:
                Intent intent = new Intent(ResultActivity.this, QuestionActivity.class);
                intent.putExtra("review", true);
                intent.putExtra("selectedQuestionPaper", questionSheetName);
                ResultActivity.this.startActivity(intent);
                finish();
                break;
        }
    }
}
