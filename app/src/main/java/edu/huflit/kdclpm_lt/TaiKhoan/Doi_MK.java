package edu.huflit.kdclpm_lt.TaiKhoan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import edu.huflit.kdclpm_lt.R;
import edu.huflit.kdclpm_lt.SQLite.MyDatabase;

public class Doi_MK extends Fragment {
    View view;
    MyDatabase database;
    ManHinhChinh manHinhChinh;
    EditText mat_khau_cu, mat_khau_moi, re_mat_khau_moi;
    ImageView back;
    Button doi_mk;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.doi_mk, container, false);
        database = new MyDatabase(getActivity());
        manHinhChinh = (ManHinhChinh) getActivity();
        anhXa();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manHinhChinh.nextTaiKhoan();
            }
        });
        doi_mk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mat_khau_cu.getText().toString().trim().length() == 0)
                {
                    String p = "<font color='#FF0000'>Mật khẩu cũ không được để trống!</font>";
                    mat_khau_cu.setHint(Html.fromHtml(p));
                    return;
                } else if (mat_khau_moi.getText().toString().trim().length() == 0) {
                    String p = "<font color='#FF0000'>Mật khẩu mới không được để trống!</font>";
                    mat_khau_moi.setHint(Html.fromHtml(p));
                    return;
                } else if (re_mat_khau_moi.getText().toString().trim().length() == 0) {
                    String p = "<font color='#FF0000'>Mật khẩu mới không được để trống!</font>";
                    re_mat_khau_moi.setHint(Html.fromHtml(p));
                    return;
                }
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
                //Kiểm tra mật khẩu cũ đúng hay ko
                boolean check_mk = database.checkMK(sharedPreferences.getString("username", null), mat_khau_cu.getText().toString().trim());
                if (check_mk == false)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Mật khẩu cũ không chính xác");
                    builder.setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    builder.create().show();
                    return;//Dừng
                }
                //Nếu mk cũ đúng thì kiểm tra xem mk mới trùng nhau chưa
                String mk1, mk2;
                mk1 = mat_khau_moi.getText().toString().trim();
                mk2 = re_mat_khau_moi.getText().toString().trim();
                boolean so_sanh = mk1.equals(mk2);
                if (so_sanh == false)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Mật khẩu mới không trung khớp");
                    builder.setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    builder.create().show();
                    return;//Dừng
                }
                //Cập nhật mật khẩu mới
                database.doiMK(sharedPreferences.getString("username", null), mk1);
                Toast.makeText(getActivity(), "Đổi mật khẩu thành công", Toast.LENGTH_LONG).show();

            }
        });
        return view;
    }
    public void anhXa()
    {
        mat_khau_cu = (EditText) view.findViewById(R.id.doi_mat_khau1);
        mat_khau_moi = (EditText) view.findViewById(R.id.doi_mat_khau2);
        re_mat_khau_moi = (EditText) view.findViewById(R.id.doi_mat_khau3);

        back = (ImageView) view.findViewById(R.id.doi_mat_khau_back);
        doi_mk = (Button) view.findViewById(R.id.doi_mat_khau_btn);
    }
    public boolean kiemTraNhapThongTin()
    {
        SharedPreferences get_username = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        boolean check_login = get_username.getBoolean("is_login", false);
        if (check_login == false)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Bạn chưa đăng nhập");
            builder.setNegativeButton("Đóng", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            builder.create().show();
            return false;
        }
        if (mat_khau_cu.length() == 0 )
        {
            String p = "<font color='#FF0000'>Không được để trống</font>";
            mat_khau_cu.setHint(Html.fromHtml(p));
            return false;
        } else if (mat_khau_moi.length() == 0)
        {
            String p = "<font color='#FF0000'>Không được để trống</font>";
            mat_khau_moi.setHint(Html.fromHtml(p));
            return false;
        } else if (re_mat_khau_moi.length() == 0) {
            String p = "<font color='#FF0000'>Không được để trống</font>";
            re_mat_khau_moi.setHint(Html.fromHtml(p));
            return false;
        }
        return true;
    }
}
