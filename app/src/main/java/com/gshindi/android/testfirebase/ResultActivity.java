package com.gshindi.android.testfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.gshindi.android.testfirebase.util.JsonFileParseUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.Map;

public class ResultActivity extends BaseActivity {

    JsonFileParseUtil jsonFileParseUtil_ = JsonFileParseUtil.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView textView = (TextView) findViewById(R.id.total_Score);
        String answerSheetName = getIntent().getStringExtra("answerSheetName");
        int score = evaluateScore(answers_, answerSheetName);
        String scoreTestViewString = "Total Score : " + score;
        textView.setText(scoreTestViewString);

    }

    @Override
    public void onBackPressed()
    {
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            signOut();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateUI(Object o) {
        Intent myIntent = new Intent(ResultActivity.this, LoginActivity.class);
        ResultActivity.this.startActivity(myIntent);
        finish();
    }

    private int evaluateScore(Map<Integer, Integer> answerSheet, String answerSheetName) {
        int totalScore = 0;
        int resourceId = this.getResources().getIdentifier(answerSheetName, "raw", this.getPackageName());
        InputStream inputStream = getResources().openRawResource(resourceId);
        JSONObject jObject = jsonFileParseUtil_.getJsonObjectForFile(inputStream);
        JSONArray jsonArray = null;
        try {
            jsonArray = jObject.getJSONArray("answers");
            for(Map.Entry<Integer, Integer> entry : answerSheet.entrySet()){
                if(entry.getValue() == jsonArray.getInt(entry.getKey()) - 1 ){
                    totalScore++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalScore;
    }
}
