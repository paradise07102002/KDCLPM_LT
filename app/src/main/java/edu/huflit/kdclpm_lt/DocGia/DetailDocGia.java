package edu.huflit.kdclpm_lt.DocGia;

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
import edu.huflit.kdclpm_lt.Object.DocGia;
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
    TextView tv_ma_dg;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.detail_doc_gia, container, false);
        database = new MyDatabase(getActivity());
        manHinhChinh = (ManHinhChinh) getActivity();
        anhXa();
        showDuLieuDocGia();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manHinhChinh.nextQLDocGia();
            }
        });
        xoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Xóa đọc giả");
                builder.setNegativeButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences lay_ma_dg = getContext().getSharedPreferences("lay_ma_dg", Context.MODE_PRIVATE);
                        int ma_doc_gia = lay_ma_dg.getInt("ma_dg", -1);
                        database.xoaDocGia(ma_doc_gia);
                        Toast.makeText(getActivity(), "Đã xóa", Toast.LENGTH_LONG).show();
                        manHinhChinh.nextQLDocGia();
                    }
                });
                builder.setPositiveButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.create().show();
            }
        });
        sua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manHinhChinh.nextUpdateDocGia();
            }
        });
        return view;
    }
    public void anhXa()
    {
        tv_ma_dg = (TextView) view.findViewById(R.id.detail_dg_ma_dg);
        name = (TextView) view.findViewById(R.id.detail_dg_name);
        email = (TextView) view.findViewById(R.id.detail_dg_email);
        phone = (TextView) view.findViewById(R.id.detail_dg_phone);
        address = (TextView) view.findViewById(R.id.detail_dg_address);

        back = (ImageView) view.findViewById(R.id.detail_dg_back);
        avartar = (ImageView) view.findViewById(R.id.detail_dg_avartar);

        xoa = (Button) view.findViewById(R.id.detail_dg_xoa);
        sua = (Button) view.findViewById(R.id.detail_dg_sua);
    }
    public DocGia layDuLieu()
    {
        SharedPreferences lay_ma_dg = getContext().getSharedPreferences("lay_ma_dg", Context.MODE_PRIVATE);
        int ma_doc_gia = lay_ma_dg.getInt("ma_dg", -1);
        Cursor cursor = database.layDuLieuDGByID(ma_doc_gia);
        DocGia docGia = new DocGia();
        if (cursor.moveToFirst())
        {
            int ma_dg_index = cursor.getColumnIndex(DBHelper.MA_DOC_GIA);
            int ten_dg_index = cursor.getColumnIndex(DBHelper.TEN_DOC_GIA);
            int email_dg_index = cursor.getColumnIndex(DBHelper.EMAIL_DOC_GIA);
            int phone_dg_index = cursor.getColumnIndex(DBHelper.PHONE_DOC_GIA);
            int address_dg_index = cursor.getColumnIndex(DBHelper.ADDRESS_DOC_GIA);
            int avartar_dg_index = cursor.getColumnIndex(DBHelper.IMAGE_DOC_GIA);

            docGia.setMa_doc_gia(cursor.getInt(ma_dg_index));
            docGia.setTen_doc_gia(cursor.getString(ten_dg_index));
            docGia.setEmail_doc_gia(cursor.getString(email_dg_index));
            docGia.setPhone_doc_gia(cursor.getString(phone_dg_index));
            docGia.setAddress_doc_gia(cursor.getString(address_dg_index));
            docGia.setImage_doc_gia(cursor.getBlob(avartar_dg_index));

            cursor.close();
        }
        return docGia;
    }
    public void showDuLieuDocGia()
    {
        DocGia docGia = layDuLieu();
        //
        tv_ma_dg.setText(Integer.toString(docGia.getMa_doc_gia()));
        name.setText(docGia.getTen_doc_gia());
        email.setText(docGia.getEmail_doc_gia());
        phone.setText(docGia.getPhone_doc_gia());
        address.setText(docGia.getAddress_doc_gia());

        byte[] bytes = docGia.getImage_doc_gia();
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        avartar.setImageBitmap(bitmap);
    }
}
