package com.csupomona.polyfinder;


import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LogPoint extends Activity {
	
	Button logIn;
	TextView logErr;
	TextView register;
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
		register = (TextView) findViewById(R.id.register);
		
		logIn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Can delete this
				if(bronco.getText().toString().equals("") || passwd.getText().toString().equals("")){
					logErr.setText("Please enter Username and Password");
				}
				else {
					Context context = getApplicationContext();
					Toast t = Toast.makeText(context, "successful", Toast.LENGTH_SHORT);
					t.show();
					logErr.setText("");
					newActivity();
					// log user into firebase
				}
			}
		});
		
		register.setText(Html.fromHtml("<a href=\"http://www.csupomona.edu/~anfarinas/cs356/polyfinder/\">Need to register?</a>"));
		register.setMovementMethod(LinkMovementMethod.getInstance());
	}
	
	private void newActivity(){
		Thread th = new Thread(){
			public void run(){
				Intent launch = new Intent("com.csupomona.polyfinder.CAMPUS");
				startActivity(launch);
			}
		};
		th.start();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}
}
