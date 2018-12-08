package com.example.carstenvos.ziekeapp;

import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ziekeapp.db";
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_FIRSTNAME = "firstname";
    public static final String COLUMN_LASTNAME = "lastname";

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String query = "CREATE TABLE IF NOT EXISTS " + TABLE_USERS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EMAIL + " TEXT " + " NOT NULL UNIQUE," +
                COLUMN_PASSWORD + " TEXT " + " NOT NULL," +
                COLUMN_FIRSTNAME + " TEXT ," +
                COLUMN_LASTNAME + " TEXT" +
                ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    //Add user
    public boolean addUser(Users user){
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_PASSWORD, user.getPassword());
        values.put(COLUMN_FIRSTNAME, user.getFirstName());
        values.put(COLUMN_LASTNAME,user.getLastName());
        SQLiteDatabase db = getWritableDatabase();
        Boolean success = false;

        try {
            db.insertOrThrow(TABLE_USERS, null, values);
            db.close();
            success = true;
        }
        catch (SQLiteConstraintException e) {
            Log.e("Add User","Email exists");
            success = false;
        }

        return success;
    }

    //Return users as lists
    public ArrayList<Users> getUsersAsList(){
        ArrayList<Users> userList = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();

        //String query = "SELECT * FROM " + TABLE_USERS;


        Cursor cursor = db.query(
                TABLE_USERS,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        while(cursor.moveToNext()) {
            Users user = new Users(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4));
            userList.add(user);
        }
        cursor.close();
        db.close();
        return userList;
    }

    //Get user for login validation
    public Users getLoginUser(String emailLogin) {
        Users loginUser;
        SQLiteDatabase db = getWritableDatabase();

        String[] whereArguments = { emailLogin };

        Cursor cursor = db.query(TABLE_USERS,null,COLUMN_EMAIL + " = ?",whereArguments,null,null,null);
        cursor.moveToFirst();

        loginUser = new Users(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4));

        cursor.close();

        return loginUser;
    }
}
