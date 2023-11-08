package edu.huflit.kdclpm_lt.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import edu.huflit.kdclpm_lt.DocGia.MH_DocGia;
import edu.huflit.kdclpm_lt.MuonTraSach.MH_MuonTra;
import edu.huflit.kdclpm_lt.MuonTraSach.QL_MuonTra;
import edu.huflit.kdclpm_lt.R;
import edu.huflit.kdclpm_lt.SQLite.DBHelper;
import edu.huflit.kdclpm_lt.SQLite.MyDatabase;

public class MyAdapterQLMuonTra extends BaseAdapter {
    LayoutInflater inflater;
    TextView textView;
    ImageView imageView;
    Context context;
    MyDatabase database;
    public MyAdapterQLMuonTra(Context context)
    {
        inflater = LayoutInflater.from(context);
        this.context = context;
    }
    @Override
    public int getCount() {
        return QL_MuonTra.muonTraSaches.size();
    }

    @Override
    public Object getItem(int i) {
        return QL_MuonTra.muonTraSaches.get(i);
    }

    @Override
    public long getItemId(int i) {
        return QL_MuonTra.muonTraSaches.get(i).getMa_muon_tra_mts();
    }
    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        View view = inflater.inflate(R.layout.item_list_mts, null);
        textView = (TextView) view.findViewById(R.id.tv_mts1);
        textView = (TextView) view.findViewById(R.id.tv_mts2);

        textView = (TextView) view.findViewById(R.id.tv_mts3);
        textView.setText(Integer.toString(QL_MuonTra.muonTraSaches.get(i).getMa_muon_tra_mts()));

        database = new MyDatabase(context);
        Cursor cursor = database.getSachByMaSach(QL_MuonTra.muonTraSaches.get(i).getMa_sach_mts());
        if (cursor.moveToFirst())
        {
            int ten_sach_index = cursor.getColumnIndex(DBHelper.TEN_SACH_S);
            textView = (TextView) view.findViewById(R.id.tv_mts4);
            textView.setText(cursor.getString(ten_sach_index));
        }

        imageView = (ImageView) view.findViewById(R.id.mts_image);
        return view;
    }
}
