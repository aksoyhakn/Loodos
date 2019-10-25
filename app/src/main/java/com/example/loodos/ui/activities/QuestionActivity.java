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

import java.util.Arrays;

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

        String prevValue;
        String[] dataArray = data.split("");

        prevValue = dataArray[0];

        for (int i = 1; i < dataArray.length; i++) {
            if (dataArray[i].equals(prevValue)) {
                COUNT += 1;
            } else {
                COUNT = 1;
                prevValue = dataArray[i];
            }

            if (COUNT >= Integer.valueOf(key)) {
                for (int j = 0; j < COUNT; j++) {
                    dataArray[i - j] = "*";
                }
            }
        }


        llQuestionActivity.setVisibility(View.VISIBLE);
        txtResult.setText(String.join(" ", dataArray));

        edtDatavalue.setText("");
        edtKeyValue.setText("");

    }

}
