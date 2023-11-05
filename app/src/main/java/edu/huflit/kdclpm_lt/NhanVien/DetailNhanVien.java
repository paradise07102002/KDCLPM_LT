package edu.huflit.kdclpm_lt.NhanVien;

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
import edu.huflit.kdclpm_lt.Object.User;
import edu.huflit.kdclpm_lt.R;
import edu.huflit.kdclpm_lt.SQLite.DBHelper;
import edu.huflit.kdclpm_lt.SQLite.MyDatabase;

public class DetailNhanVien extends Fragment {
    View view;
    MyDatabase database;
    ManHinhChinh manHinhChinh;
    TextView ma_nv, name, email, phone, role;
    ImageView back, avartar;
    Button delete, update;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.detail_nhan_vien, container, false);
        database = new MyDatabase(getActivity());
        manHinhChinh = (ManHinhChinh) getActivity();
        anhXa();
        showDuLieuNhanVien();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manHinhChinh.nextMHNhanVien();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manHinhChinh.nextUpdateNhanVien();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Xóa nhân viên");
                builder.setNegativeButton("Xác nhận xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        database.xoaUser(Integer.parseInt(ma_nv.getText().toString().trim()));
                        Toast.makeText(getActivity(), "Đã xóa nhân viên", Toast.LENGTH_LONG).show();
                        manHinhChinh.nextMHNhanVien();
                    }
                });
                builder.setPositiveButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getActivity(), "Đã hủy", Toast.LENGTH_LONG).show();
                        manHinhChinh.nextMHNhanVien();
                    }
                });
                builder.create().show();
            }
        });
        return view;
    }
    public void anhXa()
    {
        back = (ImageView) view.findViewById(R.id.detail_nv_back);
        avartar = (ImageView) view.findViewById(R.id.detail_nv_avartar);
        ma_nv = (TextView) view.findViewById(R.id.detail_nv_id);
        name = (TextView) view.findViewById(R.id.detail_nv_name);
        email = (TextView) view.findViewById(R.id.detail_nv_email);
        phone = (TextView) view.findViewById(R.id.detail_nv_phone);
        role = (TextView) view.findViewById(R.id.detail_nv_role);

        delete = (Button) view.findViewById(R.id.detail_nv_xoa);
        update = (Button) view.findViewById(R.id.detail_nv_sua);
    }
    public User layDuLieu()
    {
        SharedPreferences lay_ma_dg = getContext().getSharedPreferences("lay_ma_nv", Context.MODE_PRIVATE);
        int ma_nhan_vien = lay_ma_dg.getInt("ma_nv", -1);
        Cursor cursor = database.layDuLieuNhanVienByID(ma_nhan_vien);
        User user = new User();
        if (cursor.moveToNext())
        {
            int ma_nv_index = cursor.getColumnIndex(DBHelper.ID_USER);
            int ten_nv_index = cursor.getColumnIndex(DBHelper.FULLNAME_USER);
            int email_nv_index = cursor.getColumnIndex(DBHelper.EMAIL_USER);
            int phone_nv_index = cursor.getColumnIndex(DBHelper.PHONE_USER);
            int role_nv_index = cursor.getColumnIndex(DBHelper.ROLE_USER);
            int avartar_nv_index = cursor.getColumnIndex(DBHelper.AVARTAR_USER);

            user.setId_user(cursor.getInt(ma_nv_index));
            user.setFullname_user(cursor.getString(ten_nv_index));
            user.setEmail_user(cursor.getString(email_nv_index));
            user.setPhone_user(cursor.getString(phone_nv_index));
            user.setRole_user(cursor.getString(role_nv_index));
            user.setAvartar_user(cursor.getBlob(avartar_nv_index));
            cursor.close();
        }
        return user;
    }
    public void showDuLieuNhanVien()
    {
        User user = layDuLieu();
        //
        ma_nv.setText(Integer.toString(user.getId_user()));
        name.setText(user.getFullname_user());
        email.setText(user.getEmail_user());
        phone.setText(user.getPhone_user());
        role.setText(user.getRole_user());

        byte[] bytes = user.getAvartar_user();
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        avartar.setImageBitmap(bitmap);
    }
}
