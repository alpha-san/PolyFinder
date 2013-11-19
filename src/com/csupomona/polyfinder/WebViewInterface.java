package com.csupomona.polyfinder;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class WebViewInterface {
	
	Context c;
    public String title,arg,event_id;
    Campus camp;
	
	WebViewInterface(Context c, Campus camp){
		this.c = c;
        this.camp = camp;
	}



	@JavascriptInterface
	public void showToast(String arg){
		Toast t = Toast.makeText(c, arg, Toast.LENGTH_SHORT);
		t.show();
	}
	
	@JavascriptInterface
	public void eventDialog(String title, String arg){
        // tell campus to make the dialog
        this.arg= arg;
        this.title =title;
        camp.campDialog(title,arg);
    }

    public void eventDialog(String title, String arg, String event_id){
        this.arg=arg;
        this.title = title;
        this.event_id = event_id;
    }

       /* AlertDialog.Builder build = new AlertDialog.Builder(c);
        DialogInterface.OnClickListener listen = new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int button){
                if (button == DialogInterface.BUTTON_POSITIVE){
                    Intent i = new Intent("com.csupomona.polyfinder.DISPLAYCOMMENTS");
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    c.startActivity(i);
                }
                else showToast("Like++");
            }
        };
        build.setPositiveButton("Comments",listen);
        build.setNegativeButton("Like", listen);
        build.setMessage(arg);
        build.setTitle(title);
        build.setView(v);
        AlertDialog dialog = build.create();

		//Dialog Position
		Window window = dialog.getWindow();
		WindowManager.LayoutParams wlp = window.getAttributes();

		wlp.gravity = Gravity.TOP;
		wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		window.setAttributes(wlp);

		dialog.show();
    }*/
}
