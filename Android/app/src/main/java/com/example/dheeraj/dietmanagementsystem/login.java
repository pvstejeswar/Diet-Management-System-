package com.example.dheeraj.dietmanagementsystem;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.dheeraj.dietmanagementsystem.homepage;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class login extends ActionBarActivity implements View.OnClickListener{

    //For accessing the fields on the .XML page
    Button bLogin;
    EditText etUsername, etPassword;
    TextView registerHere;

    //For storing the registration details
    UserLocalStore userlocalstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Link the variables to the respective field id's
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bLogin = (Button) findViewById(R.id.bLogin);
        registerHere = (TextView) findViewById(R.id.registerhere);

        //What happens when these button are clicked?
        bLogin.setOnClickListener(this);
        registerHere.setOnClickListener(this);

        userlocalstore = new UserLocalStore(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bLogin:
//                System.out.println("hey");
//                Log.d("my tag", "This is my message");
//                int a =9;
//                a=10;
//
//
//                    Thread thread = new Thread(new Runnable(){
//                        @Override
//                        public void run() {
//                            try {
//                                //Your code goes here
//                                System.out.println("1");
//                                HttpClient client = new DefaultHttpClient();
//                                System.out.println("1");
//                                HttpGet request = new HttpGet("http://192.168.43.4:8080/cmpe220/webapi/messages");
//                                System.out.println("1");
//                                HttpResponse response =  client.execute(request);
//                                System.out.println("2");
//                                System.out.println(response);
//                                BufferedReader r = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
//
//                                StringBuilder total = new StringBuilder();
//
//                                String line = null;
//
//                                while ((line = r.readLine()) != null) {
//                                    total.append(line);
//                                }
//                                System.out.println(total);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                                System.out.println(e.getMessage());
//                            }
//                        }
//                    });
//
//                    thread.start();

                //Username is made global to access it from multiple activities - Global is a class
                Global.getusername = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                user user = new user(Global.getusername, password);
                Global.getusername = "Team25";
                password = "passkey";
                //authenticate(user);
                // System.out.println(username);
                System.out.println(password);
                if(Global.getusername.equals("Team25")&& password.equals("passkey"))
                    startActivity(new Intent(this, Camera_Activity.class));
                else
                    showErrorMessage();
                break;

            case R.id.registerhere:
                //startActivity(new Intent(this, register.class));
                startActivity(new Intent(this, register.class));
                break;
        }
    }

    private void authenticate(user user){
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.fetchUserDataInBackground(user, new GetUserCallback() {
            @Override
            public void done(user returnedUser) {
                if(returnedUser == null){
                    showErrorMessage();
                }else{
                    loguserIn(returnedUser);
                }
            }
        });
    }

    private void showErrorMessage(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(login.this);
        dialogBuilder.setMessage("Incorrect user details");
        dialogBuilder.setPositiveButton("Ok",null);
        dialogBuilder.show();
    }

    private void loguserIn(user returnedUser){
        userlocalstore.storeUserData(returnedUser);
        userlocalstore.setUserLoggedIn(true);

        startActivity(new Intent(this, MainActivity.class));
    }

}
