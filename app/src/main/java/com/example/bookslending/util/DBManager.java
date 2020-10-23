package com.example.bookslending.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.bookslending.database.DatabaseHelper;
import com.example.bookslending.po.UsersInfo;

import java.util.ArrayList;
import java.util.List;

public class DBManager {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public DBManager(Context context) {
//        dbHelper = new DatabaseHelper(this, "BookLending.db", context, 1);
        //获取实例化对象
//        db = DatabaseHelper.getWritableDatabase();
    }

    /**
     * add UsersInfo List
     */
    public void add(List<UsersInfo> usersInfos) {
        db.beginTransaction();//开始事务
        for (UsersInfo usersInfo : usersInfos) {
            db.execSQL("insert into users values(?,?,null)", new Object[]{
                    usersInfo.uId, usersInfo.uPass
            });
        }
        db.setTransactionSuccessful();//设置事务完成
        db.endTransaction();//结束事务
    }

    /**
     * add UserInfo String.etc
     */
    public void add(int uId, String uPass) {
        db.beginTransaction();
        db.execSQL("insert into users values(?,?,null)", new Object[]{
                uId, uPass
        });
    }

    /**
     * update info
     **/
    public void update() {

    }

    /**
     * query all userInfo return list
     */
    public List<UsersInfo> query() {
        ArrayList<UsersInfo> usersInfos = new ArrayList<>();
        Cursor c = queryTheCursor();
        while (c.moveToNext()) {
            UsersInfo usersInfo = new UsersInfo();
//            usersInfo._id = c.getInt(c.getColumnIndex("_id"));
            usersInfo.uId = c.getInt(c.getColumnIndex("uId"));
            usersInfo.uPass = c.getString(c.getColumnIndex("uPass"));
            usersInfo.uName = c.getString(c.getColumnIndex("uName"));
            usersInfos.add(usersInfo);
        }
        c.close();
        return usersInfos;
    }

    /**
     * query all userInfo return cursor
     */
    public Cursor queryTheCursor() {
        Cursor c = db.rawQuery("SELECT * FROM users", null);
        return c;
    }

    /**
     * close database
     */
    public void closeDB() {
        db.close();
    }
}

