package com.csupomona.polyfinder;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class Campus extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_campus);
		
		WebView wb = (WebView) findViewById(R.id.map);
		wb.loadUrl("file:///android_asset/leaflet/index.html");
		
		WebSettings webSettings = wb.getSettings();
		webSettings.setJavaScriptEnabled(true);
	}

}
