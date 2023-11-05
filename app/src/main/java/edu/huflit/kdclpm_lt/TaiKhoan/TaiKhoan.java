package edu.huflit.kdclpm_lt.TaiKhoan;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
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
import androidx.fragment.app.Fragment;

import edu.huflit.kdclpm_lt.ManHinhChinh;
import edu.huflit.kdclpm_lt.Object.User;
import edu.huflit.kdclpm_lt.R;
import edu.huflit.kdclpm_lt.SQLite.DBHelper;
import edu.huflit.kdclpm_lt.SQLite.MyDatabase;

public class TaiKhoan extends Fragment {
    View view;
    MyDatabase database;
    ManHinhChinh manHinhChinh;
    ImageView avartar, back;
    TextView name, email, phone, username, chuc_vu;
    Button next_update, next_doi_mk;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tai_khoan, container, false);
        database = new MyDatabase(getActivity());
        manHinhChinh = (ManHinhChinh) getActivity();
        anhXa();
        showThongTin();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        next_doi_mk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manHinhChinh.nextDoiMK();
            }
        });
        next_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manHinhChinh.nextUpdateTK();
            }
        });
        return view;
    }
    public void anhXa()
    {
        avartar = (ImageView) view.findViewById(R.id.tk_avartar);
        back = (ImageView) view.findViewById(R.id.tk_back);
        next_update = (Button) view.findViewById(R.id.tk_next_update);
        next_doi_mk = (Button) view.findViewById(R.id.tk_next_doi_mk);

        name = (TextView) view.findViewById(R.id.tk_name);
        email = (TextView) view.findViewById(R.id.tk_email);
        phone = (TextView) view.findViewById(R.id.tk_phone);
        username = (TextView) view.findViewById(R.id.tk_username);
        chuc_vu = (TextView) view.findViewById(R.id.tk_chuc_vu);
    }
    public void showThongTin()
    {
        SharedPreferences get_user = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        Cursor cursor = database.layThongTinTaiKhoan(get_user.getString("username", null));
        if (cursor.moveToFirst())
        {
            int name_index = cursor.getColumnIndex(DBHelper.FULLNAME_USER);
            int phone_index = cursor.getColumnIndex(DBHelper.PHONE_USER);
            int email_index = cursor.getColumnIndex(DBHelper.EMAIL_USER);
            int role_index = cursor.getColumnIndex(DBHelper.ROLE_USER);

            name.setText(cursor.getString(name_index));
            phone.setText(cursor.getString(phone_index));
            email.setText(cursor.getString(email_index));
            chuc_vu.setText(cursor.getString(role_index));
            cursor.close();
        }
    }
}
