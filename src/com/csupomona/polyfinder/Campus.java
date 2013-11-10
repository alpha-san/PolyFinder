package com.csupomona.polyfinder;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

public class Campus extends Activity implements View.OnClickListener{
	
	WebView webview;
	ImageView addEvent;
	ImageView confirm;
	ImageView cancel;
	ImageView meeting;
	ImageView hangout;
	ImageView party;
	Context context = this;
	Dialog dialog;
	String arg;
	boolean selection = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_campus);
		
		Thread t = new Thread(){
			public void run(){
				webview = (WebView) findViewById(R.id.map);
				webview.setWebChromeClient(new WebChromeClient());
				WebSettings webSettings = webview.getSettings();
				webSettings.setJavaScriptEnabled(true);
				webview.addJavascriptInterface(new WebViewInterface(context), "WebViewInterface");
				webview.loadUrl("file:///android_asset/leaflet/index.html");
				setReference();
			}
		};
		t.start();
	}

	
	//Set reference to XML and actionListener for buttons
	void setReference(){
		addEvent = (ImageView) findViewById(R.id.addEvent);
		confirm = (ImageView) findViewById(R.id.confirm);
		cancel = (ImageView) findViewById(R.id.cancel);
		
		
		addEvent.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(selection == false){
					dialog = new Dialog(context);
					dialog.setContentView(R.layout.pop_up);
					dialog.setTitle("Choose an event");
					setDialogButton();
					dialog.show();
					selection = true;
				}
			}
		});
		
		confirm.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				confirm.setVisibility(View.INVISIBLE);
				cancel.setVisibility(View.INVISIBLE);
				runJavascript("javascript:yesButton()");
				selection = false;
			}
		});
		
		cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				confirm.setVisibility(View.INVISIBLE);
				cancel.setVisibility(View.INVISIBLE);
				runJavascript("javascript:noButton()");
				selection = false;
			}
		});
	}

	void setDialogButton(){
		meeting = (ImageView) dialog.findViewById(R.id.meeting);
		hangout = (ImageView) dialog.findViewById(R.id.hangout);
		party = (ImageView) dialog.findViewById(R.id.party);
		
		meeting.setOnClickListener(this);
		hangout.setOnClickListener(this);
		party.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		final Context context = getApplicationContext();
		Toast t = Toast.makeText(context, "Choose a location", Toast.LENGTH_SHORT);
		dialog.dismiss();
		t.show();
		
		if(meeting.getId() == ((ImageView)v).getId() )
			arg = "Meeting";
		else if(hangout.getId() == ((ImageView)v).getId() )
	    	arg = "Hangout";
		else 
			arg = "Party";
		
		confirm.setVisibility(View.VISIBLE);
		cancel.setVisibility(View.VISIBLE);
		runJavascript("javascript:addButton()");
	}
	
	public void runJavascript(final String url){
		webview.loadUrl(url);
	}
}
