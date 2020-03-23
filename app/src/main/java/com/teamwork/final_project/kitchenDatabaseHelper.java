package com.teamwork.final_project;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by jgma on 4/3/2017.
 * This class creates the database to store the kitchen devices.
 */

public class kitchenDatabaseHelper extends SQLiteOpenHelper {

    private static String DATABASE_NAME="kitchen.db";
    private static int VERSION_NUM=1;
    public static String TableName="kitchen_device";
    public static String KEY_ID="_id";
    public static String KEY_TYPE="type";
    public static String KEY_NAME="name";
    public static String KEY_SETTING="settings";

    public kitchenDatabaseHelper(Context ctx){
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }
    //create the database
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TableName + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                                                     + KEY_TYPE + " STRING,"
                                                     + KEY_NAME + " STRING,"
                                                     + KEY_SETTING + " STRING );");

        Log.i("kitchenDatabaseHelper", "Calling onCreate");
    }
    //If the version number of the database increased, the method will be called.
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TableName);
        onCreate(db);

        Log.i("kitchenDatabaseHelper", "Calling onUpgrade, oldVersion= " + oldVersion + " newVersion= " + newVersion);
    }

}
