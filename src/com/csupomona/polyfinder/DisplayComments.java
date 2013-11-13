package com.csupomona.polyfinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by marti on 11/9/13.
 * reads from firebase and converts data to Posts
 * sends that Posts to adapter to be displayed
 */
public class DisplayComments extends ListActivity {
    private Map<String, Object> comment_id_map;
    private Firebase fBase;
    private ArrayList<Post> scom;
    private ChildEventListener listener;
    private String event_id;
    private CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedIS) {
        super.onCreate(savedIS);
        setContentView(R.layout.activity_comment);
        comment_id_map = new HashMap<String, Object>();
        ListView listV = (ListView) findViewById(R.id.listView);

        Bundle b = getIntent().getExtras();
        this.event_id = b.getString("key");
        fBase = new Firebase("https://polyfindertest.firebaseio.com/"+event_id+"/comments");
        adapter = new CustomAdapter(this, R.layout.activity_comment);

        loadComments(event_id);

        Post [] t = new Post[scom.size()];

        for (int i = 0; i < scom.size(); i++) t[i] = scom.get(i);

        adapter=new CustomAdapter<Post>(this, R.layout.activity_comment);
        ListView lv = (ListView)findViewById(android.R.layout.simple_list_item_1);
        setListAdapter(adapter);
    }

    private void loadComments(String event_id){
        listener = fBase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Object value = dataSnapshot.getValue();
                //map the name(commenrID) to the post (JSON value/DataSnapshot)
                comment_id_map.put(dataSnapshot.getName(),value);
                //create singleComment from value
                Post sc = valueParse(value);

                if (s == null) adapter.insert(sc,0);
                else{
                    Object previouSS = comment_id_map.get(s);
                    int previousIndex = adapter.getPosition(valueParse(previouSS));
                    int nextIndex = previousIndex+1;

                    if (nextIndex == adapter.getCount()) adapter.add(sc);
                    else adapter.insert(sc,nextIndex);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String comment_id = dataSnapshot.getName();
                Object old_obj = comment_id_map.get(comment_id);
                Object new_obj = dataSnapshot.getValue();
                int idx = adapter.getPosition(old_obj);

                //update comment id map
                comment_id_map.remove(old_obj);
                comment_id_map.put(dataSnapshot.getName(),new_obj);

                // set new single comment in place of old
                adapter.insert(valueParse(new_obj), idx);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String comment_id = dataSnapshot.getName();
                Object old_obj = comment_id_map.get(comment_id);
                comment_id_map.remove(old_obj);

                adapter.remove(valueParse(old_obj));
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                String comment_id = dataSnapshot.getName();
                Object old_obj = comment_id_map.get(comment_id);
                Object new_obj = dataSnapshot.getValue();

                Post new_post = valueParse(new_obj);
                Post old_post = valueParse(old_obj);

                adapter.remove(old_post);

                if (s==null) adapter.insert(new_post,0);

                else {
                    Object previousObj = comment_id_map.get(s);
                    int previousIndex = adapter.getPosition(previousObj);
                    int nextIndex = previousIndex+1;
                    if (nextIndex == adapter.getCount()) adapter.add(new_post);
                    else adapter.insert(new_post,nextIndex);
                }
            }

            @Override
            public void onCancelled() {
                adapter.clear();
            }
        });
    }

    //parse json comment child to single comment
    private Post valueParse(Object value){
        String message = ((Map)value).get("message").toString();
        String postedBy = ((Map)value).get("postedBy").toString();
        int id = Integer.parseInt(((Map)value).get("id").toString());
        String timeStamp = ((Map)value).get("timeStamp").toString();
        return new Post(event_id,id,postedBy,message,timeStamp);
    }
}