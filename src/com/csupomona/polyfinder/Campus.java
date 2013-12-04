package com.csupomona.polyfinder;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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
	private DrawerItem[] itemList;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    DrawerAdapter<DrawerItem> dAdapter;
  

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_campus);
		
		setDrawer();
		
		webview = (WebView) findViewById(R.id.map);
		webview.setWebChromeClient(new WebChromeClient());
		WebSettings webSettings = webview.getSettings();
		webSettings.setJavaScriptEnabled(true);
		//webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        WebViewInterface wInterface = new WebViewInterface(context, this);
		webview.addJavascriptInterface(wInterface, "WebViewInterface");
		webview.loadUrl("file:///android_asset/leaflet/index.html");
		setReference();
			
		
	}
	
	
	
	void setDrawer(){
		itemList = new DrawerItem[]{
			new DrawerItem(R.drawable.all_60, "ALL"),	
			new DrawerItem(R.drawable.meeting_60, "Event"),
			new DrawerItem(R.drawable.hangout_60, "Hangout"),
			new DrawerItem(R.drawable.party_60, "Party")
		};
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        
        dAdapter=new DrawerAdapter<DrawerItem>(this, R.layout.drawer_item, itemList);
        
        mDrawerList.setAdapter(dAdapter);
        
        mDrawerList.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView parent, View view, int position, long id) {
				
				Context context = getApplicationContext();
				int duration = Toast.LENGTH_SHORT;

				
				mDrawerList.setItemChecked(position, true);
				mDrawerLayout.closeDrawer(mDrawerList);
			}        	
        });
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

    public void campDialog(String title, String arg){

        AlertDialog.Builder build = new AlertDialog.Builder(context);
        DialogInterface.OnClickListener listen = new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int button){
                if (button == DialogInterface.BUTTON_POSITIVE){
                    dialog.dismiss();
                    Thread th = new Thread(){
                        public void run(){
                            Intent i = new Intent("com.csupomona.polyfinder.DISPLAYCOMMENTS");
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.putExtra("key","event_id");
                            context.startActivity(i);
                        }
                    };
                    th.start();
                }
            }
        };
        build.setPositiveButton("Comments",listen);
        build.setNegativeButton("Like", listen);
        build.setMessage(arg);
        build.setTitle(title);
        build.setView(findViewById(R.layout.event_content));
        dialog = build.create();

		//Dialog Position
		Window window = dialog.getWindow();
		WindowManager.LayoutParams wlp = window.getAttributes();

		wlp.gravity = Gravity.TOP;
		wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		window.setAttributes(wlp);

		dialog.show();
    }

}
