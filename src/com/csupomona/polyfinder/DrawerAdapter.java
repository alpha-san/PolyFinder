package com.csupomona.polyfinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DrawerAdapter<T> extends ArrayAdapter<DrawerItem>{
	
	Context context;
    int layoutId;
    DrawerItem data[];
    ImageView icon;
    TextView text;

    public DrawerAdapter(Context context, int layoutResourceId, DrawerItem[] data){
        super(context, layoutResourceId, data);
        this.context = context;
        layoutId = layoutResourceId;
        this.data = data;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        DrawerItem current = super.getItem(position);

        LayoutInflater in = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        row = in.inflate(layoutId, null);
        row.setTag(current);

        icon = (ImageView) row.findViewById(R.id.imgIcon);
        text = (TextView) row.findViewById(R.id.txtTitle);


        text.setText(data[position].title);
        icon.setImageResource(data[position].icon);

        return row;
    }
}
