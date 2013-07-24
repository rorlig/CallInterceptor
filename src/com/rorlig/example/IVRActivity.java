package com.rorlig.example;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/*
 *  IVRActivity class
 *  Shows the IVR menu for the company..
 */
public class IVRActivity extends Activity {


    TextView textView;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ivr);
        textView = (TextView) findViewById(R.id.textView);
        Intent intent = getIntent();
        if (intent.getExtras()!=null) {
            String phoneNumber = getIntent().getExtras().getString("phoneNumber");
            //todo use a map of phoneNumber / company
            textView.setText("IVR for"  + phoneNumber);
        }   else {
            textView.setText("IVR menu for unspecified number");
        }

    }
}
