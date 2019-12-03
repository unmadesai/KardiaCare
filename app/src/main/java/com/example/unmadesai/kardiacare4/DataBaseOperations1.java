package com.example.unmadesai.kardiacare4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DataBaseOperations1 extends SQLiteOpenHelper {
    public static final int database_version=6;
    //SQLiteDatabase db=new SQLiteDatabase(this);
    public String CREATE_QUERY="CREATE TABLE "+ TableData1.TableInfo.TABLE_NAME+"( " + TableData1.TableInfo.HEART_RATE + " INTEGER, " + TableData1.TableInfo.SPO2+ " INTEGER, " + TableData1.TableInfo.VALID_HR + " INTEGER," + TableData1.TableInfo.VALID_SPO2 + " INTEGER);";
    public String SELECT_QUERY="SELECT AVG("+ TableData1.TableInfo.HEART_RATE +") FROM "+ TableData1.TableInfo.TABLE_NAME;
    public String DROP="DROP TABLE IF EXISTS " +TableData1.TableInfo.TABLE_NAME;
    public DataBaseOperations1(Context context) {
        super(context, TableData1.TableInfo.DATABASE_NAME,null,database_version);
        Log.d("Database Operations","Database created");
    }

    @Override
    public void onCreate(SQLiteDatabase sdb) {
        Log.d("in on create","will create the db");
        sdb.execSQL(CREATE_QUERY);
        Log.d("selected query","Before it");
        //sdb.execSQL(SELECT_QUERY);
        Log.d("selected query","It has run");
        Log.d("Database Operations","Table Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP);
        onCreate(db);

    }
    public void putInformation(DataBaseOperations1 dop,String heartrate,String spo2,String valid_hr,String valid_spo2){
        SQLiteDatabase SQ=dop.getWritableDatabase();
        //ContentValues cv=new ContentValues();
        ContentValues initialValues = new ContentValues();
        initialValues.put(TableData1.TableInfo.HEART_RATE, heartrate);
        initialValues.put(TableData1.TableInfo.SPO2, spo2);
        initialValues.put(TableData1.TableInfo.VALID_HR, valid_hr);
        initialValues.put(TableData1.TableInfo.VALID_SPO2, valid_spo2);
        long k=SQ.insert(TableData1.TableInfo.TABLE_NAME,null,initialValues);
        Log.d("Database Operations","One Row inserted");
        if(k==-1){
            Log.d("in insert","something went wrong");
        }

    }
    /*public Cursor getAllContacts()
    {
        return db.query(TableData.TableInfo.TABLE_NAME, new String[] {TableData.TableInfo.HEART_RATE, TableData.TableInfo.SPO2, TableData.TableInfo.VALID_HR, TableData.TableInfo.VALID_SPO2},
                null, null, null, null, null);
    }*/
    public Cursor select(){
        //DataBaseOperations dbo=new DataBaseOperations(ctx);
        // Log.d("in select","entered");
        SQLiteDatabase SQ=getWritableDatabase();
        // Log.d("in select","got the object");
        String SELECT_QUERY="SELECT AVG("+ TableData1.TableInfo.HEART_RATE +") FROM "+ TableData1.TableInfo.TABLE_NAME;
        //  Log.d("in select","before cursor");
        Cursor crs=SQ.rawQuery(SELECT_QUERY, null);

        Log.d("sql value",SQ.toString());
        Log.d("in select","executed the query");
        return crs;
    }
    public boolean delete(){
        SQLiteDatabase db=getWritableDatabase();
        db.execSQL("delete from " + TableData1.TableInfo.TABLE_NAME);

        Log.d("in delete","deleted the table");
        return true;

    }
    public Cursor getInformation(DataBaseOperations1 dop){
        SQLiteDatabase SQ=dop.getReadableDatabase();
        String[] columns={TableData1.TableInfo.HEART_RATE,TableData1.TableInfo.SPO2,TableData1.TableInfo.VALID_HR,TableData1.TableInfo.VALID_SPO2};
        Cursor CR=SQ.query(TableData1.TableInfo.TABLE_NAME,columns,null,null,null,null,null);
        return CR;

    }
}
