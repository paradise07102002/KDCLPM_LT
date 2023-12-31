package edu.huflit.kdclpm_lt.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import edu.huflit.kdclpm_lt.Object.DocGia;
import edu.huflit.kdclpm_lt.Object.LoaiSach;
import edu.huflit.kdclpm_lt.Object.MuonTraSach;
import edu.huflit.kdclpm_lt.Object.Sach;
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
        values.put(DBHelper.AVARTAR_USER, user.getAvartar_user());
        return database.insert(DBHelper.TABLE_USER, null, values);
    }
    public long addDocGia(DocGia docGia)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.TEN_DOC_GIA, docGia.getTen_doc_gia());
        contentValues.put(DBHelper.EMAIL_DOC_GIA, docGia.getEmail_doc_gia());
        contentValues.put(DBHelper.PHONE_DOC_GIA, docGia.getPhone_doc_gia());
        contentValues.put(DBHelper.EMAIL_DOC_GIA, docGia.getEmail_doc_gia());
        contentValues.put(DBHelper.ADDRESS_DOC_GIA, docGia.getAddress_doc_gia());
        contentValues.put(DBHelper.IMAGE_DOC_GIA, docGia.getImage_doc_gia());
        return database.insert(DBHelper.TABLE_DOC_GIA, null, contentValues);
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
    //Lấy đọc giả by ID
    public Cursor getDocGiaByID(int ma_doc_gia)
    {
        String select = "SELECT * FROM " + DBHelper.TABLE_DOC_GIA + " WHERE " + DBHelper.MA_DOC_GIA + " = " + "'" + ma_doc_gia + "'";
        Cursor cursor = database.rawQuery(select, null);
        return cursor;
    }
    //Lấy sách khi có mã sách
    public Cursor getSachByMaSach(int ma_sach)
    {
        String select = "SELECT * FROM " + DBHelper.TABLE_SACH + " WHERE " + DBHelper.MA_SACH_S + " = " + "'" + ma_sach + "'";
        Cursor cursor = database.rawQuery(select, null);
        return cursor;
    }
    //
    public Cursor getMuonTraByID(int ma_mt)
    {
        String select = "SELECT * FROM " + DBHelper.TABLE_MUON_TRA + " WHERE " + DBHelper.MA_MUON_TRA_MTS + " = " + "'" + ma_mt + "'";
        Cursor cursor = database.rawQuery(select, null);
        return cursor;
    }
    //Thêm mượn trả sách
    public long addMuonTraSach(MuonTraSach muonTraSach)
    {
        ContentValues values = new ContentValues();
        values.put(DBHelper.MA_SACH_MTS, muonTraSach.getMa_sach_mts());
        values.put(DBHelper.MA_USER_MTS, muonTraSach.getMa_user_mts());
        values.put(DBHelper.NGAY_MUON_MTS, muonTraSach.getNgay_muon_mts());
        values.put(DBHelper.NGAY_TRA_MTS, muonTraSach.getNgay_tra_mts());

        return database.insert(DBHelper.TABLE_MUON_TRA, null, values);
    }
    //Sửa trạng thái sách
    public long suaTrangThaiSach(int ma_sach, int trang_thai)
    {
        ContentValues values = new ContentValues();
        values.put(DBHelper.TRANG_THAI_S, trang_thai);
        return database.update(DBHelper.TABLE_SACH, values, DBHelper.MA_SACH_S + " = " + ma_sach,null);
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
    public long xoaDocGia(int ma_dg)
    {
        return database.delete(DBHelper.TABLE_DOC_GIA, DBHelper.MA_DOC_GIA + " = " + "'" + ma_dg + "'", null);
    }
    public long xoaUser(int ma_nv)
    {
        return database.delete(DBHelper.TABLE_USER, DBHelper.ID_USER + " = " + "'" + ma_nv + "'", null);
    }
    public long xoaMuonTra(int ma_mt)
    {
        return database.delete(DBHelper.TABLE_MUON_TRA, DBHelper.MA_MUON_TRA_MTS + " = " + "'" + ma_mt + "'", null);
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
    //Lấy dữ liệu đoc giả
    public Cursor layDuLieuDocGia()
    {
        String select = "SELECT * FROM " + DBHelper.TABLE_DOC_GIA;
        Cursor cursor = database.rawQuery(select, null);
        return cursor;
    }
    public Cursor layDuLieuMuonTra()
    {
        String select = "SELECT * FROM " + DBHelper.TABLE_MUON_TRA;
        Cursor cursor = database.rawQuery(select, null);
        return cursor;
    }
    //Lấy dữ liệu đoc giả
    public Cursor layDuLieuNhanVien()
    {
        String select = "SELECT * FROM " + DBHelper.TABLE_USER + " WHERE " + DBHelper.ROLE_USER + " = " + "'Librarian'";
        Cursor cursor = database.rawQuery(select, null);
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
    //Thêm sách
    public long addBook(Sach sach)
    {
        ContentValues values = new ContentValues();
        values.put(DBHelper.MA_LOAI_SACH_S, sach.getMa_loai_sach_s());
        values.put(DBHelper.TEN_SACH_S, sach.getTen_sach_s());
        values.put(DBHelper.TAC_GIA_S, sach.getTac_gia_s());
        values.put(DBHelper.NHA_XUAT_BAN_S, sach.getNha_xuat_ban_s());
        values.put(DBHelper.NAM_XUAT_BAN_S, sach.getNam_xuat_ban_s());
        values.put(DBHelper.IMAGE_SACH, sach.getImage_sach());
        values.put(DBHelper.TRANG_THAI_S, 0);
        values.put(DBHelper.MO_TA_SACH, sach.getMo_ta_sach());
        return database.insert(DBHelper.TABLE_SACH, null, values);
    }
    //
    public Cursor layDuLieuSachByID(int ma_sach)
    {
        String select = "SELECT * FROM " + DBHelper.TABLE_SACH + " WHERE " + DBHelper.MA_SACH_S + " = " + ma_sach;
        Cursor cursor = database.rawQuery(select, null);
        return cursor;
    }
    //
    public Cursor layDuLieuDGByID(int ma_dg)
    {
        String select = "SELECT * FROM " + DBHelper.TABLE_DOC_GIA + " WHERE " + DBHelper.MA_DOC_GIA + " = " + ma_dg;
        Cursor cursor = database.rawQuery(select, null);
        return cursor;
    }
    public Cursor layDuLieuNhanVienByID(int ma_nv)
    {
        String select = "SELECT * FROM " + DBHelper.TABLE_USER + " WHERE " + DBHelper.ID_USER + " = " + ma_nv;
        Cursor cursor = database.rawQuery(select, null);
        return cursor;
    }
    //Sửa sách
    public long suaSach(Sach sach)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.MA_SACH_S, sach.getMa_sach_s());
        contentValues.put(DBHelper.TEN_SACH_S, sach.getTen_sach_s());
        contentValues.put(DBHelper.TAC_GIA_S, sach.getTac_gia_s());
        contentValues.put(DBHelper.NHA_XUAT_BAN_S, sach.getNha_xuat_ban_s());
        contentValues.put(DBHelper.NAM_XUAT_BAN_S, sach.getNam_xuat_ban_s());
        contentValues.put(DBHelper.MO_TA_SACH, sach.getMo_ta_sach());
        contentValues.put(DBHelper.IMAGE_SACH, sach.getImage_sach());
        return database.update(DBHelper.TABLE_SACH, contentValues, DBHelper.MA_SACH_S + " = " + sach.getMa_sach_s(), null);
    }
    //
    public long suaDocGia(DocGia docGia)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.MA_DOC_GIA, docGia.getMa_doc_gia());
        contentValues.put(DBHelper.TEN_DOC_GIA, docGia.getTen_doc_gia());
        contentValues.put(DBHelper.EMAIL_DOC_GIA, docGia.getEmail_doc_gia());
        contentValues.put(DBHelper.PHONE_DOC_GIA, docGia.getPhone_doc_gia());
        contentValues.put(DBHelper.ADDRESS_DOC_GIA, docGia.getAddress_doc_gia());
        contentValues.put(DBHelper.IMAGE_DOC_GIA, docGia.getImage_doc_gia());
        return database.update(DBHelper.TABLE_DOC_GIA, contentValues, DBHelper.MA_DOC_GIA + " = " + docGia.getMa_doc_gia(), null);
    }
    public long suaUser(User user)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.ID_USER, user.getId_user());
        contentValues.put(DBHelper.FULLNAME_USER, user.getFullname_user());
        contentValues.put(DBHelper.EMAIL_USER, user.getEmail_user());
        contentValues.put(DBHelper.PHONE_USER, user.getPhone_user());
        contentValues.put(DBHelper.AVARTAR_USER, user.getAvartar_user());
        return database.update(DBHelper.TABLE_USER, contentValues, DBHelper.ID_USER + " = " + user.getId_user(), null);
    }
    //Kiểm tra nếu đầu sách có chứa sách
    public boolean checkDauSach(int ma_dau_sach)
    {
        String select = "SELECT * FROM " + DBHelper.TABLE_SACH + " WHERE " + DBHelper.MA_LOAI_SACH_S + " = " + ma_dau_sach;
        Cursor cursor = database.rawQuery(select, null);
        if (cursor.moveToFirst())
        {
            cursor.close();
            return true;// có sách
        }
        cursor.close();
        return false;//không có sách
    }
    //Đổi mật khẩu
    public long doiMK(String username, String mat_khau_moi)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.PASSWORD_USER, mat_khau_moi);
        return database.update(DBHelper.TABLE_USER, contentValues, DBHelper.USERNAME_USER +
                " = " + "'" + username + "'", null);
    }
    //Cập nhật user
    public long updateUser(User user)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.FULLNAME_USER, user.getFullname_user());
        contentValues.put(DBHelper.EMAIL_USER, user.getEmail_user());
        contentValues.put(DBHelper.PHONE_USER, user.getPhone_user());
        return database.update(DBHelper.TABLE_USER, contentValues, DBHelper.USERNAME_USER +
                " = " + "'" + user.getUsername_user() + "'", null);
    }
    //Kiểm tra mật khẩu
    public boolean checkMK(String username, String password)
    {
        String select = "SELECT * FROM " + DBHelper.TABLE_USER + " WHERE " + DBHelper.USERNAME_USER + " = " + "'" + username + "'" + " AND " + DBHelper.PASSWORD_USER + " = " + "'" + password + "'";
        Cursor cursor = database.rawQuery(select, null);
        if (cursor.moveToFirst() == false)
        {
            cursor.close();
            return false;//mk cũ sai
        }
        else
        {
            cursor.close();
            return true;
        }
    }
    public Cursor layThongTinTaiKhoan(String username)
    {
        String select = "SELECT * FROM " + DBHelper.TABLE_USER + " WHERE " + DBHelper.USERNAME_USER + " = " + "'" + username + "'";
        Cursor cursor = database.rawQuery(select, null);
        return cursor;
    }
    public boolean checkUsername(String username)
    {
        String select = "SELECT * FROM " + DBHelper.TABLE_USER + " WHERE " + DBHelper.USERNAME_USER + " = " + "'" + username + "'";
        Cursor cursor = database.rawQuery(select, null);
        if (cursor.moveToFirst())
        {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }
    //Tìm kiếm tên sách
    public Cursor searchNameBook(String name_book)
    {
        String select = "SELECT * FROM " + DBHelper.TABLE_SACH + " WHERE " + DBHelper.TEN_SACH_S + " LIKE '%" + name_book + "%'";
        Cursor cursor = database.rawQuery(select, null);
        return cursor;
    }
    public Cursor searchIDBook(int ma_sach)
    {
        String select = "SELECT * FROM " + DBHelper.TABLE_SACH + " WHERE " + DBHelper.MA_SACH_S + " = " + "'" + ma_sach + "'";
        Cursor cursor = database.rawQuery(select, null);
        return cursor;
    }
    public Cursor searchIDDauSach(int ma_ds)
    {
        String select = "SELECT * FROM " + DBHelper.TABLE_SACH + " WHERE " + DBHelper.MA_LOAI_SACH_S + " = " + "'" + ma_ds + "'";
        Cursor cursor = database.rawQuery(select, null);
        return cursor;
    }
}
