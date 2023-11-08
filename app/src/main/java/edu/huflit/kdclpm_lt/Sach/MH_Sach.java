package edu.huflit.kdclpm_lt.Sach;

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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import edu.huflit.kdclpm_lt.Adapter.MyAdapterDMSach;
import edu.huflit.kdclpm_lt.ManHinhChinh;
import edu.huflit.kdclpm_lt.Object.Sach;
import edu.huflit.kdclpm_lt.R;
import edu.huflit.kdclpm_lt.SQLite.DBHelper;
import edu.huflit.kdclpm_lt.SQLite.MyDatabase;

public class MH_Sach extends Fragment {
    View view;
    ImageView next_add_sach;
    ManHinhChinh manHinhChinh;
    MyDatabase database;
    public static ListView listView;
    public static ArrayList<Sach> saches;
    TextView tv_thong_bao_null;
    //SEARCH
    EditText edt_name_search;
    ImageView img_search;
    Spinner spinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mh_sach, container, false);
        database = new MyDatabase(getActivity());
        saches = new ArrayList<>();
        manHinhChinh = (ManHinhChinh) getActivity();
        anhXa();
        showSpinner();
        next_add_sach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manHinhChinh.nextAddSach();
            }
        });
        capNhatDuLieuDSach();
        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String a = (String) spinner.getSelectedItem();
                if (a.equals("Tên sách"))
                {
                    searchBookName();
                } else if (a.equals("Mã sách")) {
                    searchIDBook();
                } else if (a.equals("Mã đầu sách")) {
                    searchIDDauSach();
                }
            }
        });
        return view;
    }

    public void anhXa() {
//        back = (ImageView) view.findViewById(R.id.man_hinh_sach_back);
        next_add_sach = (ImageView) view.findViewById(R.id.next_add_sach);
        listView = (ListView) view.findViewById(R.id.lv_sach);
        tv_thong_bao_null = (TextView) view.findViewById(R.id.tv_sach_null);

        edt_name_search = (EditText) view.findViewById(R.id.edt_search_name_sach);
        img_search = (ImageView) view.findViewById(R.id.img_search_name_sach);
        spinner = (Spinner) view.findViewById(R.id.mh_sach_spinner);
    }

    public void capNhatDuLieuDSach() {
        if (saches == null) {
            saches = new ArrayList<Sach>();
        } else {
            saches.removeAll(saches);
        }
        database = new MyDatabase(getActivity());
        Cursor cursor = database.layDuLieuSach();
        if (cursor != null) {
            int ten_sach_index = cursor.getColumnIndex(DBHelper.TEN_SACH_S);
            int ma_sach_index = cursor.getColumnIndex(DBHelper.MA_SACH_S);
            int img_sach_index = cursor.getColumnIndex(DBHelper.IMAGE_SACH);
            while (cursor.moveToNext()) {
                Sach sach = new Sach();
                if (ten_sach_index != -1) {
                    sach.setTen_sach_s(cursor.getString(ten_sach_index));
                }
                if (ma_sach_index != -1) {
                    sach.setMa_sach_s(cursor.getInt(ma_sach_index));
                }
                if (img_sach_index != -1) {
                    sach.setImage_sach(cursor.getBlob(img_sach_index));
                }
                saches.add(sach);
            }
        }
        if (saches != null) {
            listView.setAdapter(new MyAdapterDMSach(getActivity()));
        }
        if (listView.getCount() <= 0) {
            tv_thong_bao_null.setText("RỖNG");
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SharedPreferences lay_ma_sach = getActivity().getSharedPreferences("lay_ma_sach", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = lay_ma_sach.edit();
                int ma_sach = saches.get(i).getMa_sach_s();
                editor.putInt("ma_sach", ma_sach);
                editor.apply();

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                Drawable drawable = getResources().getDrawable(R.drawable.icon_question);
                builder.setIcon(drawable);
                builder.setTitle("Sách");
                builder.setNegativeButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                        builder1.setMessage("Bạn muốn xóa?");
                        builder1.setNegativeButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SharedPreferences lay_ma_sach = getActivity().getSharedPreferences("lay_ma_sach", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = lay_ma_sach.edit();
                                database.xoaSach(lay_ma_sach.getInt("ma_sach", -1));
                                Toast.makeText(getActivity(), "Đã xóa sách", Toast.LENGTH_LONG).show();
                                capNhatListView();
                                if (listView.getCount() <= 0) {
                                    tv_thong_bao_null.setText("Sách trống");
                                }
                            }
                        });
                        builder1.setPositiveButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        builder1.create().show();
                    }
                });
                builder.setPositiveButton("Sửa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        manHinhChinh.nextUpdateSach();
                    }
                });
                builder.setNeutralButton("Xem chi tiết", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        manHinhChinh.nextDetailSach();
                    }
                });
                builder.create().show();
            }
        });
    }

    public void capNhatListView() {
        if (saches == null) {
            saches = new ArrayList<Sach>();
        } else {
            saches.removeAll(saches);
        }
        database = new MyDatabase(getActivity());
        Cursor cursor = database.layDuLieuSach();
        if (cursor != null) {
            int ten_sach_index = cursor.getColumnIndex(DBHelper.TEN_SACH_S);
            int ma_sach_index = cursor.getColumnIndex(DBHelper.MA_SACH_S);
            int img_sach_index = cursor.getColumnIndex(DBHelper.IMAGE_SACH);
            while (cursor.moveToNext()) {
                Sach sach = new Sach();
                if (ten_sach_index != -1) {
                    sach.setTen_sach_s(cursor.getString(ten_sach_index));
                }
                if (ma_sach_index != -1) {
                    sach.setMa_sach_s(cursor.getInt(ma_sach_index));
                }
                if (img_sach_index != -1) {
                    sach.setImage_sach(cursor.getBlob(img_sach_index));
                }
                saches.add(sach);
            }
        }
        if (saches != null) {
            listView.setAdapter(new MyAdapterDMSach(getActivity()));
        }
    }

    //Tìm sách
    public void searchBookName()
    {
        if (saches == null) {
            saches = new ArrayList<Sach>();
        } else {
            saches.removeAll(saches);
        }
        database = new MyDatabase(getActivity());
        Cursor cursor = database.searchNameBook(edt_name_search.getText().toString().trim());
        if (cursor != null) {
            int ten_sach_index = cursor.getColumnIndex(DBHelper.TEN_SACH_S);
            int ma_sach_index = cursor.getColumnIndex(DBHelper.MA_SACH_S);
            int img_sach_index = cursor.getColumnIndex(DBHelper.IMAGE_SACH);
            while (cursor.moveToNext()) {
                Sach sach = new Sach();
                if (ten_sach_index != -1) {
                    sach.setTen_sach_s(cursor.getString(ten_sach_index));
                }
                if (ma_sach_index != -1) {
                    sach.setMa_sach_s(cursor.getInt(ma_sach_index));
                }
                if (img_sach_index != -1) {
                    sach.setImage_sach(cursor.getBlob(img_sach_index));
                }
                saches.add(sach);
            }
        }
        if (saches != null) {
            listView.setAdapter(new MyAdapterDMSach(getActivity()));
        }
    }
    public void searchIDBook()
    {
        if (isInteger(edt_name_search.getText().toString().trim()) == false)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Mã sách nhập không hợp lệ");
            builder.setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            builder.create().show();
            return;
        }
        if (saches == null) {
            saches = new ArrayList<Sach>();
        } else {
            saches.removeAll(saches);
        }
        database = new MyDatabase(getActivity());
        Cursor cursor = database.searchIDBook(Integer.parseInt(edt_name_search.getText().toString().trim()));
        if (cursor != null) {
            int ten_sach_index = cursor.getColumnIndex(DBHelper.TEN_SACH_S);
            int ma_sach_index = cursor.getColumnIndex(DBHelper.MA_SACH_S);
            int img_sach_index = cursor.getColumnIndex(DBHelper.IMAGE_SACH);
            while (cursor.moveToNext()) {
                Sach sach = new Sach();
                if (ten_sach_index != -1) {
                    sach.setTen_sach_s(cursor.getString(ten_sach_index));
                }
                if (ma_sach_index != -1) {
                    sach.setMa_sach_s(cursor.getInt(ma_sach_index));
                }
                if (img_sach_index != -1) {
                    sach.setImage_sach(cursor.getBlob(img_sach_index));
                }
                saches.add(sach);
            }
        }
        if (saches != null) {
            listView.setAdapter(new MyAdapterDMSach(getActivity()));
        }
    }
    public void searchIDDauSach()
    {
        if (isInteger(edt_name_search.getText().toString().trim()) == false)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Mã sách nhập không hợp lệ");
            builder.setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            builder.create().show();
            return;
        }
        if (saches == null) {
            saches = new ArrayList<Sach>();
        } else {
            saches.removeAll(saches);
        }
        database = new MyDatabase(getActivity());
        Cursor cursor = database.searchIDDauSach(Integer.parseInt(edt_name_search.getText().toString().trim()));
        if (cursor != null) {
            int ten_sach_index = cursor.getColumnIndex(DBHelper.TEN_SACH_S);
            int ma_sach_index = cursor.getColumnIndex(DBHelper.MA_SACH_S);
            int img_sach_index = cursor.getColumnIndex(DBHelper.IMAGE_SACH);
            while (cursor.moveToNext()) {
                Sach sach = new Sach();
                if (ten_sach_index != -1) {
                    sach.setTen_sach_s(cursor.getString(ten_sach_index));
                }
                if (ma_sach_index != -1) {
                    sach.setMa_sach_s(cursor.getInt(ma_sach_index));
                }
                if (img_sach_index != -1) {
                    sach.setImage_sach(cursor.getBlob(img_sach_index));
                }
                saches.add(sach);
            }
        }
        if (saches != null) {
            listView.setAdapter(new MyAdapterDMSach(getActivity()));
        }
    }
    public void showSpinner()
    {
        List<String> list = new ArrayList<>();
        list.add("Tên sách");
        list.add("Mã sách");
        list.add("Mã đầu sách");
        //Tạo Adapter cho spinner
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, list);
        //Đặt adapter cho spinner
        spinner.setAdapter(arrayAdapter);
    }
    //Kiểm tra chuỗi có phải number hay không
    public static boolean isInteger(String str) {
        try {
            // Sử dụng phương thức parseInt để cố gắng chuyển đổi chuỗi thành số nguyên
            Integer.parseInt(str);
            // Nếu không có ngoại lệ nào xảy ra, chuỗi là số nguyên hợp lệ
            return true;
        } catch (NumberFormatException e) {
            // Nếu có ngoại lệ NumberFormatException xảy ra, chuỗi không phải là số nguyên
            return false;
        }
    }
}
