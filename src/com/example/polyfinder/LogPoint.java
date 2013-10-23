package com.example.polyfinder;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LogPoint extends Activity {
	
	Button logIn;
	TextView logErr;
	String user, pwd;
	EditText bronco, passwd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log_point);
		
		referenceXML();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.log_point, menu);
		return true;
	}

	public void referenceXML(){
		logIn = (Button) findViewById(R.id.signIn);
		logErr = (TextView) findViewById(R.id.logError);
		bronco = (EditText) findViewById(R.id.userInput);
		passwd = (EditText) findViewById(R.id.pwdInput);
		
		logIn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Can delete this
				if(bronco.getText().toString().equals("") || passwd.getText().toString().equals("")){
					logErr.setText("Please enter Bronco Name and password");
				}
				else {
					Context context = getApplicationContext();
					Toast t = Toast.makeText(context, "successful", Toast.LENGTH_SHORT);
					t.show();
					logErr.setText("");
				}
			}
		});
	}
}
