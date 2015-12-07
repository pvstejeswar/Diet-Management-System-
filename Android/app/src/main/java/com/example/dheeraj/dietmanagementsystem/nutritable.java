package com.example.dheeraj.dietmanagementsystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class nutritable extends ActionBarActivity implements View.OnClickListener {

    Button goback;
    TextView r1,c1,safetoeat, moreinfo;
    TableLayout table_layout;
    TableRow table_row;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutritable);

        goback = (Button) findViewById(R.id.goback);
        table_layout = (TableLayout)findViewById(R.id.main_table_layout);
        safetoeat = (TextView) findViewById(R.id.safetoeat);
        moreinfo = (TextView) findViewById(R.id.moreinfo);
        table_layout.setColumnStretchable(0, true);
        table_layout.setColumnStretchable(0, true);

        goback.setOnClickListener(this);
        String local_info = Global.nutri_info;
        List nutr = Arrays.asList(local_info.split(","));
        HashMap<String, String> nutri_details = new HashMap<>();

        for(int i=0;i<nutr.size();i++)
        {
            String nutr_name = nutr.get(i).toString().split(":")[0];
           // nutr_name = nutr_name.replaceAll("\"", "");
            nutr_name = nutr_name.replaceAll("\\{" , "");
            String nutr_info = nutr.get(i).toString().split(":")[1];
          //  nutr_info = nutr_info.replaceAll("\"", "");
            nutr_info = nutr_info.replaceAll("\\}" , "");


            if (nutr_name.contains("final_result"))  {

            safetoeat.setText(nutr_info);
            }
            else if(nutr_name.contains("nutrient_ref"))
            {
                moreinfo.setText(nutr_info + nutr.get(i).toString().split(":")[2]);
            }
            else
            {
                nutri_details.put(nutr_name, nutr_info);
            }

            //moreinfo
//            if(nutri_details.get(nutr_info).equalsIgnoreCase("SAFE"))
//            {
//                //safetoeat.setHighlightColor(0x00FF00);
//
//                safetoeat.setTextColor(0x00FF00);
//            }
        }

//        nutri_details.put("a", "b");
//        nutri_details.put("c", "d");
//        nutri_details.put("e", "f");

        int value = 4;

        for(Map.Entry<String, String>entry: nutri_details.entrySet())
        {
            String key = entry.getKey();
            String key2 = entry.getValue();
            table_row = new TableRow(this);
            r1 = new TextView(this);
            c1 = new TextView(this);
            r1.setText(key);
            c1.setText(key2);
            r1.setTextSize(18);
            r1.setGravity(Gravity.CENTER);
            c1.setTextSize(18);
            c1.setGravity(Gravity.CENTER);
            table_row.addView(r1);
            table_row.addView(c1);
            table_layout.addView(table_row);
        }
//        for(int v =0; v<value;v++)
//        {
//            table_row = new TableRow(this);
//            r1 = new TextView(this);
//            c1 = new TextView(this);
//            r1.setText("Proteins");
//
//            c1.setText("529");
//            r1.setTextSize(18);
//            r1.setGravity(Gravity.CENTER);
//            c1.setTextSize(18);
//            c1.setGravity(Gravity.CENTER);
//            table_row.addView(r1);
//            table_row.addView(c1);
//            table_layout.addView(table_row);
//        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.goback:
                startActivity(new Intent(this, Camera_Activity.class));
                break;
        }
    }
}
