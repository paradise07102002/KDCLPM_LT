package edu.huflit.kdclpm_lt.DauSach;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import edu.huflit.kdclpm_lt.ManHinhChinh;
import edu.huflit.kdclpm_lt.Object.LoaiSach;
import edu.huflit.kdclpm_lt.R;
import edu.huflit.kdclpm_lt.SQLite.MyDatabase;

public class AddDauSach extends Fragment {
    View view;
    EditText edt_ten_dau_sach;
    Button btn_add_dau_sach;
    ImageView img_back;
    MyDatabase database;
    Context context;
    String str_ten_dau_sach;
    ManHinhChinh manHinhChinh;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_dau_sach, container, false);
        context = getContext();
        manHinhChinh = (ManHinhChinh) getActivity();
        database = new MyDatabase(context);
        anhXa();
        btn_add_dau_sach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kiemTraNhapThongTin();
            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manHinhChinh.nextQLDauSach();
            }
        });
        setColorTextView();
        return view;
    }
    public void anhXa()
    {
        edt_ten_dau_sach = (EditText) view.findViewById(R.id.edt_ten_dau_sach);
        btn_add_dau_sach = (Button) view.findViewById(R.id.btn_add_dau_sach);
        img_back = (ImageView) view.findViewById(R.id.add_dau_sach_back);
    }
    public void kiemTraNhapThongTin()
    {
        if(edt_ten_dau_sach.length() == 0)
        {
            String p = "<font color='#FF0000'>Không được để trống!</font>";
            edt_ten_dau_sach.setHint(Html.fromHtml(p));
        }
        else
        {
            addLoaiSach();
        }
    }
    public LoaiSach layDuLieu()
    {
        //Lấy dữ liệu EditText thêm vào các biến
        str_ten_dau_sach = edt_ten_dau_sach.getText().toString().trim();

        //Kiểm tra tên đầu sách đã tồn tại hay chưa/ Nếu tồn tại chọn tên đầu sách khác
        if (database.checkTenDauSach(str_ten_dau_sach))//bằng true nghĩa là mã sách đã tồn tại
        {
            edt_ten_dau_sach.setText("");//Xóa các kí tự ở ô mã sách để dòng thông báo ko bị che
            String tb = "<font color='#FF0000'>Mã đầu sách đã tồn tại </font>";
            edt_ten_dau_sach.setHint(Html.fromHtml(tb));
            return null;
        }
        //Khai báo/khởi tạo biến sach kiểu Sach
        LoaiSach loaiSach = new LoaiSach();
        //Dùng set để gán dữ liệu cho biến sach
        loaiSach.setLoai_sach_ls(str_ten_dau_sach);
        //Trả về loaisach chứa các thông tin của loại sách
        return loaiSach;
    }
    public void addLoaiSach()
    {
        //Khai báo/khởi tạo sách và gọi hàm layDuLieu để thêm thông tin chp sách
        LoaiSach loaiSach = layDuLieu();//Hàm lấy dữ liệu retrun về một biến tên sach-sau đó gán vào biến tên sach trong hàm này-lúc này sach sẽ có các thông tin từ người dùng nhập

        if(loaiSach != null)
        {
                database.addLoaiSach(loaiSach);//Gọi hàm addBook từ Mydatabase để thêm sách
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Thêm đầu sách");
                builder.setMessage("Thêm đầu sách thành công");
                builder.setNegativeButton("Tiếp tục thêm đầu sách", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.setPositiveButton("Quay về", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        manHinhChinh.nextQLDauSach();
                    }
                });
                builder.create().show();
        }
    }
    //TẠO MÀU CHUYỂN ĐỘNG CHO BUTTON
    public void setColorTextView()
    {
        int[] colors = {
                Color.parseColor("#99FFFF"),
                Color.parseColor("#CC00FF"),
                Color.parseColor("#CC99FF"),
                Color.parseColor("#CCFFFF")
        };
        // Tạo một `ValueAnimator` để chuyển đổi màu sắc
        ValueAnimator valueAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), colors[0], colors[1], colors[2], colors[3]);
        valueAnimator.setDuration(6000);
        //lặp lại vô hạn
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        // Bắt đầu `ValueAnimator`
        valueAnimator.start();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator valueAnimator) {
                // Lấy giá trị màu hiện tại
                int color = (int) valueAnimator.getAnimatedValue();
                // Đặt màu cho textview
                btn_add_dau_sach.setBackgroundColor((int) valueAnimator.getAnimatedValue());
            }
        });
    }
}
