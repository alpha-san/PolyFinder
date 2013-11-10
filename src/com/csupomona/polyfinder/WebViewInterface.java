package com.csupomona.polyfinder;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.widget.TextView;

public class WebViewInterface {
	
	Context c;
	Dialog dialog;
	TextView tv;
	
	WebViewInterface(Context c){
		this.c = c;
	}

	@JavascriptInterface
	public void eventDialog(String title, String arg){
		dialog = new Dialog(c);
		dialog.setContentView(R.layout.event_content);
		tv = (TextView) dialog.findViewById(R.id.content);
		
		//Dialog Position
		Window window = dialog.getWindow();
		WindowManager.LayoutParams wlp = window.getAttributes();

		wlp.gravity = Gravity.TOP;
		wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		window.setAttributes(wlp);
		
		dialog.setTitle(title);
		tv.setText(arg);
		dialog.show();
	}
}
