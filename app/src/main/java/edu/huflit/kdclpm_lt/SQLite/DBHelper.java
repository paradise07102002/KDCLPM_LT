package edu.huflit.kdclpm_lt.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String TEN_DATABASE = "DATABASE";

    //BẢNG TIN NHẮN
    public static final String TABLE_TIN_NHAN = "Chat";
    public static final String ID_TIN_NHAN = "id_tin_nhan";
    public static final String ID_CHAT_USER = "id_chat_user";
    public static final String ID_CHAT_ADMIN = "id_chat_admin";
    public static final String ID_GUI = "id_gui";
    public static final String NOI_DUNG = "noi_dung";
    public static final String THOI_GIAN_GUI = "thoi_gian_gui";
    //BẢNG SÁCH
    public static final String TABLE_SACH = "Sach";
    public static final String IMAGE_SACH = "image_sach";

    public static final String MA_SACH_S = "ma_sach_s";

    public static final String MA_LOAI_SACH_S = "ma_loai_sach_s";
    public static final String TEN_SACH_S = "ten_sach_s";
    public static final String TAC_GIA_S = "tac_gia_s";
    public static final String NHA_XUAT_BAN_S = "nha_xuat_ban_s";
    public static final String NAM_XUAT_BAN_S = "nam_xuat_ban_s";
    public static final String TRANG_THAI_S = "trang_thai_s";
    public static final String MO_TA_SACH = "mo_ta_sach";

    //BẢNG LOẠI SÁCH
    public static final String TABLE_LOAI_SACH = "LoaiSach";
    public static final String MA_LOAI_SACH_LS = "ma_loai_sach_ls";
    public static final String TEN_LOAI_SACH_LS = "ten_loai_sach_ls";

    //BẢNG Bình Luận Sách
    public static final String TABLE_BINH_LUAN = "BinhLuanSach";
    public static final String MA_BL = "ma_binh_luan";
    public static final String MA_SACH_BL = "ma_sach_binh_luan";
    public static final String MA_USER_BL = "ma_user_binh_luan";
    public static final String NOI_DUNG_BL = "noi_dung_binh_luan";

    //BẢNG USER
    public static final String TABLE_USER = "User";
    public static final String ID_USER = "id_user";
    public static final String USERNAME_USER = "username_user";
    public static final String PASSWORD_USER = "password_user";
    public static final String ROLE_USER = "role_user";
    public static final String FULLNAME_USER = "fullname_user";
//    public static final String LOAI_KH_USER = "loai_kh_user";
    public static final String EMAIL_USER = "email_user";
    public static final String PHONE_USER = "phone_user";
    //BẢNG ĐỌC GIẢ
    public static final String TABLE_DOC_GIA = "DocGia";
    public static final String MA_DOC_GIA = "ma_doc_gia";
    public static final String TEN_DOC_GIA = "ten_doc_gia";
    public static final String EMAIL_DOC_GIA = "email_doc_gia";
    public static final String PHONE_DOC_GIA = "phone_doc_gia";
    public static final String ADDRESS_DOC_GIA = "address_doc_gia";
    public static final String IMAGE_DOC_GIA = "image_doc_gia";

    //BẢNG MƯỢN TRẢ SÁCH
    public static final String TABLE_MUON_TRA= "MuonTraSach";
    public static final String MA_MUON_TRA_MTS = "ma_muon_tra_mts";
    public static final String MA_SACH_MTS = "ma_sach_mts";
    public static final String MA_USER_MTS = "ma_user_mts";
    public static final String NGAY_MUON_MTS = "ngay_muon_mts";
    public static final String NGAY_TRA_MTS = "ngay_tra_mts";

    //TẠO DATABASE
    //TẠO BẢNG TIN NHẮN
    private static final String TAO_TABLE_TIN_NHAN = ""
            +"CREATE TABLE " + TABLE_TIN_NHAN + "( "
            + ID_TIN_NHAN + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ID_CHAT_USER + " INTEGER NOT NULL, "
            + ID_CHAT_ADMIN + " INTEGER NOT NULL, "
            + ID_GUI + " INTEGER NOT NULL, "
            + NOI_DUNG + " TEXT NOT NULL, "
            + THOI_GIAN_GUI + " TEXT NOT NULL) ";
    //TẠO BẢNG USER
    private static final String TAO_TABLE_USER = ""
            + "CREATE TABLE " + TABLE_USER + "( "
            + ID_USER + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + USERNAME_USER + " TEXT NOT NULL, "
            + PASSWORD_USER + " TEXT NOT NULL, "
            + ROLE_USER + " TEXT NOT NULL, "
            + FULLNAME_USER + " TEXT NOT NULL, "
//            + LOAI_KH_USER + " TEXT, "
            + PHONE_USER + " TEXT NOT NULL,"
            + EMAIL_USER + " TEXT NOT NULL)";
    //TẠO BẢNG ĐỌC GIẢ
    private static final String TAO_TABLE_DOC_GIA = ""
            + "CREATE TABLE " + TABLE_DOC_GIA + "( "
            + IMAGE_DOC_GIA + " BLOB, "
            + MA_DOC_GIA + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TEN_DOC_GIA + " TEXT NOT NULL, "
            + EMAIL_DOC_GIA + " TEXT NOT NULL, "
            + ADDRESS_DOC_GIA + " TEXT NOT NULL, "
            + PHONE_DOC_GIA + " TEXT NOT NULL)";
    //TẠO BẢNG SÁCH
    private static final String TAO_TABLE_SACH = ""
            + "CREATE TABLE " + TABLE_SACH + "( "
            + IMAGE_SACH + " BLOB,"
            + MO_TA_SACH + " TEXT,"
            + MA_SACH_S + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + MA_LOAI_SACH_S + " INTEGER NOT NULL, "
            + TEN_SACH_S + " TEXT NOT NULL, "
            + TAC_GIA_S + " TEXT NOT NULL, "
            + NHA_XUAT_BAN_S + " TEXT NOT NULL, "
            + NAM_XUAT_BAN_S + " INTEGER NOT NULL, "
            + TRANG_THAI_S + " INTEGER DEFAULT 0 CHECK(" + TRANG_THAI_S + " IN(0,1)), "
            + "FOREIGN KEY (" + MA_LOAI_SACH_S + ")" + " REFERENCES " + TABLE_LOAI_SACH + "(" + MA_LOAI_SACH_LS + "))";
    //TẠO TABLE LOẠI SÁCH
    private static final String TAO_TABLE_LOAI_SACH = ""
            + "CREATE TABLE " + TABLE_LOAI_SACH + "( "
            + MA_LOAI_SACH_LS + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TEN_LOAI_SACH_LS + " TEXT NOT NULL )";

    //TẠO BẢNG BÌNH LUẬN

    private static final String TAO_TABLE_BINH_LUAN = ""
            + "CREATE TABLE " + TABLE_BINH_LUAN + "( "
            + MA_BL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + MA_SACH_BL + " INTEGER NOT NULL, "
            + MA_USER_BL + " INTEGER NOT NULL, "
            + NOI_DUNG_BL + " TEXT NOT NULL) ";
    //TẠO BẢNG MƯỢN TRẢ SÁCH
    private static final String TAO_TABLE_MUON_TRA = ""
            + "CREATE TABLE " + TABLE_MUON_TRA + "( "
            + MA_MUON_TRA_MTS + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + MA_SACH_MTS + " INTEGER NOT NULL, "
            + MA_USER_MTS + " INTEGER NOT NULL, "
            + NGAY_MUON_MTS + " TEXT, "
            + NGAY_TRA_MTS + " TEXT, "
            + "FOREIGN KEY (" + MA_SACH_MTS +")" + " REFERENCES " + TABLE_SACH + "(" + MA_SACH_S + "), "
            + "FOREIGN KEY (" + MA_USER_MTS + ")" + " REFERENCES " + TABLE_USER + "(" + ID_USER + "))";

    public DBHelper(Context context)
    {
        super(context, TEN_DATABASE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TAO_TABLE_SACH);
        sqLiteDatabase.execSQL(TAO_TABLE_LOAI_SACH);
        sqLiteDatabase.execSQL(TAO_TABLE_MUON_TRA);
        sqLiteDatabase.execSQL(TAO_TABLE_USER);
        sqLiteDatabase.execSQL(TAO_TABLE_BINH_LUAN);
        sqLiteDatabase.execSQL(TAO_TABLE_TIN_NHAN);
        sqLiteDatabase.execSQL(TAO_TABLE_DOC_GIA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
