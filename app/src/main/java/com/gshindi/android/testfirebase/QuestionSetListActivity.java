package com.gshindi.android.testfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gshindi.android.testfirebase.util.JsonFileParseUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

public class QuestionSetListActivity extends BaseActivity {

    private JSONArray jArray;
    private ListView listView;

    JsonFileParseUtil jsonFileParseUtil_ = JsonFileParseUtil.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_set_list);

        InputStream inputStream = getResources().openRawResource(R.raw.question_set_list);
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

//        int ctr;
//        try {
//            ctr = inputStream.read();
//            while (ctr != -1) {
//                byteArrayOutputStream.write(ctr);
//                ctr = inputStream.read();
//            }
//            inputStream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Log.v("Text Data", byteArrayOutputStream.toString());
//
        ArrayList<String> questionSetList = new ArrayList<>();
        try {
            JSONObject jObject = jsonFileParseUtil_.getJsonObjectForFile(inputStream);
            jArray = jObject.getJSONArray("quesionSets");
            for(int index = 0; index < jArray.length(); index++){
                questionSetList.add(jArray.getString(index));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        QuestionSetArrayAdapter questionSetArrayAdapter = new QuestionSetArrayAdapter(this, questionSetList.toArray(new String[questionSetList.size()]));
        listView = (ListView) findViewById(R.id.question_set_list);
        listView.setAdapter(questionSetArrayAdapter);

        AdapterView.OnItemClickListener adapterView = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedValue = (String) listView.getAdapter().getItem(position);
                Intent myIntent = new Intent(QuestionSetListActivity.this, QuestionActivity.class);
                myIntent.putExtra("selectedQuestionPaper", selectedValue);
                QuestionSetListActivity.this.startActivity(myIntent);
                finish();
            }
        };
        listView.setOnItemClickListener(adapterView);
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
        Intent myIntent = new Intent(QuestionSetListActivity.this, LoginActivity.class);
        QuestionSetListActivity.this.startActivity(myIntent);
        finish();
    }
}
