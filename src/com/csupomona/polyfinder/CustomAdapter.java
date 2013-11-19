package com.csupomona.polyfinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomAdapter<T> extends ArrayAdapter<Post>{

    Context context;
    int layoutId;
    Post data[] = new Post[200];
    TextView name, comment ;
    int next_index;

    public CustomAdapter(Context context, int layoutResourceId, Post[] data){
        super(context, layoutResourceId, data);
        this.context = context;
        layoutId = layoutResourceId;
        for (int i = 0; i < data.length;i++){
            this.data[i] = data[i];
        }
        next_index = 0;
    }

    public CustomAdapter(Context context, int resource) {
        super(context, resource);
        this.context = context;
        layoutId = resource;
    }

    /*@Override
    public void insert(Post p, int i){
        data[i] = p;
        super.insert(p,i);
        if (i > next_index) next_index = i;
    }

    @Override
    public void add(Post p){
        data[next_index++] = p;
    }

    @Override
    public int getPosition(Post p){
        for (int i = 0; i < next_index;i++){
            if (p.getCommentId().equals(data[i].getCommentId()))
                return i;
        }
        return -1;
    }

    @Override
    public void remove(Post p){
        for ( int i = getPosition(p); i < next_index-1; i++){
            data[i] = data[i+1];
        }
        next_index--;
        super.remove(p);
    }*/

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        Post current = super.getItem(position);

        LayoutInflater in = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        row = in.inflate(layoutId, null);
        row.setTag(current);

        name = (TextView) row.findViewById(R.id.name);
        comment = (TextView) row.findViewById(R.id.comment);


        name.setText(current.getAuthorName());
        comment.setText(current.getMessage());

        return row;
    }
}
