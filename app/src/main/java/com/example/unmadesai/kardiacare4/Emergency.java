package com.example.unmadesai.kardiacare4;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Hitesh on 06-03-2018.
 */

public class Emergency extends Activity {

    Button b1;
    EditText contact1;
    EditText contact2,username;
    String name1,name2,name;
    Context ctx=this;
    public static String con1="";
    public static String con2="";
    int count=0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emergency);
        b1=(Button)findViewById(R.id.start_btn);
        contact1=(EditText)findViewById(R.id.editname1);
        contact2=(EditText)findViewById(R.id.editname2);
        //username= (EditText) findViewById(R.id.edituser);
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   // name=username.getText().toString();
                    name1=contact1.getText().toString();
                    name2=contact2.getText().toString();

                    if(name1==""||name2==""){
                        Toast.makeText(getBaseContext(),"please provide a number", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        DataBaseOperations db=new DataBaseOperations(ctx);
                        db.putInfoInEmergency(db,name1,name2);
                        count=1;

                        Log.d("inemergency",""+count);
                    }
                    if(count==1) {
                        DataBaseOperations DOP=new DataBaseOperations(ctx);
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
                        Log.d("contact2",""+con2);
                        Intent i=new Intent(Emergency.this,MapsActivity.class);
                        startActivity(i);


                    }

                }
            });
            /*Intent i=new Intent(Emergency.this,SmsActivity.class);
            startActivity(i);*/
           }


}
