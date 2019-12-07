package com.example.unmadesai.kardiacare4;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import static com.example.unmadesai.kardiacare4.GetNearbyPlacesData.lati;
import static com.example.unmadesai.kardiacare4.GetNearbyPlacesData.longi;

/**
 * Created by Hitesh on 12-03-2018.
 */

public class SmsReceiver extends BroadcastReceiver {
    public static final String SMS_BUNDLE = "pdus";
    String smsBody;
    String address;
    String Message="http://maps.google.com/?q="+lati+","+longi;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle intentExtras = intent.getExtras();
        if (intentExtras != null) {
            Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
            String smsMessageStr = "";
            for (int i = 0; i < sms.length; ++i) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);

                 smsBody = smsMessage.getMessageBody().toString();
                 address = smsMessage.getOriginatingAddress();

                smsMessageStr += "SMS From: " + address + "\n";
                smsMessageStr += smsBody + "\n";
                //Toast.makeText(context,smsMessageStr,Toast.LENGTH_LONG).show();
            }
            Toast.makeText(context,smsMessageStr,Toast.LENGTH_LONG).show();
        }
        char sms=smsBody.charAt(0);
        if(sms=='1'){
            SmsManager smsManager=SmsManager.getDefault();
            smsManager.sendTextMessage(address,null,Message,null,null);
        }
        if(sms=='0'){
            Log.d("in sms Reciever","sms=0");
            //FetchData f=new FetchData();
            //f.onPostExecute("null");
            GetNearbyPlacesData gb=new GetNearbyPlacesData();
            //gb.Callthismethod();
            //Toast.makeText(this,"will try to contact other hospital",Toast.LENGTH_SHORT).show();
            gb.deletekey();
        }
    }
}