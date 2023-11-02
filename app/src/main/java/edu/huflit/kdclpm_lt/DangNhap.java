package edu.huflit.kdclpm_lt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import edu.huflit.kdclpm_lt.Object.User;
import edu.huflit.kdclpm_lt.SQLite.MyDatabase;

public class DangNhap extends AppCompatActivity {
    Button btn_login;
    MyDatabase database;
    EditText username, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        database = new MyDatabase(this);
        anhXa();
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dangNhap();
            }
        });
    }
    public void anhXa()
    {
        username = (EditText) findViewById(R.id.login_username2);
        password = (EditText) findViewById(R.id.login_pass2);
        btn_login = (Button) findViewById(R.id.login_login2);
    }
    //CODE KHI ẤN BUTTON ĐĂNG NHẬP
    public void dangNhap()
    {
        if (username.getText().toString().trim().length() == 0)
        {
            String p = "<font color='#FF0000'>Chưa nhập username</font>";
            username.setHint(Html.fromHtml(p));
            return;
        }
        if (password.getText().toString().trim().length() == 0)
        {
            String p = "<font color='#FF0000'>Chưa nhập password</font>";
            password.setHint(Html.fromHtml(p));
            return;
        }
        boolean check = database.checkLogin(username.getText().toString().trim(), password.getText().toString().trim());
        if (check == false)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(DangNhap.this);
            builder.setMessage("Tài khoản hoặc mật khẩu không chính xác");
            builder.setNegativeButton("Đóng", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            builder.create().show();
            return;
        }
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username.getText().toString().trim());
        editor.putBoolean("is_login", true);
        editor.apply();

        Intent intent = new Intent(getApplicationContext(), ManHinhChinh.class);
        startActivity(intent);

    }
}