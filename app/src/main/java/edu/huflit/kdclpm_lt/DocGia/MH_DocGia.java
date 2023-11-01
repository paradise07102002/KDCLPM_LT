package edu.huflit.kdclpm_lt.DocGia;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import edu.huflit.kdclpm_lt.ManHinhChinh;
import edu.huflit.kdclpm_lt.Object.Sach;
import edu.huflit.kdclpm_lt.R;
import edu.huflit.kdclpm_lt.SQLite.MyDatabase;

public class MH_DocGia extends Fragment {
    View view;
    MyDatabase database;
    ManHinhChinh manHinhChinh;
    ImageView next_add_doc_Gia;
    TextView tv_thong_bao_null;
    public static ListView listView;
    public static ArrayList<Sach> saches;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mh_doc_gia, container, false);
        database = new MyDatabase(getActivity());
        manHinhChinh = (ManHinhChinh) getActivity();
        anhXa();
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
}
