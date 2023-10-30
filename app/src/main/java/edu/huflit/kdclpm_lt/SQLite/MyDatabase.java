package edu.huflit.kdclpm_lt.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import edu.huflit.kdclpm_lt.Object.LoaiSach;
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
//        values.put(DBHelper.LOAI_KH_USER, user.getLoai_kh_user());
        return database.insert(DBHelper.TABLE_USER, null, values);
    }
    //Kiểm tra đăng nhập
    public boolean checkLogin(String username, String password)
    {
        String select = "SELECT * FROM " + DBHelper.TABLE_USER + " WHERE " + DBHelper.USERNAME_USER + " = " + "'" + username + "'" + " AND " + DBHelper.PASSWORD_USER + " = " + "'" + password + "'";
        Cursor cursor = database.rawQuery(select, null);
        if (cursor.moveToFirst() == false)
        {
            return false;//Ko đúng user or pass
        }
        else
        {
            return true;
        }
    }
    //Lấy thông tin User khi có username
    public Cursor getUserByUsername(String username)
    {
        String select = "SELECT * FROM " + DBHelper.TABLE_USER + " WHERE " + DBHelper.USERNAME_USER + " = " + "'" + username + "'";
        Cursor cursor = database.rawQuery(select, null);
        return cursor;
    }
    //Thêm đầu sách
    public long addLoaiSach(LoaiSach loaiSach)
    {
        ContentValues values = new ContentValues();
        values.put(DBHelper.TEN_LOAI_SACH_LS, loaiSach.getLoai_sach_ls());
        return database.insert(DBHelper.TABLE_LOAI_SACH, null, values);
    }
    //Kiểm tra tên đầu sách có tồn tại chưa
    public boolean checkTenDauSach(String ten_dau_sach)
    {
        //Truy vấn lấy cột mã loại sách nếu mã loại sách nhập vào có trong Database
        String select = "SELECT " + DBHelper.MA_LOAI_SACH_LS  + " FROM " + DBHelper.TABLE_LOAI_SACH + " WHERE " + DBHelper.TEN_LOAI_SACH_LS + " = " + "'" + ten_dau_sach + "'";
        //Đưa kết quả truy vấn vào trong cursor/ Do mã sách là duy nhất nên chỉ có thể tìm ra tối đa 1 đối tượng mã sách
        Cursor cursor = database.rawQuery(select, null);
        if (cursor.moveToFirst())
        {
            return true;//Mã loại sách tồn tại
        }
        return false;//Mã loại sách không tồn tại
    }
    //Lấy thông tin đầu sách
    public Cursor layDuLieuDauSach()
    {
        String[] cot = {DBHelper.MA_LOAI_SACH_LS, DBHelper.TEN_LOAI_SACH_LS};
        Cursor cursor = database.query(DBHelper.TABLE_LOAI_SACH, cot, null, null, null, null, null);
        return cursor;
    }
    //Xóa đầu sách
    public long xoaDauSach(int ma_dau_sach)
    {
        return database.delete(DBHelper.TABLE_LOAI_SACH, DBHelper.MA_LOAI_SACH_LS + " = " + "'" + ma_dau_sach + "'", null);
    }
    //Lấy dữ liệu đầu sách khi có id đầu sách
    public Cursor layDuLieuDauSachByID(int ma_dau_sach)
    {
        String select = "SELECT * FROM " + DBHelper.TABLE_LOAI_SACH + " WHERE " + DBHelper.MA_LOAI_SACH_LS + " = " + ma_dau_sach;
        Cursor cursor = database.rawQuery(select, null);
        return cursor;
    }
    //Sửa đầu sách
    public long suaDauSach(LoaiSach loaiSach)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.MA_LOAI_SACH_LS, loaiSach.getMa_loai_sach_ls());
        contentValues.put(DBHelper.TEN_LOAI_SACH_LS, loaiSach.getLoai_sach_ls());
        return database.update(DBHelper.TABLE_LOAI_SACH, contentValues, DBHelper.MA_LOAI_SACH_LS + " = " + loaiSach.getMa_loai_sach_ls(), null);
    }
    //Kiểm tra tên đầu sách tồn tại chưa
    public boolean kiemTraTenDS(String ten_dau_sach)
    {
        String select = "SELECT * FROM " + DBHelper.TABLE_LOAI_SACH + " WHERE " + DBHelper.TEN_LOAI_SACH_LS + " = " + "'" + ten_dau_sach + "'";
        Cursor cursor = database.rawQuery(select, null);
        if (cursor.moveToFirst() == true)
        {
            return true;//có tồn tại
        }
        else
        {
            return false;
        }
    }
    //Lấy dữ liệu sách
    public Cursor layDuLieuSach()
    {
        String[] cot = {DBHelper.MA_SACH_S, DBHelper.TEN_SACH_S, DBHelper.IMAGE_SACH};
        Cursor cursor = database.query(DBHelper.TABLE_SACH, cot, null, null, null, null, null);
        return cursor;
    }
    //Xóa sách
    public long xoaSach(int ma_sach)
    {
        return database.delete(DBHelper.TABLE_SACH, DBHelper.MA_SACH_S + " = " + "'" + ma_sach + "'", null);
    }
    //
    public Cursor layFullDuLieuDauSach()
    {
        String select = "SELECT " + DBHelper.TEN_LOAI_SACH_LS + " FROM " + DBHelper.TABLE_LOAI_SACH;
        Cursor cursor = database.rawQuery(select, null);
        return cursor;
    }
    //
    public LoaiSach getMaDauSachByTenDS(String ten_dau_sach)
    {
        LoaiSach loaiSach = new LoaiSach();
        String select = "SELECT * FROM "
                + DBHelper.TABLE_LOAI_SACH + " WHERE " + DBHelper.TEN_LOAI_SACH_LS + " = "
                + "'" + ten_dau_sach + "'";
        Cursor cursor = database.rawQuery(select, null);
        if (cursor != null)
        {
            int ma_dau_sach_index = cursor.getColumnIndex(DBHelper.MA_LOAI_SACH_LS);
            while (cursor.moveToNext())
            {
                //có 10 bản ghi sách trong cursor
                //moveToFirst chỉ tới cuốn sách đầu
                loaiSach.setMa_loai_sach_ls(cursor.getInt(ma_dau_sach_index));
            }
        }
        cursor.close();
        return loaiSach;
    }
}
