package com.example.loodos.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.loodos.R;
import com.example.loodos.utils.CommonUtils;

import org.intellij.lang.annotations.RegExp;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kotlin.text.Regex;

public class QuestionActivity extends AppCompatActivity {

    private LinearLayout llQuestionActivity;
    private TextView txtResult;
    private EditText edtDatavalue, edtKeyValue;
    private String data, key;
    private int COUNT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        init();


    }

    private void init() {

        llQuestionActivity = findViewById(R.id.ll_questionActivity);
        edtDatavalue = findViewById(R.id.edt_dataValue);
        edtKeyValue = findViewById(R.id.edt_keyValue);
        txtResult = findViewById(R.id.txt_result);

    }

    public void showResult(View v) {

        data = edtDatavalue.getText().toString().trim();
        key = edtKeyValue.getText().toString().trim();

        if (TextUtils.isEmpty(data) || TextUtils.isEmpty(key)) {

            if (TextUtils.isEmpty(data)) {
                edtDatavalue.setError(getString(R.string.error_value));
            }
            if (TextUtils.isEmpty(key)) {
                edtKeyValue.setError(getString(R.string.error_value));
            }

        } else {
            algorithm(data, key);
            CommonUtils.hideSoftKeyboard(QuestionActivity.this);
        }
    }

    @SuppressLint("NewApi")
    private void algorithm(String data, String key) {


        Matcher m = Pattern.compile("(.+)\\1{" + (Integer.valueOf(key) - 1) + ",}").matcher(data);
        while (m.find()) {
            data = data.replace(m.group(), m.group().replaceAll("(.)", "*"));
        }


        llQuestionActivity.setVisibility(View.VISIBLE);
        txtResult.setText(data);
        edtDatavalue.setText("");
        edtKeyValue.setText("");

    }

}
