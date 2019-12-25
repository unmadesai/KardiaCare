package com.example.unmadesai.kardiacare4;

import android.provider.BaseColumns;

/**
 * Created by hitesh on 1/31/2018.
 */

public class TableData
{

    public TableData(){

}

public static abstract class TableInfo implements BaseColumns
{
    public static final String USER_NAME="user_name";
    public static final String USER_PASS="user_pass";
    //public static final String USER_EMAIL="user_email";
    public static  final String DATABASE_NAME="register_info";
    public static final String TABLE_NAME="register";
    public static final String TABLE="emergency_contacts";
    public static final String USER_NAME1="contact_user";
    public static final String contact1="contact1name";
    public static final String contact2="contact2name";
}
}
