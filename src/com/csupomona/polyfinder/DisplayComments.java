package com.csupomona.polyfinder;

import java.util.HashMap;
import java.util.Map;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.ValueEventListener;
import com.firebase.simplelogin.SimpleLogin;
import com.firebase.simplelogin.SimpleLoginAuthenticatedHandler;
import com.firebase.simplelogin.User;
import com.firebase.simplelogin.enums.*;
import com.firebase.simplelogin.enums.Error;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by marti on 11/9/13.
 * reads from firebase and converts data to Posts
 * sends that Posts to adapter as they come in from the listeners to be displayed
 */
public class DisplayComments extends ListActivity {
    private Map<String, Post> comment_id_map; // hashmap for name(comment_id) to the JSON object
    private Firebase fBase;                     // Firebase reference
    private String event_id,email,password;                    // event Id for the comment thread
    private CustomAdapter<Post> adapter;        // adapter to be managing the data on the list view
    private ListView listView;
    private ChildEventListener listener;
    //private FirebaseReader;

    @Override
    protected void onCreate(Bundle savedIS) {
        super.onCreate(savedIS);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.comment);

        comment_id_map = new HashMap<String, Post>();

        //getting the event id from the extra bundle

        this.event_id = getIntent().getStringExtra("key");
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        //this.event_id = "-J89wmNBsFuqROJ0AJ5F";
        //listView = (ListView) findViewById(R.id.list);

        //setting the firebase to refer to the correct url
        // all events in fire base should have a comments section
        showToast("event_id: "+event_id);

        fBase = new Firebase("https://polyfindertest.firebaseio.com/"+event_id+"/comments/");
        SimpleLogin sl = new SimpleLogin(fBase);
        sl.loginWithEmail(email,password,new SimpleLoginAuthenticatedHandler() {
            @Override
            public void authenticated(Error error, User user) {
                if (error != null){

                }
            }
        });
        sl.checkAuthStatus(new SimpleLoginAuthenticatedHandler() {
            public void authenticated(com.firebase.simplelogin.enums.Error error, User user) {
                if (error != null) {
                    // Oh no! There was an error performing the check
                    showToast("login error");
                } else if (user == null) {
                    // No user is logged in
                    showToast("null user");
                } else {
                    // There is a logged in user
                    showToast("User is Logged in");
                }
            }
        });
        adapter=new CustomAdapter<Post>(this, R.layout.activity_comment);

        checkEmpty();

        //loads all data to adapter
        //sets listeners to check for changes in data
        loadComments(event_id);

        EditText inputText = (EditText)findViewById(R.id.messageInput);
        inputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_NULL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    sendMessage();
                }
                return true;
            }
        });

        findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

        super.setListAdapter(adapter);
    }

    private void checkEmpty(){
        final Firebase ref = new Firebase("https://polyfindertest.firebaseio.com/");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (!snapshot.hasChild("comments")) {
                    //write to firebase new comments section of the name event_id
                    ref.child("comments");
                    //showToast(event_id+" comments");
                }
                ref.removeEventListener(this);
            }

            @Override
            public void onCancelled() {
                //System.err.println("Listener was cancelled");
            }
        });

        final Firebase dataRef = new Firebase("https://polyfindertest.firebaseio.com/"+event_id);
        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (!snapshot.hasChild("comments")){
                    //write to firebase new comments section of the name event_id
                    dataRef.child("comments");
                    //showToast(event_id+" comments");
                }
                dataRef.removeEventListener(this);
            }

            @Override
            public void onCancelled() {
                //System.err.println("Listener was cancelled");
            }
        });
    }

    private void sendMessage() {
        EditText inputText = (EditText)findViewById(R.id.messageInput);
        String input = inputText.getText().toString();
        if (!input.equals("")) {
            // Create a new, auto-generated child of that chat location, and save our chat data there
            HashMap<String,Object> data = new HashMap<String, Object>();
            data.put("date","today");
            data.put("id",2);
            data.put("message",input);
            data.put("postedBy",email);
            fBase.push().setValue(data);

            inputText.setText("");
        }
    }

    private void loadComments(String event_id){
        listener = fBase.addChildEventListener(new ChildEventListener() {

            //adds all children one by one to the adapter
            //when new ones are added they are added in the same manner
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Object value = dataSnapshot.getValue();
                //create post from value
                Post sc = valueParse(value);
                //map the name(commentID) to the post (JSON value/DataSnapshot)
                comment_id_map.put(dataSnapshot.getName(),sc);

                //adapter.add(sc);


                //makes sure that the child is added in the correct position
                if (s == null) adapter.insert(sc,0);
                else{
                    Post previouSS = comment_id_map.get(s);
                    int previousIndex = adapter.getPosition(previouSS);

                    int nextIndex = previousIndex+1;

                    if (nextIndex == adapter.getCount()){
                        showToast("child: "+adapter.getCount());
                        adapter.add(sc);
                    }
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
                showToast("removed: "+dataSnapshot.getName());
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
        return new Post(postedBy,message,timeStamp);
    }

    public void showToast(String arg){
        Toast t = Toast.makeText(this, arg, Toast.LENGTH_SHORT);
        t.show();
    }

    public void cleanup(){
        fBase.removeEventListener(listener);
        comment_id_map.clear();
        adapter.clear();
    }

    @Override
    public void onPause() {
        super.onPause();
        cleanup();
        finish();
    }
}