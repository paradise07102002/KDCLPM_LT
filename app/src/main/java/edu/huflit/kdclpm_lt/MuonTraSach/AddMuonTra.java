package edu.huflit.kdclpm_lt.MuonTraSach;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Calendar;
import java.util.Date;

import edu.huflit.kdclpm_lt.ManHinhChinh;
import edu.huflit.kdclpm_lt.Object.MuonTraSach;
import edu.huflit.kdclpm_lt.R;
import edu.huflit.kdclpm_lt.SQLite.DBHelper;
import edu.huflit.kdclpm_lt.SQLite.MyDatabase;

public class AddMuonTra extends Fragment {
    View view;
    ManHinhChinh manHinhChinh;
    MyDatabase database;
    ImageView back;
    EditText username_muon, ma_sach_muon;
    TextView ngay_muon, ngay_tra;
    Button btn_create;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_muon_tra, container, false);
        database = new MyDatabase(getActivity());
        manHinhChinh = (ManHinhChinh) getActivity();
        anhXa();
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPhieuMuon();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manHinhChinh.nextQLMuonTra();
            }
        });
        return view;
    }
    public void anhXa()
    {
        back = (ImageView) view.findViewById(R.id.create_phieu_muon_back);
        username_muon = (EditText) view.findViewById(R.id.phieu_muon_username);
        ma_sach_muon = (EditText) view.findViewById(R.id.phieu_muon_ma_sach);
        ngay_muon = (TextView) view.findViewById(R.id.phieu_muon_ngay_muon);
        ngay_tra = (TextView) view.findViewById(R.id.phieu_muon_ngay_tra);
        btn_create = (Button) view.findViewById(R.id.phieu_muon_btn);
    }
    public void showNgay()
    {
        //Ngày tháng năm hiện tại
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);

        String a = String.format("%02d", day);
        String b = String.format("%02d", month);
        String c = String.format("%04d", year);

        ngay_muon.setText(a + "-" + b + "-" + c);
        //Tính ngày trả
        Date date_hien_tai = new Date();
        Calendar day_tra = Calendar.getInstance();//lấy ngày hiện tại
        day_tra.setTime(date_hien_tai);
        //Thêm số ngày cần
        day_tra.add(Calendar.DATE, 5);//Mượn 5 ngày sẽ trả sách
        //Lấy ngày Calendar
        int day1 = day_tra.get(Calendar.DAY_OF_MONTH);
        int month1 = day_tra.get(Calendar.MONTH) + 1;
        int year1 = day_tra.get(Calendar.YEAR);

        String aa = String.format("%02d", day1);
        String bb = String.format("%02d", month1);
        String cc = String.format("%04d", year1);
        ngay_tra.setText(aa + "-" + bb + "-" + cc);

    }
    public boolean kiemTraNhapThongTin()
    {
        if (username_muon.length() == 0 )
        {
            String p = "<font color='#FF0000'>Vui lòng nhập mã đọc giả mượn</font>";
            username_muon.setHint(Html.fromHtml(p));
            return false;
        } else if (ma_sach_muon.length() == 0)
        {
            String p = "<font color='#FF0000'>Vui lòng nhập mã sách mượn</font>";
            ma_sach_muon.setHint(Html.fromHtml(p));
            return false;
        }
        return true;
    }
    public boolean checkMaDocGiaMuon()
    {
        Cursor cursor = database.getDocGiaByID(Integer.parseInt(username_muon.getText().toString().trim()));
        if (cursor == null || cursor.getCount() == 0)
        {
            return false;
        }
        return true;
    }
    public boolean checkMaSach()
    {
        Cursor cursor = database.getSachByMaSach(Integer.parseInt(ma_sach_muon.getText().toString().trim()));
        if (cursor == null || cursor.getCount() == 0)
        {
            return false;
        }
        return true;
    }
    public boolean checkTrangThaiSach()
    {
        Cursor cursor = database.getSachByMaSach(Integer.parseInt(ma_sach_muon.getText().toString().trim()));
        if (cursor != null)
        {
            int trang_thai_index = cursor.getColumnIndex(DBHelper.TRANG_THAI_S);
            cursor.moveToFirst();
            int tt = cursor.getInt(trang_thai_index);
            if (tt == 0)
            {
                return true;
            }
        }
        return false;
    }
    public void createPhieuMuon()
    {
        boolean check = kiemTraNhapThongTin();//Kiểm tra đã nhập thông tin chưa
        if (check == false)
        {
            return;//Kết thúc
        }
        if (checkMaDocGiaMuon()==false)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Mã đọc giả không tồn tại");
            builder.setNegativeButton("Đóng", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            builder.create().show();
            return;
        }
        if (checkMaSach() == false)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Mã sách không tồn tại");
            builder.setNegativeButton("Đóng", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            builder.create().show();
            return;
        }
        if (checkTrangThaiSach() == false)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Sách hiện không có trong thư viện");
            builder.setNegativeButton("Đóng", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            builder.create().show();
            return;
        }
        else
        {

            MuonTraSach muonTraSach = new MuonTraSach();
            muonTraSach.setMa_sach_mts(Integer.parseInt(ma_sach_muon.getText().toString().trim()));
            muonTraSach.setMa_user_mts(Integer.parseInt(username_muon.getText().toString().trim()));
            muonTraSach.setNgay_muon_mts(ngay_muon.getText().toString().trim());
            muonTraSach.setNgay_tra_mts(ngay_tra.getText().toString().trim());
            database.addMuonTraSach(muonTraSach);

            //Cập nhật lại trạng thái sách
            database.suaTrangThaiSach(Integer.parseInt(ma_sach_muon.getText().toString().trim()), 1);
            //tHÔNG BÁO MƯỢN THÀNH CÔNG
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Tạo phiếu mượn thành công");
            builder.setNegativeButton("Đóng", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            builder.create().show();
        }
    }
}
