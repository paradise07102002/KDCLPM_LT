package edu.huflit.kdclpm_lt.DauSach;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import edu.huflit.kdclpm_lt.ManHinhChinh;
import edu.huflit.kdclpm_lt.Object.LoaiSach;
import edu.huflit.kdclpm_lt.R;
import edu.huflit.kdclpm_lt.SQLite.DBHelper;
import edu.huflit.kdclpm_lt.SQLite.MyDatabase;

public class UpdateDauSach extends Fragment {
    View view;
    ImageView back;
    EditText edt_ten_dau_sach;
    Button update;
    MyDatabase database;
    ManHinhChinh manHinhChinh;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.update_dau_sach, container, false);
        database = new MyDatabase(getActivity());
        manHinhChinh = (ManHinhChinh) getActivity();
        anhXa();
        SharedPreferences lay_id_dau_sach = getActivity().getSharedPreferences("lay_ma_dau_sach", Context.MODE_PRIVATE);
        int ma_ds = lay_id_dau_sach.getInt("ma_dau_sach", 0);

        Cursor cursor = database.layDuLieuDauSachByID(ma_ds);
        int ten_ds_index = cursor.getColumnIndex(DBHelper.TEN_LOAI_SACH_LS);

        cursor.moveToFirst();
        edt_ten_dau_sach.setText(cursor.getString(ten_ds_index));
        cursor.close();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (kiemTraNhapThongTin() == false)
                {
                }
                else {
                    if (kiemTraTenDauSach(edt_ten_dau_sach.getText().toString().trim()) == true)
                    {
                    }
                    else
                    {
                        SharedPreferences lay_id_dau_sach = getActivity().getSharedPreferences("lay_ma_dau_sach", Context.MODE_PRIVATE);
                        int ma_ds = lay_id_dau_sach.getInt("ma_dau_sach", 0);
                        LoaiSach loaiSach = new LoaiSach();
                        loaiSach.setMa_loai_sach_ls(ma_ds);
                        loaiSach.setLoai_sach_ls(edt_ten_dau_sach.getText().toString().trim());

                        database.suaDauSach(loaiSach);
                        Toast.makeText(getActivity(), "Cập nhật thành công", Toast.LENGTH_LONG).show();
                        manHinhChinh.nextQLDauSach();
                    }
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manHinhChinh.nextQLDauSach();
            }
        });
        return view;
    }
    public void anhXa()
    {
        back = (ImageView) view.findViewById(R.id.update_dau_sach_back);
        edt_ten_dau_sach  =(EditText) view.findViewById(R.id.edt_ten_dau_sach_update);
        update = (Button) view.findViewById(R.id.btn_update_dau_sach);
    }
    public boolean kiemTraNhapThongTin()
    {
        //mds, ts, tg, nhaxb, namxb, tt;
        if (edt_ten_dau_sach.getText().toString().trim().length() == 0)
        {
            String p = "<font color='#FF0000'>Không được để trống!</font>";
            edt_ten_dau_sach.setHint(Html.fromHtml(p));
            return false;
        }
        return true;
    }
    public boolean kiemTraTenDauSach(String ten_dau_sach)
    {
        boolean check = database.kiemTraTenDS(ten_dau_sach);
        if (check == true)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Tên đầu sách đã tồn tại");
            builder.setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            builder.create().show();
            return true;
        }
        else
        {
            return false;
        }
    }
}
