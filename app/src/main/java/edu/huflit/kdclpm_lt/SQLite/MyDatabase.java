package edu.huflit.kdclpm_lt.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import edu.huflit.kdclpm_lt.Object.User;

public class MyDatabase {
    SQLiteDatabase database;
    DBHelper helper;

    public MyDatabase(Context context)
    {
        helper = new DBHelper(context);
        database = helper.getWritableDatabase();
    }
    //Kiểm tra admin tồn tại chưa
    public boolean checkAdmin()
    {
        String select = "SELECT * FROM " + DBHelper.TABLE_USER + " WHERE " + DBHelper.ROLE_USER + " = " + "'admin'";
        Cursor cursor = database.rawQuery(select, null);
        if (cursor.moveToFirst())
        {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }
    public long addUser(User user)
    {
        ContentValues values = new ContentValues();
        values.put(DBHelper.USERNAME_USER, user.getUsername_user());
        values.put(DBHelper.PASSWORD_USER, user.getPassword_user());
        values.put(DBHelper.FULLNAME_USER, user.getFullname_user());
        values.put(DBHelper.EMAIL_USER, user.getEmail_user());
        values.put(DBHelper.PHONE_USER, user.getPhone_user());
        values.put(DBHelper.ROLE_USER, user.getRole_user());
        values.put(DBHelper.LOAI_KH_USER, user.getLoai_kh_user());
        return database.insert(DBHelper.TABLE_USER, null, values);
    }
}
