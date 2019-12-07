package com.example.unmadesai.kardiacare4;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import static com.example.unmadesai.kardiacare4.Emergency.con1;
import static com.example.unmadesai.kardiacare4.Emergency.con2;
import static com.example.unmadesai.kardiacare4.GetNearbyPlacesData.lati;
import static com.example.unmadesai.kardiacare4.GetNearbyPlacesData.longi;
import static com.example.unmadesai.kardiacare4.GetNearbyPlacesData.result;
import static com.example.unmadesai.kardiacare4.MapsActivity.flati;
import static com.example.unmadesai.kardiacare4.MapsActivity.flongi;
import static com.example.unmadesai.kardiacare4.MapsActivity.latitude;
import static com.example.unmadesai.kardiacare4.MapsActivity.longitude;

public class SmsActivity extends AppCompatActivity {
    Button Login,Register,Delete,Update,SMS;
    int status=0;
    int MY_PERMISSION_REQUEST_SMS=1;
    String SENT="SMS_SENT";
    String DELIVERED="SMS_DELIVERED";
    PendingIntent sentPI,deliveredPI;
    BroadcastReceiver smsSentReciever,smsDeliveredReciever;
    EditText pnumber;
    Context ctx=this;
    String HMessage="Emergency reply with 1(yes) or 0(No)";
    String hnmuber="8806498076";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acticity_sms);
        Login=(Button)findViewById(R.id.loginbtn);
        Register=(Button)findViewById(R.id.reg_btn);
        Delete=(Button)findViewById(R.id.deletebtn);
        Update=(Button)findViewById(R.id.updatebtn);
        SMS=(Button) findViewById(R.id.smsbtn);
        pnumber=(EditText) findViewById(R.id.phone);
        sentPI= PendingIntent.getBroadcast(this,0,new Intent(SENT),0);
        deliveredPI= PendingIntent.getBroadcast(this,0,new Intent(DELIVERED),0);
        SMS.performClick();
       /* Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status=1;
                Bundle b=new Bundle();
                b.putInt("status",status);
                Intent i=new Intent("Login ");
                i.putExtras(b);
                startActivity(i);
            }
        });
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status=2;
                Bundle b=new Bundle();
                b.putInt("status",status);
                Intent i=new Intent("Update-Filter");
                i.putExtras(b);
                startActivity(i);
            }
        });
        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status=3;
                Bundle b=new Bundle();
                b.putInt("status",status);
                Intent i=new Intent("Delete-Filter ");
                i.putExtras(b);
                startActivity(i);
            }
        });*/



    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("In on Resume","reached here");
            smsSentReciever=new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    switch (getResultCode()){
                        case Activity.RESULT_OK:
                            Toast.makeText(SmsActivity.this,"Sms Sent", Toast.LENGTH_SHORT).show();
                            Log.d("reached here","result_ok");
                            break;
                        case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                            Toast.makeText(SmsActivity.this,"GENERIC ERROR", Toast.LENGTH_SHORT).show();
                            Log.d("reached here","generic failure");
                            break;
                        case SmsManager.RESULT_ERROR_NO_SERVICE:
                            Toast.makeText(SmsActivity.this,"No service!", Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_NULL_PDU:
                            Toast.makeText(SmsActivity.this,"NULL PDU", Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_RADIO_OFF:
                            Toast.makeText(SmsActivity.this,"Radio off", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            };
            smsDeliveredReciever=new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                        switch (getResultCode()){
                            case Activity.RESULT_OK:
                                Toast.makeText(SmsActivity.this,"Sms delivered", Toast.LENGTH_SHORT).show();
                                break;
                            case Activity.RESULT_CANCELED:
                                Toast.makeText(SmsActivity.this,"sms not deliered", Toast.LENGTH_SHORT).show();
                        }
                }
            };
            registerReceiver(smsSentReciever,new IntentFilter(SENT));
            registerReceiver(smsDeliveredReciever,new IntentFilter(DELIVERED) );
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(smsSentReciever);
        unregisterReceiver(smsDeliveredReciever);
    }

    public void sendSms(View view){
        //LatLng latLng0=;
        //double lat=latLng0.getLatitude();
        //Toast.makeText(getBaseContext(),"Hospital number is"+result,Toast.LENGTH_SHORT).show();
        String Message="Emergency http://maps.google.com/?q="+flati+","+flongi;
        /*DataBaseOperations DOP=new DataBaseOperations(ctx);
        Cursor CR=DOP.getInformationfromEmergency(DOP);
        CR.moveToFirst();
        //CR.moveToNext();

        String Name="";
        do{


            con1=CR.getString(0);
            con2=CR.getString(1);
        }while(CR.moveToNext());
        //Log.d("name",""+Name);
        Log.d("contact1",""+con1);
        Log.d("contact2",""+con2);*/

        //pnumber=(EditText) findViewById(R.id.phone);
        String number=con1;
        Log.d("phonenumber",""+con1);
        Log.d("in sendsms","user clicked");
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},MY_PERMISSION_REQUEST_SMS);
        }

        else{
            Log.d("in else part","permission is granted");
            android.telephony.SmsManager sms=android.telephony.SmsManager.getDefault();
            sms.sendTextMessage(number,null,Message,sentPI,deliveredPI);
            Log.d("in sendsms","sent msg to first");
            sms.sendTextMessage(con2,null,Message,sentPI,deliveredPI);

            sms.sendTextMessage(hnmuber,null,HMessage,sentPI,deliveredPI);

        }

    }



}
