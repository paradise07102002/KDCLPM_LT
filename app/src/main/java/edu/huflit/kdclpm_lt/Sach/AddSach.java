package edu.huflit.kdclpm_lt.Sach;

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

import edu.huflit.kdclpm_lt.ManHinhChinh;
import edu.huflit.kdclpm_lt.Object.LoaiSach;
import edu.huflit.kdclpm_lt.Object.Sach;
import edu.huflit.kdclpm_lt.R;
import edu.huflit.kdclpm_lt.SQLite.DBHelper;
import edu.huflit.kdclpm_lt.SQLite.MyDatabase;

public class AddSach extends Fragment {
    View view;
    ManHinhChinh manHinhChinh;
    MyDatabase database;
    EditText ten_sach, ten_tg, nha_xb, nam_xb, mo_ta_sach;
    Button btn_add_sach;
    ImageView back;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView img_add_sach;
    //Hiển thị danh sách đầu sách lên spinner
    private Spinner spinner;
    private List<String> list_dau_sach;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_sach, container, false);
        manHinhChinh = (ManHinhChinh) getActivity();
        database = new MyDatabase(getActivity());
        anhXa();
        img_add_sach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImageFromGallery();
            }
        });
        btn_add_sach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinner.getSelectedItem() == null)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Hiện chưa có đầu sách");
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
                    kiemTraNhapThongTin();
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manHinhChinh.nextQLSach();
            }
        });
        showSpinner();
        return view;
    }
    public void anhXa()
    {
        img_add_sach = (ImageView) view.findViewById(R.id.add_sach_img);
        spinner = (Spinner) view.findViewById(R.id.add_sach_spinner);
        btn_add_sach  = (Button) view.findViewById(R.id.add_sach_btn);

        ten_sach = (EditText) view.findViewById(R.id.add_sach_name);
        ten_tg = (EditText) view.findViewById(R.id.add_sach_tg);
        nha_xb = (EditText) view.findViewById(R.id.add_sach_nhaxb);
        nam_xb = (EditText) view.findViewById(R.id.add_sach_namxb);
        mo_ta_sach = (EditText) view.findViewById(R.id.add_sach_mo_ta);
        back = (ImageView) view.findViewById(R.id.add_sach_back);
    }
    private void loadImageFromUri(Uri uri) {
        Picasso.with(getActivity()).load(uri).into(img_add_sach);
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
    //Lấy tên đầu sách show lên spinner
    public void showSpinner()
    {
        Cursor cursor = database.layFullDuLieuDauSach();
        //Khởi tạo ArrayList
        list_dau_sach = new ArrayList<>();
        //Nếu có dữ liệu -> Duyệt qua cursor
        if (cursor != null)
        {
            int ten_dau_sach_index = cursor.getColumnIndex(DBHelper.TEN_LOAI_SACH_LS);
            //Duyệt qua cursor
            while (cursor.moveToNext())
            {
                list_dau_sach.add(cursor.getString(ten_dau_sach_index));
            }
            //Đóng cursor để giải phóng
            cursor.close();
        }
        //Tạo Adapter cho spinner
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, list_dau_sach);
        //Đặt adapter cho spinner
        spinner.setAdapter(arrayAdapter);
    }
    //Các bước thêm sách
    public void kiemTraNhapThongTin()
    {
        if(ten_sach.length() == 0)
        {
            String p = "<font color='#FF0000'>Tên sách không được để trống!</font>";
            ten_sach.setHint(Html.fromHtml(p));
        }
        else if (ten_tg.length() == 0)
        {
            String pass = "<font color='#FF0000'>Tên tác giả không được để trống!</font>";
            ten_tg.setHint(Html.fromHtml(pass));
        }
        else if (nha_xb.length() == 0)
        {
            String pass = "<font color='#FF0000'>Nhà xuất bản không được để trống!</font>";
            nha_xb.setHint(Html.fromHtml(pass));
        }
        else if (nam_xb.length() == 0) {
            String p = "<font color='#FF0000'>Tên tác giả không được để trống!</font>";
            nam_xb.setHint(Html.fromHtml(p));
        }
        else
        {
            addBook();
        }
    }

    //Lấy thông tin từ EditText và lưu vào các biến/ Sau đó thêm vào Sach
    public Sach layDuLieu()
    {
        //Lấy ảnh
        byte[] imageByteArray = convertImageToByArray(img_add_sach);
        //Lấy tên đầu sách từ spinner
        String ten_ds = (String) spinner.getSelectedItem();
        //Lấy mã đầu sách từ tên đầu sách trên spinner
        LoaiSach loaiSach = database.getMaDauSachByTenDS(ten_ds);

        //Khai báo/khởi tạo biến sach kiểu Sach
        Sach sach = new Sach();
        //Dùng set để gán dữ liệu cho biến sach
        sach.setMa_loai_sach_s(loaiSach.getMa_loai_sach_ls());
        sach.setTen_sach_s(ten_sach.getText().toString().trim());
        sach.setTac_gia_s(ten_tg.getText().toString().trim());
        sach.setNha_xuat_ban_s(nha_xb.getText().toString().trim());
        sach.setMo_ta_sach(mo_ta_sach.getText().toString().trim());
        sach.setNam_xuat_ban_s(Integer.parseInt(nam_xb.getText().toString().trim()));
        sach.setImage_sach(imageByteArray);
        //Trả về sach chứa các thông tin của sách
        return sach;
    }
    //Thêm sách

    public void addBook()
    {
        //Khai báo/khởi tạo sách và gọi hàm layDuLieu để thêm thông tin chp sách
        Sach sach = layDuLieu();//Hàm lấy dữ liệu retrun về một biến tên sach-sau đó gán vào biến tên sach trong hàm này-lúc này sach sẽ có các thông tin từ người dùng nhập
        if(sach != null)
        {
            database.addBook(sach);//Gọi hàm addBook từ Mydatabase để thêm sách
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Thêm sách");
            builder.setMessage("Thêm sách thành công");
            builder.setNegativeButton("Tiếp tục thêm sách", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            builder.setPositiveButton("Quay về", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    manHinhChinh.nextQLSach();
                }
            });
            builder.create().show();
        }
    }
}
