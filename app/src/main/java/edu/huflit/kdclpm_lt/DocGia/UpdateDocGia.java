package edu.huflit.kdclpm_lt.DocGia;

import android.content.Context;
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
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import edu.huflit.kdclpm_lt.ManHinhChinh;
import edu.huflit.kdclpm_lt.Object.DocGia;
import edu.huflit.kdclpm_lt.Object.LoaiSach;
import edu.huflit.kdclpm_lt.Object.Sach;
import edu.huflit.kdclpm_lt.R;
import edu.huflit.kdclpm_lt.SQLite.DBHelper;
import edu.huflit.kdclpm_lt.SQLite.MyDatabase;

public class UpdateDocGia extends Fragment {
    View view;
    MyDatabase database;
    ManHinhChinh manHinhChinh;
    ImageView back, avartar;
    EditText name, email, phone, address;
    Button update;
    TextView tv_ma_dg;
    private static final int PICK_IMAGE_REQUEST = 1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.update_doc_gia, container, false);
        database = new MyDatabase(getActivity());
        manHinhChinh = (ManHinhChinh) getActivity();
        anhXa();
        anhXa();
        SharedPreferences lay_id_dau_sach = getActivity().getSharedPreferences("lay_ma_dg", Context.MODE_PRIVATE);
        int ma_sach = lay_id_dau_sach.getInt("ma_dg", 0);

        Cursor cursor = database.layDuLieuDGByID(ma_sach);
        if (cursor.moveToFirst())
        {
            int ma_dg_index = cursor.getColumnIndex(DBHelper.MA_DOC_GIA);
            int ten_dg_index = cursor.getColumnIndex(DBHelper.TEN_DOC_GIA);
            int email_dg_index = cursor.getColumnIndex(DBHelper.EMAIL_DOC_GIA);
            int phone_dg_index = cursor.getColumnIndex(DBHelper.PHONE_DOC_GIA);
            int address_dg_index = cursor.getColumnIndex(DBHelper.ADDRESS_DOC_GIA);
            int avartar_dg_index = cursor.getColumnIndex(DBHelper.IMAGE_DOC_GIA);

            tv_ma_dg.setText(cursor.getString(ma_dg_index));
            name.setText(cursor.getString(ten_dg_index));
            email.setText(cursor.getString(email_dg_index));
            phone.setText(cursor.getString(phone_dg_index));
            address.setText(cursor.getString(address_dg_index));
            byte[] bytes = cursor.getBlob(avartar_dg_index);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            avartar.setImageBitmap(bitmap);
        }
        cursor.close();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manHinhChinh.nextQLDocGia();
            }
        });
        avartar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImageFromGallery();
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
                    //
                    byte[] imageByteArray = convertImageToByArray(avartar);

                    SharedPreferences lay_ma_sach = getActivity().getSharedPreferences("lay_ma_dg", Context.MODE_PRIVATE);
                    int ma_dg = lay_ma_sach.getInt("ma_dg", 0);
                    DocGia docGia = new DocGia();
                    docGia.setMa_doc_gia(ma_dg);
                    docGia.setTen_doc_gia(name.getText().toString().trim());
                    docGia.setEmail_doc_gia(email.getText().toString().trim());
                    docGia.setPhone_doc_gia(phone.getText().toString().trim());
                    docGia.setAddress_doc_gia(address.getText().toString().trim());
                    docGia.setImage_doc_gia(imageByteArray);

                    database.suaDocGia(docGia);
                    Toast.makeText(getActivity(), "Cập nhật thành công", Toast.LENGTH_LONG).show();
                    manHinhChinh.nextQLDocGia();
                }
            }
        });
        return view;
    }
    public void anhXa()
    {
        tv_ma_dg = (TextView) view.findViewById(R.id.update_dg_ma_dg);

        back = (ImageView) view.findViewById(R.id.update_doc_gia_back);
        avartar = (ImageView) view.findViewById(R.id.update_dg_avartar);

        name = (EditText) view.findViewById(R.id.update_dg_name);
        email = (EditText) view.findViewById(R.id.update_dg_email);
        phone = (EditText) view.findViewById(R.id.update_dg_phone);
        address = (EditText) view.findViewById(R.id.update_dg_address);

        update = (Button) view.findViewById(R.id.update_dg_btn);
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
        } else if (phone.getText().toString().trim().length() == 0)
        {
            String p = "<font color='#FF0000'>Không được để trống!</font>";
            phone.setHint(Html.fromHtml(p));
            return false;
        } else if (address.getText().toString().trim().length() == 0)
        {
            String p = "<font color='#FF0000'>Không được để trống!</font>";
            address.setHint(Html.fromHtml(p));
            return false;
        }
        return true;
    }
}
