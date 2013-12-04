package com.csupomona.polyfinder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.simplelogin.SimpleLogin;
import com.firebase.simplelogin.SimpleLoginAuthenticatedHandler;
import com.firebase.simplelogin.User;
import com.firebase.simplelogin.enums.*;

public class Signup extends Activity{

    Button reg, cancel;
    EditText name, email, pass1, pass2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        reg = (Button) findViewById(R.id.register);
        cancel = (Button) findViewById(R.id.regCancel);
        email= (EditText) findViewById(R.id.up_email);
        pass1 = (EditText) findViewById(R.id.up_pwd);
        pass2 = (EditText) findViewById(R.id.up_pwd2);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String e_mail = email.getText().toString();
                String password1 = pass1.getText().toString();
                String password2 = pass2.getText().toString();

                if (e_mail.equals("")|| password1.equals("") || password2.equals("") ){
                    showToast("Error: must fill in all fields");
                } else if(!password1.equals(password2)){
                    showToast("First password does not match the second");
                }else {
                    SimpleLogin auth = new SimpleLogin(new Firebase("https://polyfindertest.firebaseio.com"));
                    auth.createUser(e_mail,password1,new SimpleLoginAuthenticatedHandler() {
                        @Override
                        public void authenticated(com.firebase.simplelogin.enums.Error error, User user) {
                            if(error != null){
                                //if user is already used
                            } else {
                                showToast("Successful Registration!");
                            }
                        }
                    });
                }
            }
        });
	}
	
	private void showToast(String s){
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }
}
