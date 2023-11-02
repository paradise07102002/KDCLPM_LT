package edu.huflit.kdclpm_lt.MuonTraSach;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import edu.huflit.kdclpm_lt.ManHinhChinh;
import edu.huflit.kdclpm_lt.R;
import edu.huflit.kdclpm_lt.SQLite.MyDatabase;

public class MH_MuonTra extends Fragment {
    View view;
    MyDatabase database;
    ManHinhChinh manHinhChinh;
    ImageView img_next_create_phieu_muon, img_next_ql_phieu_muon;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mh_muon_tra, container, false);
        database = new MyDatabase(getActivity());
        manHinhChinh = (ManHinhChinh) getActivity();
        anhXa();
        img_next_create_phieu_muon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manHinhChinh.nextAddMuonTra();
            }
        });

        return view;
    }
    public void anhXa()
    {
        img_next_create_phieu_muon = (ImageView) view.findViewById(R.id.next_create_muon_tra);
    }
}
