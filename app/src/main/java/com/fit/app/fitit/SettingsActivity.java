package com.fit.app.fitit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity {
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private EditText stepGoals, calorieGoals;
    private Button saveBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button sendFeedbackBtn = (Button)findViewById(R.id.feedbackBtn);
        sendFeedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String feedback = "https://docs.google.com/forms/d/1JjwLgjuVKeQAIOecIykF3C3F1EwuzxMM9OCVAc0nnTI/edit#responses";
                Uri feedbackWebAddress = Uri.parse(feedback);

                Intent goToFeedbackForm = new Intent(Intent.ACTION_VIEW, feedbackWebAddress);
                if(goToFeedbackForm.resolveActivity(getPackageManager()) != null){
                    startActivity(goToFeedbackForm);
                }
            }
        });

        Button reportBtn = (Button)findViewById(R.id.reportBtn);
        reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String report = "https://goo.gl/forms/3VQzjW29eaKULKf83";
                Uri reportWebAddress = Uri.parse(report);

                Intent goToReportForm = new Intent(Intent.ACTION_VIEW, reportWebAddress);
                if(goToReportForm.resolveActivity(getPackageManager()) != null){
                    startActivity(goToReportForm);
                }
            }
        });

        saveBtn = (Button)findViewById(R.id.saveBtn);
        stepGoals = (EditText) findViewById(R.id.stepGoalsTextEdit);
        calorieGoals = (EditText) findViewById(R.id.calorieTextEdit);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //mPreferences = getSharedPreferences("myDataBase", Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();

        checkSharedPreferences();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String steps = stepGoals.getText().toString();
                mEditor.putString(getString(R.string.steps), steps);
                mEditor.commit();

                String calories = calorieGoals.getText().toString();
                mEditor.putString(getString(R.string.calories), calories);
                mEditor.commit();
            }
        });

    }

    private void checkSharedPreferences(){
        String stepsSaved = mPreferences.getString(getString(R.string.steps),"");
        String caloriesSaved = mPreferences.getString(getString(R.string.calories),"");
    }
}
