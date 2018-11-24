package com.example.skett.abndp9inventoryapp2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class InventoryDBHelper extends SQLiteOpenHelper {

    //class name for log message
    public static final String TAG = InventoryDBHelper.class.getSimpleName();
    //You must increment the database version if you change the DB schema.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Inventory.db";

    public InventoryDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase invDB) {
        String SQL_CREATE_KICKS_TABLE = "CREATE TABLE " + InventoryContract.KicksEntry.TABLE_NAME + " (" + InventoryContract.KicksEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + InventoryContract.KicksEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, " + InventoryContract.KicksEntry.COLUMN_PRICE + " INTEGER NOT NULL, " + InventoryContract.KicksEntry.COLUMN_QUANTITY + " INTEGER NOT NULL DEFAULT 0, " + InventoryContract.KicksEntry.COLUMN_SUPPLIER_NAME + " INTEGER NOT NULL, " + InventoryContract.KicksEntry.COLUMN_SUPPLIER_PHONE + " TEXT NOT NULL);";
        invDB.execSQL(SQL_CREATE_KICKS_TABLE);

        Log.d(TAG, "onCreate: SQLite DB successfully created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase invDB, int oldVersion, int newVersion) {
        invDB.execSQL("DROP TABLE IF EXISTS " + InventoryContract.KicksEntry.TABLE_NAME);
    }
}

