package com.example.unmadesai.kardiacare4;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    Button Login;
    EditText USERNAME,USERPASS;
    String username,userpass;
    Context ctx=this;
    Button signupbtn;
   public static boolean loginstatus=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Login = (Button) findViewById(R.id.loginbtn);
        USERNAME=(EditText) findViewById(R.id.email);
        USERPASS=(EditText) findViewById(R.id.pass);
        signupbtn=(Button) findViewById(R.id.signup2);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Toast.makeText(getBaseContext(),"Please wait...", Toast.LENGTH_LONG).show();
                    username=USERNAME.getText().toString();
                    userpass=USERPASS.getText().toString();
                    Log.d("name in login",""+username);
                    Log.d("pass in login",""+userpass);
                    DataBaseOperations DOP=new DataBaseOperations(ctx);
                    Cursor CR=DOP.getInformation(DOP);
                    CR.moveToFirst();
                    //CR.moveToNext();

                    String Name="";
                    do{
                        if(username.equals(CR.getString(0))&& (userpass.equals(CR.getString(1)))){
                            loginstatus=true;
                            Name=CR.getString(0);
                            Log.d("reached here","will login now");

                        }

                    }while(CR.moveToNext());
                    if(loginstatus){
                        Toast.makeText(getBaseContext(),"LOGIN SUCCESSFUL----\n Welcome"+Name, Toast.LENGTH_LONG).show();
                        Log.d("in loginstatus","login successful");
                        Intent i=new Intent(Login.this,Emergency.class);
                        startActivity(i);

                    }
                    else
                        Toast.makeText(getBaseContext(),"LOGIN FAILED", Toast.LENGTH_LONG).show();
                    //finish();

            }
        });
        /*signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Login.this,RegisterActivity.class);
                startActivity(i);
            }
        });*/

    }



}
