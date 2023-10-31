package edu.huflit.doanqlthuvien.fragment_sach;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import edu.huflit.doanqlthuvien.DBHelper;
import edu.huflit.doanqlthuvien.ManHinhChinh;
import edu.huflit.doanqlthuvien.ManHinhUser.ManHinhUser;
import edu.huflit.doanqlthuvien.MyAdapter.MyAdapterBinhLuan;
import edu.huflit.doanqlthuvien.MyAdapter.MyAdapterDMSach;
import edu.huflit.doanqlthuvien.MyDatabase;
import edu.huflit.doanqlthuvien.OOP.BinhLuanSach;
import edu.huflit.doanqlthuvien.OOP.LoaiSach;
import edu.huflit.doanqlthuvien.OOP.Sach;
import edu.huflit.doanqlthuvien.OOP.User;
import edu.huflit.doanqlthuvien.R;
import edu.huflit.doanqlthuvien.fragment_admin.ManHinhChinhAdmin;

public class DetailSach extends Fragment {
    View view;
    View background;
    MyDatabase database;
    ManHinhChinh manHinhChinh;
    ImageView back, img_sach;
    TextView dau_sach, ten_sach, tac_gia, nha_xb, nam_xb, trang_thai, mo_ta_sach;

    EditText edt_binh_luan;
    ImageView send;
    public static ListView lv_show_binh_luan;
    public static ArrayList<BinhLuanSach> binhLuanSaches;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.detail_sach, container, false);
        database = new MyDatabase(getActivity());
        manHinhChinh = (ManHinhChinh) getActivity();
        anhXa();
        capNhatBinhLuan();
        setColorTextView();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences get_user = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
                String username = get_user.getString("username", null);
                boolean is_login = get_user.getBoolean("is_login", false);

                if (is_login == false)
                {
                    manHinhChinh.gotoManHinhUser();
                }
                else
                {
                    User user = database.checkRole(username);
                    if (user.getRole_user().equals("admin"))
                    {
                        manHinhChinh.gotoManHinhSach();
                    }
                    else
                    {
                        manHinhChinh.gotoManHinhUser();
                    }
                }
            }
        });
        showDuLieuSach();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences get_user = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
                boolean check_login = get_user.getBoolean("is_login", false);
                if (check_login == false)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Đăng nhập để bình luận");
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
                    SharedPreferences lay_ma_sach = getActivity().getSharedPreferences("lay_ma_sach", Context.MODE_PRIVATE);
                    int ma_sach = lay_ma_sach.getInt("ma_sach", 0);

                    BinhLuanSach binhLuanSach = new BinhLuanSach();

                    String username = get_user.getString("username", null);
                    Cursor cursor = database.getUserByUsername(username);
                    if (cursor != null)
                    {
                        int id_user_index = cursor.getColumnIndex(DBHelper.ID_USER);
                        cursor.moveToFirst();
                        binhLuanSach.setMa_user_binh_luan(cursor.getInt(id_user_index));
                        binhLuanSach.setMa_sach_binh_luan(ma_sach);
                        binhLuanSach.setNoi_dung_binh_luan(edt_binh_luan.getText().toString().trim());
                        database.addBinhLuan(binhLuanSach);

                        Toast.makeText(getActivity(), "Đã gửi bình luận", Toast.LENGTH_LONG).show();
                        edt_binh_luan.setText("");
                        capNhatBinhLuan();
                    }

                }
            }
        });
        return view;
    }
    public void anhXa()
    {
        background = (View) view.findViewById(R.id.background_detail_sach);

        back = (ImageView) view.findViewById(R.id.detail_sach_back);
        img_sach = (ImageView) view.findViewById(R.id.detail_sach_img);

        dau_sach = (TextView) view.findViewById(R.id.detail_sach_spinner);
        ten_sach = (TextView) view.findViewById(R.id.detail_sach_name);
        tac_gia = (TextView) view.findViewById(R.id.detail_sach_tg);
        nha_xb = (TextView) view.findViewById(R.id.detail_sach_nhaxb);
        nam_xb = (TextView) view.findViewById(R.id.detail_sach_namxb);
        trang_thai = (TextView) view.findViewById(R.id.detail_sach_tt);
//        mo_ta_sach = (TextView) view.findViewById(R.id.detail_sach_mo_ta);

        edt_binh_luan = (EditText) view.findViewById(R.id.edt_binh_luan);
        send = (ImageView) view.findViewById(R.id.img_send);
        lv_show_binh_luan = (ListView) view.findViewById(R.id.lv_show_bl);
    }
    public Sach layDuLieu()
    {
        SharedPreferences lay_ma_sach = getActivity().getSharedPreferences("lay_ma_sach", Context.MODE_PRIVATE);
        int ma_sach = lay_ma_sach.getInt("ma_sach", 0);
        Cursor cursor = database.layDuLieuSachByID(ma_sach);
        Sach sach = new Sach();
        if (cursor != null)
        {
            int ma_dau_sach_index = cursor.getColumnIndex(DBHelper.MA_LOAI_SACH_S);

            int ten_sach_index = cursor.getColumnIndex(DBHelper.TEN_SACH_S);
            int ten_tg_index = cursor.getColumnIndex(DBHelper.TAC_GIA_S);
            int nha_xb_index = cursor.getColumnIndex(DBHelper.NHA_XUAT_BAN_S);
            int nam_xb_index = cursor.getColumnIndex(DBHelper.NAM_XUAT_BAN_S);
            int trang_thai_index = cursor.getColumnIndex(DBHelper.TRANG_THAI_S);
            int mo_ta_index = cursor.getColumnIndex(DBHelper.MO_TA_SACH);
            int img_sach_index = cursor.getColumnIndex(DBHelper.IMAGE_SACH);

            cursor.moveToFirst();
            sach.setMa_loai_sach_s(cursor.getInt(ma_dau_sach_index));
            sach.setTen_sach_s(cursor.getString(ten_sach_index));
            sach.setTac_gia_s(cursor.getString(ten_tg_index));
            sach.setNha_xuat_ban_s(cursor.getString(nha_xb_index));
            sach.setNam_xuat_ban_s(cursor.getInt(nam_xb_index));
            sach.setTrang_thai_s(cursor.getInt(trang_thai_index));
            sach.setMo_ta_sach(cursor.getString(mo_ta_index));
            sach.setImage_sach(cursor.getBlob(img_sach_index));

            cursor.close();
        }
        return sach;
    }
    public void showDuLieuSach()
    {
        Sach sach = layDuLieu();
        LoaiSach loaiSach = new LoaiSach();

        //Ta có mã đầu sách, truy vấn lấy tên đầu sách
        String ten_dau_sach = "";
        Cursor cursor = database.layDuLieuDauSachByID(sach.getMa_loai_sach_s());
        if (cursor != null)
        {
            int ten_dau_sach_index = cursor.getColumnIndex(DBHelper.TEN_LOAI_SACH_LS);
            cursor.moveToFirst();
            loaiSach.setLoai_sach_ls(cursor.getString(ten_dau_sach_index));
            cursor.close();
        }
//        Đổ dữ liệu lên TextView
        dau_sach.setText(loaiSach.getLoai_sach_ls());
        ten_sach.setText(sach.getTen_sach_s());
        tac_gia.setText(sach.getTac_gia_s());
        nha_xb.setText(sach.getNha_xuat_ban_s());
        nam_xb.setText(Integer.toString(sach.getNam_xuat_ban_s()));
//        mo_ta_sach.setText(sach.getMo_ta_sach());
        int tt = sach.getTrang_thai_s();
        if (tt == 0)
        {
            trang_thai.setText("Chưa cho mượn");
        }
        else
        {
            trang_thai.setText("Đã cho mượn");
        }
//        byte[] bytes = ManHinhSach.saches.get(i).getImage_sach();
//        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//        imageView = (ImageView) view.findViewById(R.id.dms_image);
//        imageView.setImageBitmap(bitmap);
        byte[] bytes = sach.getImage_sach();
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        img_sach.setImageBitmap(bitmap);
    }
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
                background.setBackgroundColor((int) valueAnimator.getAnimatedValue());
            }
        });
    }
    //CẬP NHẬT BÌNH LUẬN
    public void capNhatBinhLuan()
    {
        if (binhLuanSaches == null)
        {
            binhLuanSaches = new ArrayList<BinhLuanSach>();
        }
        else
        {
            binhLuanSaches.removeAll(binhLuanSaches);
        }
        SharedPreferences lay_ma_sach = getActivity().getSharedPreferences("lay_ma_sach", Context.MODE_PRIVATE);
        int ma_sach = lay_ma_sach.getInt("ma_sach", 0);
        database = new MyDatabase(getActivity());
        Cursor cursor = database.layDuLieuBinhLuanByMaSach(ma_sach);
        if (cursor != null)
        {
            int ma_bl_index = cursor.getColumnIndex(DBHelper.MA_BL);
            int ma_user_index = cursor.getColumnIndex(DBHelper.MA_USER_BL);
            int ma_sach_index = cursor.getColumnIndex(DBHelper.MA_SACH_BL);
            int noi_dung_index = cursor.getColumnIndex(DBHelper.NOI_DUNG_BL);
            while (cursor.moveToNext())
            {
                BinhLuanSach binhLuanSach = new BinhLuanSach();
                if (ma_bl_index != -1)
                {
                    binhLuanSach.setMa_binh_luan(cursor.getInt(ma_bl_index));
                }
                if (ma_user_index != -1)
                {
                    binhLuanSach.setMa_user_binh_luan(cursor.getInt(ma_user_index));
                }
                if (ma_sach_index != -1)
                {
                    binhLuanSach.setMa_sach_binh_luan(cursor.getInt(ma_sach_index));
                }
                if (noi_dung_index != -1)
                {
                    binhLuanSach.setNoi_dung_binh_luan(cursor.getString(noi_dung_index));
                }
                binhLuanSaches.add(binhLuanSach);
            }
        }
        if (binhLuanSaches != null)
        {
            lv_show_binh_luan.setAdapter(new MyAdapterBinhLuan(getActivity()));
        }
        lv_show_binh_luan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

}
