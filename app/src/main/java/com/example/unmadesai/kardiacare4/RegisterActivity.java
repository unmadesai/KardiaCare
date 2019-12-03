package com.example.unmadesai.kardiacare4;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    EditText USER_NAME,USER_PASS,CON_PASS,USER_EMAIL;
    String user_name,user_email,user_pass,con_pass;
    Button REG;
    AlertDialog.Builder builder;
    Context ctx=this;
    //int flag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        USER_NAME=(EditText) findViewById(R.id.reg_name);
        USER_PASS=(EditText) findViewById(R.id.reg_password);
       // USER_EMAIL=(EditText)findViewById(R.id.reg_email);
        CON_PASS=(EditText) findViewById(R.id.reg_con_password);
        REG=(Button) findViewById(R.id.reg_btn);

        REG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    user_name=USER_NAME.getText().toString();
                    user_pass=USER_PASS.getText().toString();
                    //user_email=USER_EMAIL.getText().toString();
                    con_pass=CON_PASS.getText().toString();
                Log.d("username",""+user_name);
                Log.d("userpass",""+user_pass);

                if(user_name.equals("")|| user_pass.equals("")|| con_pass.equals("")){

                        builder=new AlertDialog.Builder(RegisterActivity.this);
                        builder.setTitle("Something Went Wrong....");
                        builder.setMessage("Please fill aLL the details");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alertDialog=builder.create();
                        alertDialog.show();
                    }
                    else if(!(user_pass.equals(con_pass))){
                                Toast.makeText(getBaseContext(),"Passswords are not matching", Toast.LENGTH_LONG).show();
                                     //   USER_NAME.setText("");
                                USER_PASS.setText("");
                                CON_PASS.setText("");

                    }
                    else{
                        DataBaseOperations DB=new DataBaseOperations(ctx);
                        Log.d("reached here first","in register after calling db operations");
                        DB.putInformation(DB,user_name,user_pass);

                        Toast.makeText(getBaseContext(),"Regiter Successful", Toast.LENGTH_LONG).show();
                         Intent i=new Intent(RegisterActivity.this,Login.class);
                         startActivity(i);

                    }
            }
        });
    }
}
