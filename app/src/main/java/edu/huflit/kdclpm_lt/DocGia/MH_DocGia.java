package edu.huflit.kdclpm_lt.DocGia;

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

import edu.huflit.kdclpm_lt.Adapter.MyAdapterDMDocGia;
import edu.huflit.kdclpm_lt.Adapter.MyAdapterDMSach;
import edu.huflit.kdclpm_lt.ManHinhChinh;
import edu.huflit.kdclpm_lt.Object.DocGia;
import edu.huflit.kdclpm_lt.Object.Sach;
import edu.huflit.kdclpm_lt.R;
import edu.huflit.kdclpm_lt.SQLite.DBHelper;
import edu.huflit.kdclpm_lt.SQLite.MyDatabase;

public class MH_DocGia extends Fragment {
    View view;
    MyDatabase database;
    ManHinhChinh manHinhChinh;
    ImageView next_add_doc_Gia;
    TextView tv_thong_bao_null;
    public static ListView listView;
    public static ArrayList<DocGia> docGias;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mh_doc_gia, container, false);
        database = new MyDatabase(getActivity());
        manHinhChinh = (ManHinhChinh) getActivity();
        anhXa();
        docGias = new ArrayList<>();
        capNhatDuLieuDocGia();
        next_add_doc_Gia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manHinhChinh.nextAddDocGia();
            }
        });
        return view;
    }
    public void anhXa()
    {
        next_add_doc_Gia = (ImageView) view.findViewById(R.id.next_add_doc_gia);
        listView = (ListView) view.findViewById(R.id.lv_doc_gia);
        tv_thong_bao_null = (TextView) view.findViewById(R.id.tv_doc_gia_null);
    }
    public void capNhatDuLieuDocGia() {
        if (docGias == null) {
            docGias = new ArrayList<DocGia>();
        } else {
            docGias.removeAll(docGias);
        }
        Cursor cursor = database.layDuLieuDocGia();
        if (cursor != null) {
            int ma_dg_index = cursor.getColumnIndex(DBHelper.MA_DOC_GIA);
            int ten_dg_index = cursor.getColumnIndex(DBHelper.TEN_DOC_GIA);
            int email_dg_index = cursor.getColumnIndex(DBHelper.EMAIL_DOC_GIA);
            int phone_dg_index = cursor.getColumnIndex(DBHelper.PHONE_DOC_GIA);
            int address_dg_index = cursor.getColumnIndex(DBHelper.ADDRESS_DOC_GIA);
            int avartar_dg_index = cursor.getColumnIndex(DBHelper.IMAGE_DOC_GIA);

            while (cursor.moveToNext()) {
                DocGia docGia = new DocGia();
                if (ma_dg_index != -1)
                {
                    docGia.setMa_doc_gia(cursor.getInt(ma_dg_index));
                }
                if (ten_dg_index != -1) {
                    docGia.setTen_doc_gia(cursor.getString(ten_dg_index));
                }
                if (email_dg_index != -1) {
                    docGia.setEmail_doc_gia(cursor.getString(phone_dg_index));
                }
                if (phone_dg_index != -1) {
                    docGia.setPhone_doc_gia(cursor.getString(phone_dg_index));
                }
                if (address_dg_index != -1) {
                    docGia.setAddress_doc_gia(cursor.getString(address_dg_index));
                }
                if (avartar_dg_index != -1) {
                    docGia.setImage_doc_gia(cursor.getBlob(avartar_dg_index));
                }
                docGias.add(docGia);
            }
        }
        if (docGias != null) {
            listView.setAdapter(new MyAdapterDMDocGia(getActivity()));
        }
        if (listView.getCount() <= 0) {
            tv_thong_bao_null.setText("RỖNG");
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("lay_ma_dg", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("ma_dg",docGias.get(i).getMa_doc_gia());
                editor.apply();

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Đọc giả");
                builder.setNegativeButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AlertDialog.Builder xoa = new AlertDialog.Builder(getActivity());
                        xoa.setTitle("Xóa đọc giả");
                        xoa.setNegativeButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SharedPreferences lay_ma_dg = getActivity().getSharedPreferences("lay_ma_dg", Context.MODE_PRIVATE);
                                database.xoaDocGia(lay_ma_dg.getInt("ma_dg", 0));
                                Toast.makeText(getActivity(), "Đã xóa", Toast.LENGTH_LONG).show();
                                capNhatListView();
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
                        manHinhChinh.nextUpdateDocGia();
                    }
                });
                builder.setNeutralButton("Xem chi tiết", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        manHinhChinh.nextQLDetailDocGia();
                    }
                });
                builder.create().show();
            }
        });
    }
    public void capNhatListView()
    {
        if (docGias == null) {
            docGias = new ArrayList<DocGia>();
        } else {
            docGias.removeAll(docGias);
        }
        Cursor cursor = database.layDuLieuDocGia();
        if (cursor != null) {
            int ma_dg_index = cursor.getColumnIndex(DBHelper.MA_DOC_GIA);
            int ten_dg_index = cursor.getColumnIndex(DBHelper.TEN_DOC_GIA);
            int email_dg_index = cursor.getColumnIndex(DBHelper.EMAIL_DOC_GIA);
            int phone_dg_index = cursor.getColumnIndex(DBHelper.PHONE_DOC_GIA);
            int address_dg_index = cursor.getColumnIndex(DBHelper.ADDRESS_DOC_GIA);
            int avartar_dg_index = cursor.getColumnIndex(DBHelper.IMAGE_DOC_GIA);

            while (cursor.moveToNext()) {
                DocGia docGia = new DocGia();
                if (ma_dg_index != -1)
                {
                    docGia.setMa_doc_gia(cursor.getInt(ma_dg_index));
                }
                if (ten_dg_index != -1) {
                    docGia.setTen_doc_gia(cursor.getString(ten_dg_index));
                }
                if (email_dg_index != -1) {
                    docGia.setEmail_doc_gia(cursor.getString(phone_dg_index));
                }
                if (phone_dg_index != -1) {
                    docGia.setPhone_doc_gia(cursor.getString(phone_dg_index));
                }
                if (address_dg_index != -1) {
                    docGia.setAddress_doc_gia(cursor.getString(address_dg_index));
                }
                if (avartar_dg_index != -1) {
                    docGia.setImage_doc_gia(cursor.getBlob(avartar_dg_index));
                }
                docGias.add(docGia);
            }
        }
        if (docGias != null) {
            listView.setAdapter(new MyAdapterDMDocGia(getActivity()));
        }
        if (listView.getCount() <= 0) {
            tv_thong_bao_null.setText("RỖNG");
        }
    }
}
