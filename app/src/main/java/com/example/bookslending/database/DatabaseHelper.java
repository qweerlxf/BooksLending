package com.example.bookslending.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private final String FOREIGN_KEYS = "PRAGMA foreign_keys = ON;";

//    private final String CREATE_TABLE_ADMIN =
//            "create table admin(aId integer primary key, " +
//                    "aName varchar(255) not null, " +
//                    "aPicture varchar(255), " +
//                    "aPass varchar(255) not null)";

    private final String CREATE_TABLE_USERS =
            "create table users(uId integer primary key, " +
                    "uPass varchar(255) not null, " +
//                    "uPicture varchar(255), " +
                    "uName varchar(255))";

    private final String CREATE_TABLE_BOOKS =
            "create table books(bId integer primary key, " +
                    "bName varchar(255) not null, " +
                    "bPicture varchar(255), " +
                    "bWriter varchar(255), " +
                    "bType varchar(255), " +
                    "bTime text)";

    private final String CREATE_TABLE_BORROWS =
            "create table borrows(uId integer, " +
                    "bId integer, " +
                    "bTime text, " +
                    "rTime text, " +
                    "uEvaluation varchar(255), " +
                    "primary key(uId, bId), " +
                    "foreign key(uId) references users(uId), " +
                    "foreign key(bId) references books(bId))";

    private final String DROP_TABLE_ADMIN = "drop table if exists admin";
    private final String DROP_TABLE_USERS = "drop table if exists users";
    private final String DROP_TABLE_BOOKS = "drop table if exists books";
    private final String DROP_TABLE_BORROWS = "drop table if exists borrows";

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FOREIGN_KEYS);

//        db.execSQL(CREATE_TABLE_ADMIN);
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_BOOKS);
        db.execSQL(CREATE_TABLE_BORROWS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL(DROP_TABLE_ADMIN);
            db.execSQL(DROP_TABLE_USERS);
            db.execSQL(DROP_TABLE_BOOKS);
            db.execSQL(DROP_TABLE_BORROWS);

            onCreate(db);
        }
    }
}
