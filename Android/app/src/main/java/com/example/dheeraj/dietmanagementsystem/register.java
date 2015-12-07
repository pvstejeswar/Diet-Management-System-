package com.example.dheeraj.dietmanagementsystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;



public class register extends ActionBarActivity implements View.OnClickListener {

    Button bRegister, bCamera;
    EditText etName, etAge, etUserName, etPassword, etGender;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = (EditText) findViewById(R.id.etName);
        etAge =  (EditText) findViewById(R.id.etAge);
        etGender = (EditText) findViewById(R.id.etGender);
        etUserName = (EditText) findViewById(R.id.etUserName);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bRegister = (Button) findViewById(R.id.bRegister);

        bRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bRegister:

                String name = etName.getText().toString();
                String gender = etGender.getText().toString();
                String username = etUserName.getText().toString();
                String password = etPassword.getText().toString();
                int age = Integer.parseInt(etAge.getText().toString());
//add gender to the user category here------>>
                user user = new user(name, age, username, password);

                registerUser(user);
                break;
        }
    }

    private void registerUser(user user){
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.storeUserDataInBackground(user, new GetUserCallback() {
            @Override
            public void done(user returnedUser) {
                startActivity(new Intent(register.this, login.class));

            }
        });    }

}
