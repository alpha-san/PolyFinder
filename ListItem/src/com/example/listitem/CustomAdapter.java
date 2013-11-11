package com.example.listitem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomAdapter<T> extends ArrayAdapter<Post>{

	Context context;
	int layoutId;
	Post data[] = null;
	TextView name, comment ;
	
	public CustomAdapter(Context context, int layoutResourceId, Post[] data){
		super(context, layoutResourceId, data);
		this.context = context;
		layoutId = layoutResourceId;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		Post current = data[position];
		
		LayoutInflater in = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		row = in.inflate(layoutId, null);
		row.setTag(current);
			
		name = (TextView) row.findViewById(R.id.name);
		comment = (TextView) row.findViewById(R.id.comment);
		
		
		name.setText(current.name);
		comment.setText(current.comment);
		
		return row;
	}
	
	
}
