package edu.huflit.kdclpm_lt.DocGia;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import edu.huflit.kdclpm_lt.ManHinhChinh;
import edu.huflit.kdclpm_lt.Object.DocGia;

import edu.huflit.kdclpm_lt.R;
import edu.huflit.kdclpm_lt.SQLite.MyDatabase;

public class AddDocGia extends Fragment {
    View view;
    ManHinhChinh manHinhChinh;
    MyDatabase database;
    EditText name, email, phone, address;
    Button btn_add;
    ImageView back;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView image_doc_gia;
    //Hiển thị danh sách đầu sách lên spinner
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_doc_gia, container, false);
        manHinhChinh = (ManHinhChinh) getActivity();
        database = new MyDatabase(getActivity());
        anhXa();
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kiemTraNhapThongTin();
            }
        });
        image_doc_gia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImageFromGallery();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manHinhChinh.nextQLDocGia();
            }
        });
        return view;
    }
    public void anhXa()
    {
        name = (EditText) view.findViewById(R.id.add_dg_name);
        email = (EditText) view.findViewById(R.id.add_dg_email);
        phone = (EditText) view.findViewById(R.id.add_dg_phone);
        address = (EditText) view.findViewById(R.id.add_dg_address);
        image_doc_gia = (ImageView) view.findViewById(R.id.add_dg_avartar);
        back = (ImageView) view.findViewById(R.id.add_doc_gia_back);

        btn_add = (Button) view.findViewById(R.id.add_dg_btn);
    }
    //Các bước thêm đọc giả
    public void kiemTraNhapThongTin()
    {
        if(name.length() == 0)
        {
            String p = "<font color='#FF0000'>Tên đọc giả không được để trống!</font>";
            name.setHint(Html.fromHtml(p));
        }
        else if (email.length() == 0)
        {
            String pass = "<font color='#FF0000'>Email không được để trống!</font>";
            email.setHint(Html.fromHtml(pass));
        }
        else if (phone.length() == 0)
        {
            String pass = "<font color='#FF0000'>Số điện thoại không được để trống!</font>";
            phone.setHint(Html.fromHtml(pass));
        } else if (address.length() == 0) {
            String pass = "<font color='#FF0000'>Địa chỉ không được để trống!</font>";
            address.setHint(Html.fromHtml(pass));
        } else
        {
            addDocGia();
        }
    }

    //Lấy thông tin từ EditText và lưu vào các biến/ Sau đó thêm vào Sach
    public DocGia layDuLieu()
    {
        //Lấy ảnh
        byte[] imageByteArray = convertImageToByArray(image_doc_gia);
        //Khai báo/khởi tạo biến sach kiểu Sach
        DocGia docGia = new DocGia();
        //Dùng set để gán dữ liệu cho biến sach
        docGia.setTen_doc_gia(name.getText().toString().trim());
        docGia.setTen_doc_gia(email.getText().toString().trim());
        docGia.setTen_doc_gia(phone.getText().toString().trim());
        docGia.setAddress_doc_gia(address.getText().toString().trim());
        docGia.setImage_doc_gia(imageByteArray);
        //Trả về sach chứa các thông tin của sách
        return docGia;
    }
    //Thêm sách

    public void addDocGia()
    {
        DocGia docGia = layDuLieu();
        if(docGia != null)
        {
            database.addDocGia(docGia);
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Thêm đọc giả");
            builder.setMessage("Thêm đọc giả thành công");
            builder.setNegativeButton("Tiếp tục thêm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            builder.setPositiveButton("Quay về", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.create().show();
        }
    }
    //Lấy hình ảnh
    private void loadImageFromUri(Uri uri) {
        Picasso.with(getActivity()).load(uri).into(image_doc_gia);
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
