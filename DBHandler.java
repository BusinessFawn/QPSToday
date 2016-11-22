package com.littlehouse_design.qpstoday;

/**
 * Created by johnkonderla on 11/21/16.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "QPSToday";
    // Ingredients table name
    private static final String TABLE_ADDRESSES = "addresses";

    private static final String IP_ID = "ipID";
    private static final String IP_ADDRESS = "ipAddress";
    private static final String PORT = "port";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ADDRESS_TABLE = "CREATE TABLE " + TABLE_ADDRESSES + "(" +
                IP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + IP_ADDRESS + " TEXT, " +
                PORT + " INTEGER DEFAULT 0)";
        db.execSQL(CREATE_ADDRESS_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADDRESSES);
        onCreate(db);
    }
    public void dropDB() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE " + TABLE_ADDRESSES);
    }
    public void addIP(String IP, int port) {
        SQLiteDatabase dbWrite = this.getWritableDatabase();
        SQLiteDatabase dbRead = this.getReadableDatabase();

        String countQuery = "SELECT * FROM " + TABLE_ADDRESSES;
        Cursor cursor = dbRead.rawQuery(countQuery, null);
        ContentValues values = new ContentValues();
        values.put(IP_ADDRESS, IP);
        values.put(PORT, port);
        if(cursor.getCount() < 1) {
            dbWrite.insert(TABLE_ADDRESSES, null, values);
        } else {
            dbWrite.update(TABLE_ADDRESSES, values, IP_ID + " = ?",
                    new String[]{"1"});

        }
        cursor.close();
        dbWrite.close();
        dbRead.close();
    }
    public String getIpAddress() {
        SQLiteDatabase dbRead = this.getReadableDatabase();
        String ipAddress;

        Cursor cursor = dbRead.query(TABLE_ADDRESSES, new String[]{IP_ADDRESS}, IP_ID + "=?",
                new String[]{String.valueOf(0)},null,null,null);
        if(cursor != null)
            cursor.moveToFirst();
        if(cursor.getCount() < 1) {
            ipAddress = "Not Set";
        }
        else {
            ipAddress = cursor.getString(1);
        }
        cursor.close();
        dbRead.close();
        return ipAddress;

    }
    public int getPort() {
        SQLiteDatabase dbRead = this.getReadableDatabase();
        int port;

        Cursor cursor = dbRead.query(TABLE_ADDRESSES, new String[]{PORT}, IP_ID + "=?",
                new String[]{String.valueOf(0)},null,null,null);
        if(cursor != null)
            cursor.moveToFirst();
        if(cursor.getCount() < 1) {
            port = -1;
        }
        else {
            port = Integer.parseInt(cursor.getString(2));
        }

        return port;

    }
}
