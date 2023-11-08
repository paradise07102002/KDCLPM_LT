package edu.huflit.kdclpm_lt.MuonTraSach;

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
import edu.huflit.kdclpm_lt.Adapter.MyAdapterQLMuonTra;
import edu.huflit.kdclpm_lt.ManHinhChinh;
import edu.huflit.kdclpm_lt.Object.DocGia;
import edu.huflit.kdclpm_lt.Object.MuonTraSach;
import edu.huflit.kdclpm_lt.R;
import edu.huflit.kdclpm_lt.SQLite.DBHelper;
import edu.huflit.kdclpm_lt.SQLite.MyDatabase;

public class QL_MuonTra extends Fragment {
    View view;
    MyDatabase database;
    ManHinhChinh manHinhChinh;
    ImageView back;
    public static ListView listView;
    public static ArrayList<MuonTraSach> muonTraSaches;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.ql_muon_tra, container, false);
        database = new MyDatabase(getActivity());
        manHinhChinh = (ManHinhChinh) getActivity();
        anhXa();
        muonTraSaches = new ArrayList<>();
        capNhatDuLieuMuonTra();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manHinhChinh.nextQLMuonTra();
            }
        });
        return view;
    }
    public void anhXa()
    {
        back = (ImageView) view.findViewById(R.id.ql_mt_back);
        listView = (ListView) view.findViewById(R.id.lv_muon_tra);
    }
    public void capNhatDuLieuMuonTra() {
        if (muonTraSaches == null) {
            muonTraSaches = new ArrayList<MuonTraSach>();
        } else {
            muonTraSaches.removeAll(muonTraSaches);
        }
        Cursor cursor = database.layDuLieuMuonTra();
        if (cursor != null) {
            int ma_mt_index = cursor.getColumnIndex(DBHelper.MA_MUON_TRA_MTS);
            int ma_dg_index = cursor.getColumnIndex(DBHelper.MA_USER_MTS);
            int ma_sach_index = cursor.getColumnIndex(DBHelper.MA_SACH_MTS);


            while (cursor.moveToNext()) {
                MuonTraSach muonTraSach = new MuonTraSach();
                if (ma_mt_index != -1)
                {
                    muonTraSach.setMa_muon_tra_mts(cursor.getInt(ma_mt_index));
                }
                if (ma_dg_index != -1) {
                    muonTraSach.setMa_user_mts(cursor.getInt(ma_dg_index));
                }
                if (ma_sach_index != -1) {
                    muonTraSach.setMa_sach_mts(cursor.getInt(ma_sach_index));
                }
                muonTraSaches.add(muonTraSach);
            }
        }
        if (muonTraSaches != null) {
            listView.setAdapter(new MyAdapterQLMuonTra(getActivity()));
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("lay_ma_mt", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("ma_mt",muonTraSaches.get(i).getMa_muon_tra_mts());
                editor.apply();
                manHinhChinh.nextDetailMT();
            }
        });
    }
    public void capNhatListView()
    {
        if (muonTraSaches == null) {
            muonTraSaches = new ArrayList<MuonTraSach>();
        } else {
            muonTraSaches.removeAll(muonTraSaches);
        }
        Cursor cursor = database.layDuLieuMuonTra();
        if (cursor != null) {
            int ma_mt_index = cursor.getColumnIndex(DBHelper.MA_MUON_TRA_MTS);
            int ma_dg_index = cursor.getColumnIndex(DBHelper.MA_USER_MTS);
            int ma_sach_index = cursor.getColumnIndex(DBHelper.MA_SACH_MTS);

            while (cursor.moveToNext()) {
                MuonTraSach muonTraSach = new MuonTraSach();
                if (ma_mt_index != -1)
                {
                    muonTraSach.setMa_muon_tra_mts(cursor.getInt(ma_mt_index));
                }
                if (ma_dg_index != -1) {
                    muonTraSach.setMa_user_mts(cursor.getInt(ma_dg_index));
                }
                if (ma_sach_index != -1) {
                    muonTraSach.setMa_user_mts(cursor.getInt(ma_sach_index));
                }
                muonTraSaches.add(muonTraSach);
            }
        }
        if (muonTraSaches != null) {
            listView.setAdapter(new MyAdapterQLMuonTra(getActivity()));
        }
    }
}
