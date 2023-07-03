package com.example.experiment2_diary;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    //数据库名字
    public static final String DB_NAME = "User.db";
    public static final String TABLE_NAME0="user";
    public static final String TABLE_NAME="Diary";
    public static final int VERSION=1;
    //创建用户表
    private static final String CREATE_USER = "create table user(id integer primary key autoincrement," +
            "username varchar(30)," +
            "password char(32)," +
            "phone varchar(30)," +
            "emil varchar(30))";
    //创建日记表

    public static final String CREATE_DIARY="create table Diary(id integer primary key autoincrement," +
            "title text," +
            "time text," +
            "author varchar(30)," +
            "content text," +
            "picture BLOB)";
    //运行程序时，Android Studio帮你创建数据库，只会执行一次


    public MySQLiteOpenHelper(@Nullable Context context) {
        super(context,DB_NAME,null,1);
    }
    public MySQLiteOpenHelper(Context context, String dbname, SQLiteDatabase.CursorFactory factory, int version){
        super(context, dbname, factory, version);
    }
    //创建数据表
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_USER);
        sqLiteDatabase.execSQL(CREATE_DIARY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public long register(User u){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("username ",u.getUsername());
        cv.put("password",u.getPassword());
        cv.put("phone",u.getPhone());
        cv.put("emil",u.getEmil());
        long users = db.insert("user",null,cv);
        return users;
    }

    //登录方法实现
    public boolean login(String username,String password){
        SQLiteDatabase db = getWritableDatabase();
        boolean result = false;
        Cursor users = db.query("user", null, "username like?", new String[]{username}, null, null, null);
        if(users!=null){
            while (users.moveToNext()){
                @SuppressLint("Range") String username1 = users.getString(users.getColumnIndex("username"));
                MD5 my_login = new MD5(password,username);
                String check_login = my_login.getMD5();
                String password1 = users.getString(2);
                result = password1.equals(check_login);
                return result;
            }
        }
        return false;
    }

    //根据用户名查找当前登录用户信息
    public User select(String username){
        SQLiteDatabase db = getWritableDatabase();
        User SelectUser = new User();
        Cursor user = db.query("user", new String[]{"username", "phone", "emil"}, "username=?", new String[]{username}, null, null, null);
        while(user.moveToNext()){
            @SuppressLint("Range") String uname =user.getString(user.getColumnIndex("username"));
            @SuppressLint("Range") String phone = user.getString(user.getColumnIndex("phone"));
            @SuppressLint("Range") String emil = user.getString(user.getColumnIndex("emil"));
            SelectUser.setUsername(uname);
            SelectUser.setPhone(phone);
            SelectUser.setEmil(emil);
        }
        user.close();
        return SelectUser;
    }

    public long diary_add(Diary dy){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("title",dy.getTitle());
        cv.put("author",dy.getAuthor());
        cv.put("time",dy.getTime());
        cv.put("content",dy.getContent());
        cv.put("picture",dy.getPicture().toString());
        long diart = db.insert("diary",null,cv);
        return diart;
    }

}