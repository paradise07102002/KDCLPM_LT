package edu.huflit.kdclpm_lt.NhanVien;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import edu.huflit.kdclpm_lt.ManHinhChinh;
import edu.huflit.kdclpm_lt.Object.User;
import edu.huflit.kdclpm_lt.R;
import edu.huflit.kdclpm_lt.SQLite.DBHelper;
import edu.huflit.kdclpm_lt.SQLite.MyDatabase;

public class Add_NhanVien extends Fragment {
    View view;
    ManHinhChinh manHinhChinh;
    MyDatabase database;
    EditText username, pass, re_pass, fullname, email, phone;
    Spinner spinner;
    ImageView back, avartar;
    Button add_nv;
    private static final int PICK_IMAGE_REQUEST = 1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_nhan_vien, container, false);
        database = new MyDatabase(getActivity());
        manHinhChinh = (ManHinhChinh) getActivity();
        anhXa();
        showSpinner();

        add_nv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkInput();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manHinhChinh.nextMHNhanVien();
            }
        });
        avartar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImageFromGallery();
            }
        });
        return view;
    }
    public void anhXa()
    {
        username = (EditText) view.findViewById(R.id.add_nv_username);
        pass = (EditText) view.findViewById(R.id.add_nv_password);
        re_pass = (EditText) view.findViewById(R.id.add_nv_repassword);
        fullname = (EditText) view.findViewById(R.id.add_nv_fullname);
        email = (EditText) view.findViewById(R.id.add_nv_email);
        phone = (EditText) view.findViewById(R.id.add_nv_phone);
        add_nv = (Button) view.findViewById(R.id.add_nv_btn);
        back = (ImageView) view.findViewById(R.id.add_nv_back);
        spinner = (Spinner) view.findViewById(R.id.add_nv_spinner);
        avartar = (ImageView) view.findViewById(R.id.add_nv_avartar);
    }
    public void showSpinner()
    {
        List<String> list_role = new ArrayList<>();
        list_role.add("Librarian");
        //Tạo Adapter cho spinner
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, list_role);
        //Đặt adapter cho spinner
        spinner.setAdapter(arrayAdapter);
    }
    public void checkInput()
    {
        if(username.length() == 0)
        {
            String p = "<font color='#FF0000'>Username không được để trống!</font>";
            username.setHint(Html.fromHtml(p));
        }
        else if (pass.length() == 0)
        {
            String p = "<font color='#FF0000'>Password không được để trống!</font>";
            pass.setHint(Html.fromHtml(p));
        }
        else if (pass.length() < 8)
        {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Mật khẩu tối thiểu 8 kí tự");
                builder.setNegativeButton("Đóng", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.create().show();
        }
        else if (re_pass.length() == 0)
        {
            String p = "<font color='#FF0000'>Re-Password không được để trống!</font>";
            re_pass.setHint(Html.fromHtml(p));
        }
        else if (fullname.length() == 0)
        {
            String p = "<font color='#FF0000'>Họ tên không được để trống!</font>";
            fullname.setHint(Html.fromHtml(p));
        }
        else if (email.length() == 0)
        {
            String p = "<font color='#FF0000'>Email không được để trống!</font>";
            email.setHint(Html.fromHtml(p));
        }
        else if (checkEmail(email.getText().toString().trim()) == false)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Email không hợp lệ");
            builder.setNegativeButton("Đóng", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            builder.create().show();
        }
        else if (phone.length() == 0)
        {
            String p = "<font color='#FF0000'>Số điện thoại không được để trống!</font>";
            phone.setHint(Html.fromHtml(p));
        }
        else if (phone.length() != 10)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Số điện thoại không hợp lệ");
            builder.setNegativeButton("Đóng", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            builder.create().show();
        } else if (checkPass() == false) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Mật khẩu không trùng khớp");
            builder.setNegativeButton("Đóng", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            builder.create().show();
        }
        else if (checkUsername())
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Username đã tồn tại");
            builder.setNegativeButton("Đóng", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            builder.create().show();
        }
        else
        {
            addUser();
        }
    }
    public boolean checkPass()
    {
        String edt_pass, edt_re_pass;
        edt_pass = pass.getText().toString().trim();
        edt_re_pass = re_pass.getText().toString().trim();
        if (edt_pass.equals(edt_re_pass))
        {
            return true;
        }
        return false;
    }
    public boolean checkUsername()
    {
        boolean check = database.checkUsername(username.getText().toString().trim());
        if (check)
        {
            return true;
        }
        return false;
    }
    public User layDuLieu()
    {
        //Ngược lại là mã sách chưa tồn tại--> Thêm thông tin sách vào Sach

        //Khai báo/khởi tạo biến sach kiểu Sach
        User user = new User();
        //Dùng set để gán dữ liệu cho biến sach
        user.setUsername_user(username.getText().toString().trim());
        user.setPassword_user(pass.getText().toString().trim());
        user.setFullname_user(fullname.getText().toString().trim());
        user.setEmail_user(email.getText().toString().trim());
        user.setPhone_user(phone.getText().toString().trim());
        user.setRole_user((String) spinner.getSelectedItem());
        byte[] imageByteArray = convertImageToByArray(avartar);
        user.setAvartar_user(imageByteArray);
        //Trả về loaisach chứa các thông tin của loại sách
        return user;
    }
    public void addUser()
    {
        User user = layDuLieu();
        database.addUser(user);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Đăng ký thành công");
        builder.setNegativeButton("Đến đăng nhập", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setPositiveButton("Quay về", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }
    public boolean checkEmail(String emaill)
    {
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(emaill);
        return matcher.matches();
    }
    //Lấy hình ảnh
    private void loadImageFromUri(Uri uri) {
        Picasso.with(getActivity()).load(uri).into(avartar);
    }
    // Hàm để chuyển đổi ảnh từ ImageView thành mảng byte
    private byte[] convertImageToByArray(ImageView imageView)
    {
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        //nén ảnh nhỏ lại
        bitmap = Bitmap.createScaledBitmap(bitmap, 50, 50, true);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
        return stream.toByteArray();
    }
    //Click vào imageview mở thư viện chọn ảnh
    private void pickImageFromGallery()
    {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    //Lấy ảnh từ thư viện hiển thị lên imageview
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK  && data != null)
        {
            Uri uri = data.getData();
            loadImageFromUri(uri);
        }
    }
}
