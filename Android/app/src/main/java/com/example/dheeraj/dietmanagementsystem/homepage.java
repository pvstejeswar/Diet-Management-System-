package com.example.dheeraj.dietmanagementsystem;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.lang.reflect.Array;


public class homepage extends Activity implements AdapterView.OnItemSelectedListener{

    Spinner spinner_hgt;
    Button takephoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        spinner_hgt = (Spinner) findViewById(R.id.spinner_height);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.height, android.R.layout.simple_spinner_item);
        spinner_hgt.setAdapter(adapter);
        spinner_hgt.setOnItemSelectedListener(this);

        //button
    }

    //Spinner code
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        TextView myText = (TextView) view;
        Toast.makeText(this, "You selected " +myText.getText(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}
