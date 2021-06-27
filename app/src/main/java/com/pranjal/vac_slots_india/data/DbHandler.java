package com.pranjal.vac_slots_india.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.pranjal.vac_slots_india.params.Params;

public class DbHandler extends SQLiteOpenHelper
{
    public DbHandler(Context context) {
        super(context, Params.DB_NAME, null, Params.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create = "CREATE TABLE " + Params.TABLE_NAME + " (" +
                Params.KEY_ID + " INTEGER PRIMARY KEY," +
                Params.KEY_SESSIONID + " TEXT," +
                Params.KEY_SESSION_NAME + " TEXT," +
                Params.KEY_NOTIFIED + " TEXT)";
        db.execSQL(create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String delete = "DROP TABLE IF EXISTS " + Params.TABLE_NAME;
        db.execSQL(delete);
        onCreate(db);
    }
}
