package com.csupomona.polyfinder;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Campus extends Activity {
	
	WebView webview;
	ImageView addEvent;
	ImageView confirm;
	ImageView cancel;
	Context context = this;
	Dialog dialog;
	String arg;
	Button submit;
	boolean selection = false;
	RadioGroup radio;
	String description;
	int type;

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
					dialog.setContentView(R.layout.post_it);		
					dialog.setTitle("Create Event");
					submit = (Button) dialog.findViewById(R.id.submit);
					radio = (RadioGroup) dialog.findViewById(R.id.rbutton);
	
					submit.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							description = ((EditText) dialog.findViewById(R.id.eventInput)).getText().toString();
							type = radio.getCheckedRadioButtonId();
							
							if(!description.isEmpty() && type != -1){
								//webview.loadUrl("javascript:alertMe('" + description + "')");
								confirm.setVisibility(View.VISIBLE);
								cancel.setVisibility(View.VISIBLE);
								dialog.dismiss();
								webview.loadUrl("javascript:addButton()");
								selection = true;
							}
							else {
								Toast t = Toast.makeText(context, "Please fill out all required information", Toast.LENGTH_SHORT);
								t.show();
							}
						}
					});
					
					dialog.show();
				}
			}
		});
		
		confirm.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				confirm.setVisibility(View.INVISIBLE);
				cancel.setVisibility(View.INVISIBLE);
				webview.loadUrl("javascript:yesButton('" + type + "', '" + description + "')");
				
				//reset value
				description = "";
				type = -1;
				selection = false;
			}
		});
		
		cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				confirm.setVisibility(View.INVISIBLE);
				cancel.setVisibility(View.INVISIBLE);
				webview.loadUrl("javascript:noButton()");
				
				//reset
				description = "";
				type = -1;
				selection = false;
			}
		});
	}
}
