package com.gshindi.android.testfirebase.util;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by abhinavgarg on 16/07/16.
 */
public class JsonFileParseUtil {

    private static JsonFileParseUtil instance_ = null;
    private static final Object mutex_ = new Object();

    private JsonFileParseUtil() {
    }

    public static JsonFileParseUtil getInstance() {
        if (instance_ == null) {
            synchronized (mutex_) {
                if (instance_ == null) {
                    instance_ = new JsonFileParseUtil();
                }
            }
        }
        return instance_;
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }

    public JSONObject getJsonObjectForFile(InputStream inputStream) {
        int ctr;
        JSONObject jObject = null;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ctr = inputStream.read();
            while (ctr != -1) {
                byteArrayOutputStream.write(ctr);
                ctr = inputStream.read();
            }
            inputStream.close();

            Log.v("Text Data", byteArrayOutputStream.toString());
            jObject = new JSONObject(byteArrayOutputStream.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jObject;
    }
}
