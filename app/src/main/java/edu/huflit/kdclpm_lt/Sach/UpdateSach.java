package edu.huflit.kdclpm_lt.Sach;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class UpdateSach extends Fragment {
    View view;
    ImageView back;
    Button update_sach;
    MyDatabase database;
    ManHinhChinh manHinhChinh;
    Spinner spinner;
    EditText ten_sach, ten_tg, nha_xb, nam_xb, mo_ta_sach;
    ImageView img_sach;
    private List<String> list_dau_sach;
    private static final int PICK_IMAGE_REQUEST = 1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.update_sach, container, false);
        database = new MyDatabase(getActivity());
        manHinhChinh = (ManHinhChinh) getActivity();
        anhXa();
        SharedPreferences lay_id_dau_sach = getActivity().getSharedPreferences("lay_ma_sach", Context.MODE_PRIVATE);
        int ma_sach = lay_id_dau_sach.getInt("ma_sach", 0);

        Cursor cursor = database.layDuLieuSachByID(ma_sach);
        if (cursor != null)
        {
            int ten_sach_index = cursor.getColumnIndex(DBHelper.TEN_SACH_S);
            int ten_tg_index = cursor.getColumnIndex(DBHelper.TAC_GIA_S);
            int nha_xb_index = cursor.getColumnIndex(DBHelper.NHA_XUAT_BAN_S);
            int nam_xb_index = cursor.getColumnIndex(DBHelper.NAM_XUAT_BAN_S);
            int mo_ta_index = cursor.getColumnIndex(DBHelper.MO_TA_SACH);
            int image_sach_index = cursor.getColumnIndex(DBHelper.IMAGE_SACH);

            cursor.moveToFirst();
            ten_sach.setText(cursor.getString(ten_sach_index));
            ten_tg.setText(cursor.getString(ten_tg_index));
            nha_xb.setText(cursor.getString(nha_xb_index));
            nam_xb.setText(Integer.toString(cursor.getInt(nam_xb_index)));
            mo_ta_sach.setText(cursor.getString(mo_ta_index));
            byte[] bytes = cursor.getBlob(image_sach_index);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            img_sach.setImageBitmap(bitmap);
        }
        cursor.close();

        img_sach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImageFromGallery();
            }
        });
        update_sach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (kiemTraNhapThongTin() == false)
                {
                }
                else
                {
                    //
                    byte[] imageByteArray = convertImageToByArray(img_sach);
                    //Lấy tên đầu sách từ spinner
                    String ten_ds = (String) spinner.getSelectedItem();
                    //Lấy mã đầu sách từ tên đầu sách trên spinner
                    LoaiSach loaiSach = database.getMaDauSachByTenDS(ten_ds);

                    SharedPreferences lay_ma_sach = getActivity().getSharedPreferences("lay_ma_sach", Context.MODE_PRIVATE);
                    int ma_sach = lay_ma_sach.getInt("ma_sach", 0);
                    Sach sach = new Sach();
                    sach.setMa_sach_s(ma_sach);
                    sach.setTen_sach_s(ten_sach.getText().toString().trim());
                    sach.setTac_gia_s(ten_tg.getText().toString().trim());
                    sach.setNha_xuat_ban_s(nha_xb.getText().toString().trim());
                    sach.setNam_xuat_ban_s(Integer.parseInt(nam_xb.getText().toString().trim()));
                    sach.setMo_ta_sach(mo_ta_sach.getText().toString().trim());
                    sach.setMa_loai_sach_s(loaiSach.getMa_loai_sach_ls());
                    sach.setImage_sach(imageByteArray);

                    database.suaSach(sach);
                    Toast.makeText(getActivity(), "Cập nhật thành công", Toast.LENGTH_LONG).show();
                    manHinhChinh.nextQLSach();
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
        back = (ImageView) view.findViewById(R.id.update_sach_back);
        update_sach = (Button) view.findViewById(R.id.update_sach_btn);

        img_sach = (ImageView) view.findViewById(R.id.update_sach_img);
        ten_sach = (EditText) view.findViewById(R.id.update_sach_name);
        ten_tg = (EditText) view.findViewById(R.id.update_sach_tg);
        nha_xb = (EditText) view.findViewById(R.id.update_sach_nhaxb);
        nam_xb = (EditText) view.findViewById(R.id.update_sach_namxb);
        mo_ta_sach = (EditText) view.findViewById(R.id.update_sach_mo_ta);

        spinner = (Spinner) view.findViewById(R.id.update_sach_spinner);
    }
    public boolean kiemTraNhapThongTin()
    {
        //mds, ts, tg, nhaxb, namxb, tt;
        if (ten_sach.getText().toString().trim().length() == 0)
        {
            String p = "<font color='#FF0000'>Không được để trống!</font>";
            ten_sach.setHint(Html.fromHtml(p));
            return false;
        }
        else if (ten_tg.getText().toString().trim().length() == 0)
        {
            String p = "<font color='#FF0000'>Không được để trống!</font>";
            ten_tg.setHint(Html.fromHtml(p));
            return false;
        } else if (nha_xb.getText().toString().trim().length() == 0)
        {
            String p = "<font color='#FF0000'>Không được để trống!</font>";
            nha_xb.setHint(Html.fromHtml(p));
            return false;
        } else if (nam_xb.getText().toString().trim().length() == 0)
        {
            String p = "<font color='#FF0000'>Không được để trống!</font>";
            nam_xb.setHint(Html.fromHtml(p));
            return false;
        }
        return true;
    }
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
    private byte[] convertImageToByArray(ImageView imageView)
    {
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        //nén ảnh nhỏ lại
        bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
    private void loadImageFromUri(Uri uri) {
        Picasso.with(getActivity()).load(uri).into(img_sach);
    }
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
