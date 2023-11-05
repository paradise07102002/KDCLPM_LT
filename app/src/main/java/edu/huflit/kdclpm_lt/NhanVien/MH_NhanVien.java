package edu.huflit.kdclpm_lt.NhanVien;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import edu.huflit.kdclpm_lt.Adapter.MyAdapterDMDocGia;
import edu.huflit.kdclpm_lt.Adapter.MyAdapterDMNhanVien;
import edu.huflit.kdclpm_lt.ManHinhChinh;
import edu.huflit.kdclpm_lt.Object.DocGia;
import edu.huflit.kdclpm_lt.Object.User;
import edu.huflit.kdclpm_lt.R;
import edu.huflit.kdclpm_lt.SQLite.DBHelper;
import edu.huflit.kdclpm_lt.SQLite.MyDatabase;

public class MH_NhanVien extends Fragment {
    View view;
    MyDatabase database;
    ManHinhChinh manHinhChinh;
    ImageView next_add_nv;
    public static ListView listView;
    public static ArrayList<User> users;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mh_nhan_vien, container, false);
        database = new MyDatabase(getActivity());
        manHinhChinh = (ManHinhChinh) getActivity();
        anhXa();
        users = new ArrayList<>();
        capNhatDuLieuNV();
        next_add_nv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manHinhChinh.nextAddNhanVien();
            }
        });


        return view;
    }
    public void anhXa()
    {
        next_add_nv = (ImageView) view.findViewById(R.id.next_add_nv);
        listView = (ListView) view.findViewById(R.id.lv_nv);
    }
    public void capNhatDuLieuNV() {
        if (users == null) {
            users = new ArrayList<User>();
        } else {
            users.removeAll(users);
        }
        Cursor cursor = database.layDuLieuNhanVien();
        if (cursor != null) {
            int ma_nv_index = cursor.getColumnIndex(DBHelper.ID_USER);
            int ten_nv_index = cursor.getColumnIndex(DBHelper.FULLNAME_USER);
            int email_nv_index = cursor.getColumnIndex(DBHelper.EMAIL_USER);
            int phone_nv_index = cursor.getColumnIndex(DBHelper.PHONE_USER);
            int role_nv_index = cursor.getColumnIndex(DBHelper.ROLE_USER);
            int avartar_dg_index = cursor.getColumnIndex(DBHelper.AVARTAR_USER);

            while (cursor.moveToNext()) {
                User user = new User();
                if (ma_nv_index != -1)
                {
                    user.setId_user(cursor.getInt(ma_nv_index));
                }
                if (ten_nv_index != -1) {
                    user.setFullname_user(cursor.getString(ten_nv_index));
                }
                if (email_nv_index != -1) {
                    user.setEmail_user(cursor.getString(email_nv_index));
                }
                if (phone_nv_index != -1) {
                    user.setPhone_user(cursor.getString(phone_nv_index));
                }
                if (role_nv_index != -1) {
                    user.setRole_user(cursor.getString(role_nv_index));
                }
                if (avartar_dg_index != -1) {
                    user.setAvartar_user(cursor.getBlob(avartar_dg_index));
                }
                users.add(user);
            }
        }
        if (users != null) {
            listView.setAdapter(new MyAdapterDMNhanVien(getActivity()));
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("lay_ma_nv", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("ma_nv",users.get(i).getId_user());
                editor.apply();

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Nhân viên");
                builder.setNegativeButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AlertDialog.Builder xoa = new AlertDialog.Builder(getActivity());
                        xoa.setTitle("Xóa nhân viên");
                        xoa.setNegativeButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                AlertDialog.Builder show = new AlertDialog.Builder(getActivity());
                                show.setTitle("Xóa nhân viên");
                                show.setNegativeButton("Xác nhận xóa", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        SharedPreferences manv = getActivity().getSharedPreferences("lay_ma_nv", Context.MODE_PRIVATE);
                                        database.xoaUser(manv.getInt("ma_nv", -1));
                                        Toast.makeText(getActivity(), "Đã xóa nhân viên", Toast.LENGTH_LONG).show();
                                        capNhatListView();
                                    }
                                });
                                show.setPositiveButton("Hủy", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                                show.create().show();
                            }
                        });
                        xoa.setPositiveButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        xoa.create().show();
                    }
                });
                builder.setPositiveButton("Sửa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        manHinhChinh.nextUpdateNhanVien();
                    }
                });
                builder.setNeutralButton("Xem chi tiết", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        manHinhChinh.nextDetailNhanVien();
                    }
                });
                builder.create().show();
            }
        });
    }
    public void capNhatListView()
    {
        if (users == null) {
            users = new ArrayList<User>();
        } else {
            users.removeAll(users);
        }
        Cursor cursor = database.layDuLieuNhanVien();
        if (cursor != null) {
            int ma_nv_index = cursor.getColumnIndex(DBHelper.ID_USER);
            int ten_nv_index = cursor.getColumnIndex(DBHelper.FULLNAME_USER);
            int email_nv_index = cursor.getColumnIndex(DBHelper.EMAIL_USER);
            int phone_nv_index = cursor.getColumnIndex(DBHelper.PHONE_USER);
            int role_nv_index = cursor.getColumnIndex(DBHelper.ROLE_USER);
            int avartar_dg_index = cursor.getColumnIndex(DBHelper.AVARTAR_USER);

            while (cursor.moveToNext()) {
                User user = new User();
                if (ma_nv_index != -1)
                {
                    user.setId_user(cursor.getInt(ma_nv_index));
                }
                if (ten_nv_index != -1) {
                    user.setFullname_user(cursor.getString(ten_nv_index));
                }
                if (email_nv_index != -1) {
                    user.setEmail_user(cursor.getString(email_nv_index));
                }
                if (phone_nv_index != -1) {
                    user.setPhone_user(cursor.getString(phone_nv_index));
                }
                if (role_nv_index != -1) {
                    user.setRole_user(cursor.getString(role_nv_index));
                }
                if (avartar_dg_index != -1) {
                    user.setAvartar_user(cursor.getBlob(avartar_dg_index));
                }
                users.add(user);
            }
        }
        if (users != null) {
            listView.setAdapter(new MyAdapterDMNhanVien(getActivity()));
        }
    }
}
