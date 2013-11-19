package com.csupomona.polyfinder;

import java.util.HashMap;
import java.util.Map;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by marti on 11/9/13.
 * reads from firebase and converts data to Posts
 * sends that Posts to adapter as they come in from the listeners to be displayed
 */
public class DisplayComments extends ListActivity {
    private Map<String, Post> comment_id_map; // hashmap for name(comment_id) to the JSON object
    private Firebase fBase;                     // Firebase reference
    private String event_id;                    // event Id for the comment thread
    private CustomAdapter<Post> adapter;        // adapter to be managing the data on the list view
    ListView listView;

    @Override
    protected void onCreate(Bundle savedIS) {
        super.onCreate(savedIS);
        //setContentView(android.R.layout.simple_list_item_1);

        comment_id_map = new HashMap<String, Post>();

        //getting the event id from the extra bundle
        Bundle b = getIntent().getExtras();
        this.event_id = b.getString("key");
        this.event_id = "0";

        //setting the firebase to refer to the correct url
        // all events in fire base should have a comments section
        fBase = new Firebase("https://polyfindertest.firebaseio.com/"+event_id+"/comments");
        adapter=new CustomAdapter<Post>(this, R.layout.activity_comment);

        //loads all data to adapter
        //sets listeners to check for changes in data
        loadComments(event_id);

        listView = (ListView)findViewById(android.R.layout.simple_list_item_1);
        setListAdapter(adapter);
    }

    private void loadComments(String event_id){
        ChildEventListener listener = fBase.addChildEventListener(new ChildEventListener() {

            //adds all children one by one to the adapter
            //when new ones are added they are added in the same manner
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Object value = dataSnapshot.getValue();
                //create singleComment from value
                Post sc = valueParse(value);
                //map the name(commentID) to the post (JSON value/DataSnapshot)
                comment_id_map.put(dataSnapshot.getName(),sc);


                //makes sure that the child is added in the correct position
                if (s == null) adapter.insert(sc,0);
                else{
                    Post previouSS = comment_id_map.get(s);
                    int previousIndex = adapter.getPosition(previouSS);
                    int nextIndex = previousIndex+1;

                    if (nextIndex == adapter.getCount()) adapter.add(sc);
                    else adapter.insert(sc,nextIndex);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Post old_post, new_post;

                Object new_obj = dataSnapshot.getValue();
                new_post = valueParse(new_obj);
                if (s != null){
                    old_post = comment_id_map.get(s);
                    int idx = adapter.getPosition(old_post);
                    idx++;
                    //update comment id map
                    comment_id_map.remove(s);
                    comment_id_map.put(dataSnapshot.getName(),new_post);

                    // set new single comment in place of old
                    adapter.insert(new_post, idx);
                }
                else {
                    adapter.insert(new_post,0);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String comment_id = dataSnapshot.getName();
                Object old_obj = comment_id_map.get(comment_id);
                adapter.remove(comment_id_map.get(comment_id));
                comment_id_map.remove(comment_id);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                String comment_id = dataSnapshot.getName();
                Post old_post = comment_id_map.get(comment_id);

                Object new_obj = dataSnapshot.getValue();
                Post new_post = valueParse(new_obj);

                adapter.remove(old_post);

                if (s==null) adapter.insert(new_post,0);

                else {
                    Post previousObj = comment_id_map.get(s);
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
        String id = (((Map)value).get("id").toString());
        String timeStamp = ((Map)value).get("date").toString();
        return new Post(event_id,id,postedBy,message,timeStamp);
    }

    public void showToast(String arg){
        Toast t = Toast.makeText(this, arg, Toast.LENGTH_SHORT);
        t.show();
    }

}