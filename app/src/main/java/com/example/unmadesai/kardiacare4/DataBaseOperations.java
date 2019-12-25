package com.example.unmadesai.kardiacare4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by hitesh on 1/31/2018.
 */

public class DataBaseOperations extends SQLiteOpenHelper {
    public static final int database_version=10;
    public String CREATE_QUERY="CREATE TABLE "+ TableData.TableInfo.TABLE_NAME+"("+TableData.TableInfo.USER_NAME+" TEXT," +TableData.TableInfo.USER_PASS+" TEXT);";
    public String CREATE_EMERGENCY="CREATE TABLE "+ TableData.TableInfo.TABLE+"("+TableData.TableInfo.contact1+" TEXT," +TableData.TableInfo.contact2+" TEXT);";
    public String DROP="DROP TABLE IF EXISTS " +TableData.TableInfo.TABLE_NAME;
    public String DROP1="DROP TABLE IF EXISTS " +TableData.TableInfo.TABLE;
    public DataBaseOperations(Context context) {
        super(context, TableData.TableInfo.DATABASE_NAME,null,database_version);
        Log.d("Database Operations","Database created");
    }

    @Override
    public void onCreate(SQLiteDatabase sdb) {
        Log.d("in on create","will create the db");
        sdb.execSQL(CREATE_QUERY);

            sdb.execSQL(CREATE_EMERGENCY);
            Log.d("emergency table","table created");

        Log.d("Database Operations","Table Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DROP);
            db.execSQL(DROP1);
            onCreate(db);

    }
    public void putInformation(DataBaseOperations dop, String name, String pass){
        SQLiteDatabase SQ=dop.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(TableData.TableInfo.USER_NAME,name);
        Log.d("reached here","pass will be put after this");
        cv.put(TableData.TableInfo.USER_PASS,pass);
        Log.d("reached here","pass has been put");
        long k=SQ.insert(TableData.TableInfo.TABLE_NAME,null,cv);
        Log.d("Database Operations","One Row inserted");
        if(k==-1){
            Log.d("in insert","something went wrong");
        }

    }
    public void putInfoInEmergency(DataBaseOperations dop, String name1, String name2){
        SQLiteDatabase SQ=dop.getWritableDatabase();
        ContentValues cv=new ContentValues();

        cv.put(TableData.TableInfo.contact1,name1);
        cv.put(TableData.TableInfo.contact2,name2);
        long k=SQ.insert(TableData.TableInfo.TABLE,null,cv);
        Log.d("Database Operations","One Row inserted");
        if(k==-1){
            Log.d("in put ","something went wrong");
        }
    }
    public Cursor getInformation(DataBaseOperations dop){
        SQLiteDatabase SQ=dop.getReadableDatabase();
        String[] columns={TableData.TableInfo.USER_NAME,TableData.TableInfo.USER_PASS};
        Cursor CR=SQ.query(TableData.TableInfo.TABLE_NAME,columns,null,null,null,null,null);

        return CR;
    }
    public Cursor getInformationfromEmergency(DataBaseOperations dop){
        SQLiteDatabase SQ=dop.getReadableDatabase();
        String[] columns={TableData.TableInfo.contact1, TableData.TableInfo.contact2};
        Cursor CR=SQ.query(TableData.TableInfo.TABLE,columns,null,null,null,null,null);
        return  CR;
    }
}
