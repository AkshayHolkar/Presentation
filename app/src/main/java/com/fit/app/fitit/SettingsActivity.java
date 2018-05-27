package com.fit.app.fitit;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SettingsActivity extends AppCompatActivity {

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
    }
}
