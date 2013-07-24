package com.rorlig.example;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
//import android.content.SharedPreferences;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import java.util.Arrays;

public class CallReceiver extends BroadcastReceiver {


    private static final String TAG = "CallInterceptor" ;

    public static final String[] interceptNumberArray = {"18009346489",
                                                            "18009220204"};

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

        SharedPreferences pref = context.getSharedPreferences(
                "callinterceptor_preference", Context.MODE_WORLD_READABLE);
//
        String prefPhoneNumber = pref.getString("phoneNumber", null);
        Long expirationTimer = pref.getLong("expirationTime", -1L);
        SharedPreferences.Editor editor = pref.edit();

        Log.d(TAG, "onReceive");

        //outgoing call . prefPhoneNumber ==  null or hasExpired

        //show popup - if prefPhoneNumber ==null //  prefPhoneNumber == phoneNumber and has expired ...
        //

        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            String phoneNumber = intent.getExtras().getString(Intent.EXTRA_PHONE_NUMBER);
            Log.d(TAG, "phoneNumber called :  " + phoneNumber );
            if (Arrays.asList(interceptNumberArray).contains(phoneNumber)
                    && ((prefPhoneNumber==null)||(prefPhoneNumber.equals(phoneNumber)&&isExpired(expirationTimer)) ) ){
                Log.d(TAG, "phoneNumber called in IVR list -- intercept it" );

                Intent i = new Intent();
                i.setClassName("com.rorlig.example", "com.rorlig.example.CallInterceptorActivity");
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("phoneNumber", phoneNumber);
                context.startActivity(i);
                setResultData(null);
                this.abortBroadcast();

            } else {
                Log.d(TAG, "phoneNumber called not in IVR list forward the intent");
            }

		}
        editor.clear();
        editor.commit();
	}

    private boolean isExpired(Long expirationTimer) {
        if (expirationTimer==-1L) return false;

        return expirationTimer < (System.currentTimeMillis() - 60*1000);
    }

}
