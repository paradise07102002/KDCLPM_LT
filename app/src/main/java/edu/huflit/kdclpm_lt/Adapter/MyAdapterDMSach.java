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

import edu.huflit.kdclpm_lt.R;
import edu.huflit.kdclpm_lt.Sach.MH_Sach;

public class MyAdapterDMSach extends BaseAdapter {
    LayoutInflater inflater;
    TextView textView;
    ImageView imageView;
    Context context;
    public MyAdapterDMSach(Context context)
    {
        inflater = LayoutInflater.from(context);
        this.context = context;
    }
    @Override
    public int getCount() {
        return MH_Sach.saches.size();
    }

    @Override
    public Object getItem(int i) {
        return MH_Sach.saches.get(i);
    }

    @Override
    public long getItemId(int i) {
        return MH_Sach.saches.get(i).getMa_sach_s();
    }


    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        View view = inflater.inflate(R.layout.item_list_dms, null);

        textView = (TextView) view.findViewById(R.id.tv_dms1);
        textView = (TextView) view.findViewById(R.id.tv_dms2);
        textView = (TextView) view.findViewById(R.id.tv_dms3);
        textView.setText(Integer.toString(MH_Sach.saches.get(i).getMa_sach_s()));
        textView = (TextView) view.findViewById(R.id.tv_dms4);
        textView.setText(MH_Sach.saches.get(i).getTen_sach_s());

        byte[] bytes = MH_Sach.saches.get(i).getImage_sach();
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        imageView = (ImageView) view.findViewById(R.id.dms_image);
        imageView.setImageBitmap(bitmap);

        return view;
    }
}
