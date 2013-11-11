package com.example.listitem;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {

	
	ListView listview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Post f[] = new Post[]
				{
					new Post("Allan", "Wow, what is this?"),
					new Post("Marti", "This is custom adapter"),
					new Post("Raymond", "Wow, very nice. Good Job"),
					new Post("Nicolette", "Nice, who wrote this?"),
					new Post("Luke", "That would be me.  >_<"),
					new Post("Fernando", "What are you doing?"),
					new Post("Luke", "Sleeping Zzz"),
					new Post("Ashley", "Hey wanna go see movie tonight?"),
					new Post("Ivan", "Hey, how is your project?"),
					new Post("Megan Fox", "Are you free tomorrow?")
		};
		listview = (ListView) findViewById(R.id.status);		
		CustomAdapter<Post> cadapter = new CustomAdapter<Post>(MainActivity.this, R.layout.custom, f);
		listview.setAdapter(cadapter);
		
	}	
	
}
