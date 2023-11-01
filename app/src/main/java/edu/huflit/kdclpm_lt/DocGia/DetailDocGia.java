package edu.huflit.kdclpm_lt.DocGia;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import edu.huflit.kdclpm_lt.ManHinhChinh;
import edu.huflit.kdclpm_lt.Object.LoaiSach;
import edu.huflit.kdclpm_lt.Object.Sach;
import edu.huflit.kdclpm_lt.R;
import edu.huflit.kdclpm_lt.SQLite.DBHelper;
import edu.huflit.kdclpm_lt.SQLite.MyDatabase;

public class DetailDocGia extends Fragment {
    View view;
    MyDatabase database;
    ManHinhChinh manHinhChinh;
    TextView name, email, phone, address;
    ImageView back, avartar;
    Button xoa, sua;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.detail_doc_gia, container, false);
        database = new MyDatabase(getActivity());
        manHinhChinh = (ManHinhChinh) getActivity();
        anhXa();




        return view;
    }
    public void anhXa()
    {
        name = (TextView) view.findViewById(R.id.detail_dg_name);
        email = (TextView) view.findViewById(R.id.detail_dg_email);
        phone = (TextView) view.findViewById(R.id.detail_dg_phone);
        address = (TextView) view.findViewById(R.id.detail_dg_address);

        back = (ImageView) view.findViewById(R.id.detail_dg_back);
        avartar = (ImageView) view.findViewById(R.id.detail_dg_avartar);

        xoa = (Button) view.findViewById(R.id.detail_dg_xoa);
        sua = (Button) view.findViewById(R.id.detail_dg_sua);
    }
    public Sach layDuLieu()
    {
        SharedPreferences lay_ma_dg = getActivity().getSharedPreferences("lay_ma_dg", Context.MODE_PRIVATE);
        int ma_dg = lay_ma_dg.getInt("ma_dg", 0);
        Cursor cursor = database.layDuLieuSachByID(ma_sach);
        Sach sach = new Sach();
        if (cursor != null)
        {
            int ma_dau_sach_index = cursor.getColumnIndex(DBHelper.MA_LOAI_SACH_S);

            int ten_sach_index = cursor.getColumnIndex(DBHelper.TEN_SACH_S);
            int ten_tg_index = cursor.getColumnIndex(DBHelper.TAC_GIA_S);
            int nha_xb_index = cursor.getColumnIndex(DBHelper.NHA_XUAT_BAN_S);
            int nam_xb_index = cursor.getColumnIndex(DBHelper.NAM_XUAT_BAN_S);
            int trang_thai_index = cursor.getColumnIndex(DBHelper.TRANG_THAI_S);
            int mo_ta_index = cursor.getColumnIndex(DBHelper.MO_TA_SACH);
            int img_sach_index = cursor.getColumnIndex(DBHelper.IMAGE_SACH);

            cursor.moveToFirst();
            sach.setMa_loai_sach_s(cursor.getInt(ma_dau_sach_index));
            sach.setTen_sach_s(cursor.getString(ten_sach_index));
            sach.setTac_gia_s(cursor.getString(ten_tg_index));
            sach.setNha_xuat_ban_s(cursor.getString(nha_xb_index));
            sach.setNam_xuat_ban_s(cursor.getInt(nam_xb_index));
            sach.setTrang_thai_s(cursor.getInt(trang_thai_index));
            sach.setMo_ta_sach(cursor.getString(mo_ta_index));
            sach.setImage_sach(cursor.getBlob(img_sach_index));

            cursor.close();
        }
        return sach;
    }
    public void showDuLieuSach()
    {
        Sach sach = layDuLieu();
        LoaiSach loaiSach = new LoaiSach();

        //Ta có mã đầu sách, truy vấn lấy tên đầu sách
        String ten_dau_sach = "";
        Cursor cursor = database.layDuLieuDauSachByID(sach.getMa_loai_sach_s());
        if (cursor != null)
        {
            int ten_dau_sach_index = cursor.getColumnIndex(DBHelper.TEN_LOAI_SACH_LS);
            cursor.moveToFirst();
            loaiSach.setLoai_sach_ls(cursor.getString(ten_dau_sach_index));
            cursor.close();
        }
//        Đổ dữ liệu lên TextView
        dau_sach.setText(loaiSach.getLoai_sach_ls());
        ten_sach.setText(sach.getTen_sach_s());
        tac_gia.setText(sach.getTac_gia_s());
        nha_xb.setText(sach.getNha_xuat_ban_s());
        nam_xb.setText(Integer.toString(sach.getNam_xuat_ban_s()));
        mo_ta_sach.setText(sach.getMo_ta_sach());
        int tt = sach.getTrang_thai_s();
        if (tt == 0)
        {
            trang_thai.setText("Chưa cho mượn");
        }
        else
        {
            trang_thai.setText("Đã cho mượn");
        }
        byte[] bytes = sach.getImage_sach();
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        img_sach.setImageBitmap(bitmap);
    }
}
