package edu.huflit.kdclpm_lt.MuonTraSach;

import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import edu.huflit.kdclpm_lt.ManHinhChinh;
import edu.huflit.kdclpm_lt.R;
import edu.huflit.kdclpm_lt.SQLite.DBHelper;
import edu.huflit.kdclpm_lt.SQLite.MyDatabase;

public class Detail_MuonTra extends Fragment {
    View view;
    MyDatabase database;
    ManHinhChinh manHinhChinh;
    TextView ma_mt, ma_sach, ma_dg, ten_sach, ten_dg, ngay_muon, ngay_tra;
    ImageView back, image_sach;
    Button btn_tra_sach;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.detail_muon_tra, container, false);
        database = new MyDatabase(getActivity());
        manHinhChinh = (ManHinhChinh) getActivity();
        anhXa();
        showDuLieu();
        btn_tra_sach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                traSach();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manHinhChinh.nextQLMT();
            }
        });
        return view;
    }
    public void anhXa()
    {
        ma_mt = (TextView) view.findViewById(R.id.detail_mt_ma_mt);
        ma_sach = (TextView) view.findViewById(R.id.detail_mt_ma_sach);
        ma_dg = (TextView) view.findViewById(R.id.detail_mt_ma_dg);
        ten_sach = (TextView) view.findViewById(R.id.detail_mt_ts);
        ten_dg = (TextView) view.findViewById(R.id.detail_mt_tendg);
        ngay_muon = (TextView) view.findViewById(R.id.detail_mt_nm);
        ngay_tra = (TextView) view.findViewById(R.id.detail_mt_nt);

        back = (ImageView) view.findViewById(R.id.detail_mt_back);
        image_sach = (ImageView) view.findViewById(R.id.detail_mt_img_sach);

        btn_tra_sach = (Button) view.findViewById(R.id.detail_mt_btn);
    }
    public void showDuLieu()
    {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("lay_ma_mt", Context.MODE_PRIVATE);
        Cursor cursor = database.getMuonTraByID(sharedPreferences.getInt("ma_mt", -1));
        if (cursor.moveToFirst())
        {
            int ma_mt_index = cursor.getColumnIndex(DBHelper.MA_MUON_TRA_MTS);
            int ma_sach_index = cursor.getColumnIndex(DBHelper.MA_SACH_MTS);
            int ma_dg_index = cursor.getColumnIndex(DBHelper.MA_USER_MTS);
            int nm_index = cursor.getColumnIndex(DBHelper.NGAY_MUON_MTS);
            int nt_index = cursor.getColumnIndex(DBHelper.NGAY_TRA_MTS);

            ma_mt.setText(Integer.toString(cursor.getInt(ma_mt_index)));
            ma_sach.setText(Integer.toString(cursor.getInt(ma_sach_index)));
            ma_dg.setText(Integer.toString(cursor.getInt(ma_dg_index)));
            ngay_muon.setText(cursor.getString(nm_index));
            ngay_tra.setText(cursor.getString(nt_index));

            //Lấy tên sách và tên đọc giả
            Cursor get_ts = database.getSachByMaSach(cursor.getInt(ma_sach_index));
            if (get_ts.moveToFirst())
            {
                int ten_sach_index = get_ts.getColumnIndex(DBHelper.TEN_SACH_S);
                int image_index = get_ts.getColumnIndex(DBHelper.IMAGE_SACH);

                ten_sach.setText(get_ts.getString(ten_sach_index));
                byte[] bytes = get_ts.getBlob(image_index);
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                image_sach.setImageBitmap(bitmap);
            }
            Cursor get_dg = database.getDocGiaByID(cursor.getInt(ma_dg_index));
            if (get_dg.moveToFirst())
            {
                int ten_dg_index = get_dg.getColumnIndex(DBHelper.TEN_DOC_GIA);
                ten_dg.setText(get_dg.getString(ten_dg_index));
            }
            get_ts.close();;
            get_dg.close();
        }
        cursor.close();
    }
    public void traSach()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Xác nhận trả sách");
        builder.setPositiveButton("Trả sách", new DialogInterface.OnClickListener() {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("lay_ma_mt", Context.MODE_PRIVATE);
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Lấy mã sách
                Cursor cursor = database.getMuonTraByID(sharedPreferences.getInt("ma_mt", -1));
                if (cursor.moveToFirst())
                {
                    int ma_sach_index = cursor.getColumnIndex(DBHelper.MA_SACH_MTS);
                    database.suaTrangThaiSach(cursor.getInt(ma_sach_index), 0);

                    database.xoaMuonTra(sharedPreferences.getInt("ma_mt", -1));

                    Toast.makeText(getActivity(), "Trả sách thành công", Toast.LENGTH_LONG).show();
                    manHinhChinh.nextQLMT();
                }
            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.create().show();
    }
}
