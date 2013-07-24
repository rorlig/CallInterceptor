package com.rorlig.example;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/*
 *  CallInterceptorActivity class - called by the broadcast receiver for blocked numbers.
 *  Uses android:theme="@android:style/Theme.Dialog" for dialog styling...
 */
public class CallInterceptorActivity extends Activity {

    SharedPreferences pref;
    private final static String TAG = "CallInterceptorActivity";
    Button launchIVR;
    Button makeCall;
    String phoneNumber;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interceptor);

        pref = getApplicationContext().getSharedPreferences(
                "callinterceptor_preference", MODE_WORLD_READABLE);

        Intent intent = getIntent();
        if (intent.getExtras()!=null) {
             phoneNumber = getIntent().getExtras().getString("phoneNumber");
        }

        Log.d(TAG, "CallInterceptor called for phoneNumber : " + phoneNumber);

        launchIVR = (Button) findViewById(R.id.button_ivr);
        makeCall = (Button) findViewById(R.id.button_call);

        launchIVR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "launchIVR button clicked");

                Intent i = new Intent();
                i.setClassName("com.rorlig.example", "com.rorlig.example.IVRActivity");
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("phoneNumber", phoneNumber);
                startActivity(i);
                finish();
            }
        });


        makeCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "makeCall button clicked");
                Log.d(TAG, "calling " + phoneNumber);
                /* write this number to shared preference */
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("phoneNumber", phoneNumber);
                editor.putLong("expirationTime", System.currentTimeMillis());
                editor.commit();
                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                startActivity(callIntent);
                finish();
            }
        });
    }
}