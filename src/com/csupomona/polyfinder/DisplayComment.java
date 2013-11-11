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
 */
public class DisplayComment extends ListActivity {
    private Map<String, Object> comment_id_map;
    private String ref; //firebase url
    private ArrayList<SingleComment> scom;
    private ChildEventListener listener;
    private int event_id;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedIS) {
        super.onCreate(savedIS);
        setContentView(R.layout.activity_comment);
        ref = "https://polyfindertest.firebaseio.com/comments/";
        comment_id_map = new HashMap<String, Object>();

        ListView listV = (ListView) findViewById(R.id.listView);
        Bundle b = getIntent().getExtras();
        this.event_id = b.getInt("key");

        loadComments(event_id);
        ArrayList<String> t = new ArrayList<String> ();
        for (int i = 0; i < scom.size(); i++)
            t.add(scom.get(i).toString());
        adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,t);
        setListAdapter(adapter);
    }



    public void loadComments(final int event_id){

        Firebase fBase = new Firebase(ref);

        listener = fBase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onCancelled() {
                //listener was cancelled
            }

            @Override
            public void onChildAdded(DataSnapshot ss, String previousChildName) {
                Object value = ss.getValue();
                comment_id_map.put(ss.getName(),value);

                //create singleComment from value
                SingleComment sc = valueParse(value);

                if (previousChildName == null){

                    scom.add(0,sc);
                }
                else {
                    Object previousSS = comment_id_map.get(value);
                    int previousIndex =  scom.indexOf(valueParse(previousSS));
                    int nextIndex = previousIndex +1;
                    if (nextIndex == scom.size()){
                        scom.add(sc);
                    }
                    else{
                        scom.add(nextIndex,sc);
                    }
                }

                //notify model of changes
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String comment_id = dataSnapshot.getName();
                Object old_obj = comment_id_map.get(comment_id);
                Object new_obj = dataSnapshot.getValue();
                int idx = scom.indexOf(valueParse(old_obj));

                // set new single comment in place of old
                scom.set(idx, valueParse(new_obj));

                //notify ui model of changes
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String comment_id = dataSnapshot.getName();
                Object oldObj = comment_id_map.get(comment_id);
                scom.remove(valueParse(oldObj));

                //notify ui model of changes
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                String commentId = dataSnapshot.getName();
                Object oldObj = comment_id_map.get(commentId);
                Object newObj = dataSnapshot.getValue();

                SingleComment oldsc = valueParse(oldObj);
                SingleComment newsc = valueParse(newObj);
                int index = scom.indexOf(oldsc);
                scom.remove(index);

                if (s == null){
                    scom.add(0,newsc);
                }
                else {
                    Object previousObj = comment_id_map.get(s);
                    int previousIndex = scom.indexOf(oldsc);
                    int nextIndex = previousIndex+1;
                    if (nextIndex == scom.size()){
                        scom.add(newsc);
                    }
                    else {
                        scom.add(nextIndex, newsc);
                    }
                }
                //notify ui model of changes
                adapter.notifyDataSetChanged();
            }
        });
    }

    //parse json comment child to single comment
    public SingleComment valueParse(Object value){
        String message = ((Map)value).get("message").toString();
        String postedBy = ((Map)value).get("postedBy").toString();
        int id = Integer.parseInt(((Map)value).get("id").toString());
        String timeStamp = ((Map)value).get("timeStamp").toString();
        return new SingleComment(event_id,id,postedBy,message,timeStamp);
    }
}


/*
                Object value = ss.getValue();
				String message = ((Map)value).get("message").toString();
				String postedBy = ((Map)value).get("postedBy").toString();
				String id = ((Map)value).get("id").toString();
				String timeStamp = ((Map)value).get("timeStamp").toString();
				text.setText(postedBy + ": (" + timeStamp + ")"+ "\n\t\t" + message);

                String userName = ss.getName(); //gets number of the comment
                GenericTypeIndicator<Map<String, Object>> t = new GenericTypeIndicator<Map<String, Object>>() {};
                Map<String, Object> userData = ss.getValue(t);
                System.out.println("User " + userName + " has entered the chat");

                Object value = ss.getValue();
                int eventId = Integer.parseInt(((Map)value).get("eventId").toString());
                if (event_id == eventId);
                {
                    SingleComment sc = new SingleComment();

                    sc.setAuthor_name(((Map)value).get("postedBy").toString());
                    sc.setComment_id(Integer.parseInt(ss.getName()));
                    sc.setEvent_id(eventId);
                    sc.setMessage(((Map)value).get("message").toString());
                    sc.setTimestamp(((Map)value).get("timeStamp").toString());

                    //add the child to the array
                    if (scom_idx >= 100){
                        //throw error or do something to lower the amount of comments
                    }
                    else{
                        //add to the array scom
                        scom[scom_idx++] = sc;
                        //update ui
                    }
                }  */