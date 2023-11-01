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
import edu.huflit.kdclpm_lt.R;
import edu.huflit.kdclpm_lt.Sach.MH_Sach;

public class MyAdapterDMDocGia extends BaseAdapter {
    LayoutInflater inflater;
    TextView textView;
    ImageView imageView;
    Context context;
    public MyAdapterDMDocGia(Context context)
    {
        inflater = LayoutInflater.from(context);
        this.context = context;
    }
    @Override
    public int getCount() {
        return MH_DocGia.docGias.size();
    }

    @Override
    public Object getItem(int i) {
        return MH_DocGia.docGias.get(i);
    }

    @Override
    public long getItemId(int i) {
        return MH_DocGia.docGias.get(i).getMa_doc_gia();
    }
    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        View view = inflater.inflate(R.layout.item_list_dmdocgia, null);
        textView = (TextView) view.findViewById(R.id.tv_dmdg1);
        textView = (TextView) view.findViewById(R.id.tv_dmdg2);

        textView = (TextView) view.findViewById(R.id.tv_dmdg3);
        textView.setText(Integer.toString(MH_DocGia.docGias.get(i).getMa_doc_gia()));
        textView = (TextView) view.findViewById(R.id.tv_dmdg4);
        textView.setText(MH_DocGia.docGias.get(i).getTen_doc_gia());

        byte[] bytes = MH_DocGia.docGias.get(i).getImage_doc_gia();
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        imageView = (ImageView) view.findViewById(R.id.dmdg_image);
        imageView.setImageBitmap(bitmap);

        return view;
    }
}
