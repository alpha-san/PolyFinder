package cartesia.comments;

import java.util.Map;
import org.json.*;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.GenericTypeIndicator;
import com.firebase.client.ValueEventListener;
import android.os.Bundle;
import android.os.Message;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Comment extends Activity {
	
	Button send;
	TextView comments;
	EditText text;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		
		readDatabase();
	}
//	public void XML(){
//		send = (Button) findViewById(R.id.send);
//		text = (EditText) findViewById(R.id.comment);
//		
//		send.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				String comment = text.getText().toString();
//				storeComment(comment);
//			}
//		});
//	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.comment, menu);
		return true;
	}
//	public void storeComment(String value){
//		Firebase fBase = new Firebase("https://polyfindertest.firebaseio.com/");
//		String id = fBase.child("User ID").getName();
//		
//		Firebase pushlist = fBase.push();
//		String name = pushlist.getName();
//	}
	public void readDatabase(){
		Firebase fBase = new Firebase("https://polyfindertest.firebaseio.com/events/0");
		fBase.addValueEventListener(new ValueEventListener() {

			@Override
			public void onCancelled() {}

			@Override
			public void onDataChange(DataSnapshot ss) {
				TextView text = (TextView) findViewById(R.id.textView);
				
				Object value = ss.getValue();
				String message = ((Map)value).get("message").toString();
				String postedBy = ((Map)value).get("postedBy").toString();
				String id = ((Map)value).get("id").toString();
				String timeStamp = ((Map)value).get("timeStamp").toString();
				text.setText(postedBy + ": (" + timeStamp + ")"+ "\n\t\t" + message);
			}			
		}); 
		    
	}
	public void format(Object val){
		
	}

	///////////////////////////////////////////////////////////////////////////////////
	private static class Message {

	    private String postedBy;
	    private String message;
	    private int id;
	    private String timeStamp;

	    private Message() { }

	    public Message(int id, String postedBy, String message, String timeStamp) {
	        this.postedBy = postedBy;
	        this.message = message;
	        this.id = id;
	        this.timeStamp = timeStamp;
	    }

	    public int getId() {
	        return id;
	    }
	    public String getMessage() {
	        return message;
	    }
	    public String getPostedBy() {
	    	return postedBy;
	    }
	    public String getTimeStamp() {
	    	return timeStamp;
	    }
	}
}

