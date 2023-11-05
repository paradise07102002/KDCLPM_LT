package edu.huflit.kdclpm_lt.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import edu.huflit.kdclpm_lt.DocGia.MH_DocGia;
import edu.huflit.kdclpm_lt.NhanVien.MH_NhanVien;
import edu.huflit.kdclpm_lt.R;

public class MyAdapterDMNhanVien extends BaseAdapter {
    LayoutInflater inflater;
    TextView textView;
    ImageView imageView;
    Context context;
    public MyAdapterDMNhanVien(Context context)
    {
        inflater = LayoutInflater.from(context);
        this.context = context;
    }
    @Override
    public int getCount() {
        return MH_NhanVien.users.size();
    }

    @Override
    public Object getItem(int i) {
        return MH_NhanVien.users.get(i);
    }

    @Override
    public long getItemId(int i) {
        return MH_NhanVien.users.get(i).getId_user();
    }
    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        View view = inflater.inflate(R.layout.item_list_dm_user, null);
        textView = (TextView) view.findViewById(R.id.tv_dmuser1);
        textView = (TextView) view.findViewById(R.id.tv_dmuser2);

        textView = (TextView) view.findViewById(R.id.tv_dmuser3);
        textView.setText(Integer.toString(MH_NhanVien.users.get(i).getId_user()));
        textView = (TextView) view.findViewById(R.id.tv_dmuser4);
        textView.setText(MH_NhanVien.users.get(i).getFullname_user());

        byte[] bytes = MH_NhanVien.users.get(i).getAvartar_user();
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        imageView = (ImageView) view.findViewById(R.id.dmuser_image);
        imageView.setImageBitmap(bitmap);

        return view;
    }
}
