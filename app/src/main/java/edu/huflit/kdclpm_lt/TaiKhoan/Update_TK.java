package edu.huflit.kdclpm_lt.TaiKhoan;

import android.content.Context;
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
import androidx.fragment.app.Fragment;

import edu.huflit.kdclpm_lt.ManHinhChinh;
import edu.huflit.kdclpm_lt.Object.User;
import edu.huflit.kdclpm_lt.R;
import edu.huflit.kdclpm_lt.SQLite.DBHelper;
import edu.huflit.kdclpm_lt.SQLite.MyDatabase;

public class Update_TK extends Fragment {
    View view;
    EditText name, email, phone;
    ImageView back;
    Button update;
    MyDatabase database;
    ManHinhChinh manHinhChinh;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.update_tai_khoan, container, false);
        database = new MyDatabase(getActivity());
        manHinhChinh = (ManHinhChinh) getActivity();
        anhXa();
        showThongTin();
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                capNhat();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manHinhChinh.nextTaiKhoan();
            }
        });
        return view;
    }
    public void anhXa()
    {
        name = (EditText) view.findViewById(R.id.update_tk_name);
        email = (EditText) view.findViewById(R.id.update_tk_email);
        phone = (EditText) view.findViewById(R.id.update_tk_phone);
        update = (Button) view.findViewById(R.id.update_tk_update);

        back = (ImageView) view.findViewById(R.id.update_tk_back);
    }
    public void capNhat()
    {
        if (name.getText().toString().trim().length() == 0)
        {
            String p = "<font color='#FF0000'>Họ tên không được để trống!</font>";
            name.setHint(Html.fromHtml(p));
            return;
        } else if (email.getText().toString().trim().length() == 0) {
            String p = "<font color='#FF0000'>Email không được để trống!</font>";
            email.setHint(Html.fromHtml(p));
            return;
        } else if (phone.getText().toString().trim().length() == 0) {
            String p = "<font color='#FF0000'>Số điện thoại không được để trống!</font>";
            phone.setHint(Html.fromHtml(p));
            return;
        }
        //Lấy thông tin đăng nhập
        SharedPreferences get_user = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        User user = new User();
        user.setFullname_user(name.getText().toString().trim());
        user.setEmail_user(email.getText().toString().trim());
        user.setPassword_user(phone.getText().toString().trim());
        user.setUsername_user(get_user.getString("username", null));
        database.updateUser(user);
        Toast.makeText(getActivity(), "Cập nhật thành công", Toast.LENGTH_LONG).show();
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

            name.setText(cursor.getString(name_index));
            phone.setText(cursor.getString(phone_index));
            email.setText(cursor.getString(email_index));
            cursor.close();
        }
    }
}
