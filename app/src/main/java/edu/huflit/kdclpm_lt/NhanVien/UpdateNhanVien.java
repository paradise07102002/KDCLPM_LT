package edu.huflit.kdclpm_lt.NhanVien;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.huflit.kdclpm_lt.ManHinhChinh;
import edu.huflit.kdclpm_lt.Object.User;
import edu.huflit.kdclpm_lt.R;
import edu.huflit.kdclpm_lt.SQLite.DBHelper;
import edu.huflit.kdclpm_lt.SQLite.MyDatabase;

public class UpdateNhanVien extends Fragment {
    View view;
    ManHinhChinh manHinhChinh;
    MyDatabase database;
    ImageView back, avartar;
    Button update;
    EditText name, email, phone;
    TextView role, ma_nv;
    private static final int PICK_IMAGE_REQUEST = 1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.update_nhan_vien, container, false);
        manHinhChinh = (ManHinhChinh) getActivity();
        database = new MyDatabase(getActivity());
        anhXa();
        showDuLieuNhanVien();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manHinhChinh.nextMHNhanVien();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (kiemTraNhapThongTin() == false)
                {

                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Xác nhận thay đổi");
                    builder.setNegativeButton("Cập nhật", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            User user = new User();
                            user.setId_user(Integer.parseInt(ma_nv.getText().toString().trim()));
                            user.setFullname_user(name.getText().toString().trim());
                            user.setEmail_user(email.getText().toString().trim());
                            user.setPhone_user(phone.getText().toString().trim());
                            byte[] imageByteArray = convertImageToByArray(avartar);
                            user.setAvartar_user(imageByteArray);
                            database.suaUser(user);
                            Toast.makeText(getActivity(), "Cập nhật thành công", Toast.LENGTH_LONG).show();
                            manHinhChinh.nextMHNhanVien();
                        }
                    });
                    builder.setPositiveButton("Hủy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            Toast.makeText(getActivity(), "Đã hủy", Toast.LENGTH_LONG).show();
                            manHinhChinh.nextMHNhanVien();
                        }
                    });
                    builder.create().show();
                }
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
        back = (ImageView) view.findViewById(R.id.update_nv_back);
        avartar = (ImageView) view.findViewById(R.id.update_nv_avartar);
        update = (Button) view.findViewById(R.id.update_nv_sua);

        name = (EditText) view.findViewById(R.id.update_nv_name);
        email = (EditText) view.findViewById(R.id.update_nv_email);
        phone = (EditText) view.findViewById(R.id.update_nv_phone);

        role = (TextView) view.findViewById(R.id.update_nv_role);
        ma_nv = (TextView) view.findViewById(R.id.update_nv_id);
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
    public boolean kiemTraNhapThongTin()
    {
        //mds, ts, tg, nhaxb, namxb, tt;
        if (name.getText().toString().trim().length() == 0)
        {
            String p = "<font color='#FF0000'>Không được để trống!</font>";
            name.setHint(Html.fromHtml(p));
            return false;
        }
        else if (email.getText().toString().trim().length() == 0)
        {
            String p = "<font color='#FF0000'>Không được để trống!</font>";
            email.setHint(Html.fromHtml(p));
            return false;
        } else if (checkEmail(email.getText().toString().trim()) == false) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Email không hợp lệ");
            builder.setNegativeButton("Đóng", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            builder.create().show();
            return false;
        } else if (phone.getText().toString().trim().length() == 0)
        {
            String p = "<font color='#FF0000'>Không được để trống!</font>";
            phone.setHint(Html.fromHtml(p));
            return false;
        } else if (phone.length() != 10) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Số điện thoại không hợp lệ");
            builder.setNegativeButton("Đóng", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            builder.create().show();
            return false;
        }
        return true;
    }
    public boolean checkEmail(String emaill)
    {
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(emaill);
        return matcher.matches();
    }
    //
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
