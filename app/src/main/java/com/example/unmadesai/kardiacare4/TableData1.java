package com.example.unmadesai.kardiacare4;

import android.provider.BaseColumns;

/**
 * Created by hitesh on 1/31/2018.
 */

public class TableData1
{

    public TableData1(){

    }

    public static abstract class TableInfo implements BaseColumns
    {
        public static final String HEART_RATE="heartrate";
        public static final String VALID_HR="validhr";

        public static  final String DATABASE_NAME="sensor_data";
        public static final String TABLE_NAME="sensors";

        public static final String SPO2="spo2";
        public static final String VALID_SPO2="validspo2";
    }
}
