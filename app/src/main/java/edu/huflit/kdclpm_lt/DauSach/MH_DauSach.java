package edu.huflit.kdclpm_lt.DauSach;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import edu.huflit.kdclpm_lt.Adapter.MyAdapterDMDauSach;
import edu.huflit.kdclpm_lt.ManHinhChinh;
import edu.huflit.kdclpm_lt.Object.LoaiSach;
import edu.huflit.kdclpm_lt.R;
import edu.huflit.kdclpm_lt.SQLite.DBHelper;
import edu.huflit.kdclpm_lt.SQLite.MyDatabase;

public class MH_DauSach extends Fragment {
    View view;
    ImageView next_add_dau_sach, img_back;
    TextView tv_thong_bao_null;
    public static ManHinhChinh manHinhChinh;
    MyDatabase database;
    public static Context context;
    public static ListView listView;
    public static ArrayList<LoaiSach> loaiSaches;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mh_dau_sach, container, false);
        context = getContext();
        database = new MyDatabase(context);
        anhXa();
        manHinhChinh = (ManHinhChinh) getActivity();
        capNhatDuLieuDSach();
        //ẤN VÀO BUTTON ĐỂ TỚI MÀN HÌNH THÊM ĐẦU SÁCH
        next_add_dau_sach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manHinhChinh.nextAddDauSach();
            }
        });


        return view;
    }
    public void anhXa()
    {
        next_add_dau_sach = (ImageView) view.findViewById(R.id.next_add_dau_sach);
        img_back = (ImageView) view.findViewById(R.id.man_hinh_dau_sach_back);
        listView = (ListView) view.findViewById(R.id.lv_dau_sach);
        tv_thong_bao_null = (TextView) view.findViewById(R.id.tv_dau_sach_null);
    }
    public void capNhatDuLieuDSach()
    {
        if (loaiSaches == null)
        {
            loaiSaches = new ArrayList<LoaiSach>();
        }
        else
        {
            loaiSaches.removeAll(loaiSaches);
        }
        database = new MyDatabase(context);
        Cursor cursor = database.layDuLieuDauSach();
        if (cursor != null)
        {
            int ten_dau_sach_index = cursor.getColumnIndex(DBHelper.TEN_LOAI_SACH_LS);
            int ma_dau_sach_index = cursor.getColumnIndex(DBHelper.MA_LOAI_SACH_LS);
            while (cursor.moveToNext())
            {
                LoaiSach loaiSach = new LoaiSach();
                if (ten_dau_sach_index != -1)
                {
                    loaiSach.setLoai_sach_ls(cursor.getString(ten_dau_sach_index));
                }
                if (ma_dau_sach_index != -1)
                {
                    loaiSach.setMa_loai_sach_ls(cursor.getInt(ma_dau_sach_index));
                }
                loaiSaches.add(loaiSach);
            }
        }
        if (loaiSaches != null)
        {
            listView.setAdapter(new MyAdapterDMDauSach(context));
        }
        if (listView.getCount()<=0)
        {
            tv_thong_bao_null.setText("RỖNG");
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }
}
