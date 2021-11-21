package com.example.easyjob;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.Button;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    SeekBar seekBar;  //declare seekbar object
    TextView textView;
    //declare member variables for SeekBar
    int discrete = 0;
    int start = 50;
    int start_position = 50; //progress tracker
    int temp = 0;
    //declare objects for ViewStub
    ViewStub stub;
    CheckBox checkBox;
    ListView lv; //declare Listview object
    String msg = "Android : ";
    ImageView goDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //declare viewstub object
        stub = findViewById(R.id.viewStub1);
        @SuppressWarnings("unused")
        View inflated = stub.inflate();
        stub.setVisibility(View.VISIBLE);
        lv = (ListView) findViewById(R.id.lv);
        lv.setAdapter(LoadListViewData());//bind controller for ListView
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView arg0, View arg1, int position, long arg3) {
                System.out.println("onItemClick_position:"+position);
                Intent i = new Intent(MainActivity.this, Detail.class);
                i.putExtra("position", position);
                startActivity(i);
            }
        });
    }

    public SimpleAdapter LoadListViewData(){
        /*Define a dynamic temperature array*/
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String,Object>>();
        String[] wkTitle = new String[]{
                "Android Developer",
                "Test Engineering",
                "Front-end Developer",
                "UI Designer",
                "Product manager"};
        String[] wkText = new String[]{
                "Google",
                "FaceBook",
                "Amazon",
                "Linkedin",
                "Tiktok",
        };
        /*Define a dynamic weather icon array*/
        int[] iconWeather = new int[]{
                R.drawable.google,
                R.drawable.facebook,
                R.drawable.amazon,
                R.drawable.linkedin,
                R.drawable.tiktok,
        };
        /*store data for the array*/
        for(int i=0;i<5;i++)
        {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemImage", iconWeather[i]);//add image to hashmap
            map.put("ItemTitle", wkTitle[i]);
            map.put("ItemText", wkText[i]);
            listItem.add(map);
        }

        SimpleAdapter mSimpleAdapter = new SimpleAdapter(this,listItem,//Data to bind
                R.layout.item,//layout of each item
                //The data source keys in the dynamic array correspond to the View that defines the layout
                new String[] {"ItemImage","ItemTitle", "ItemText"},
                new int[] {R.id.ItemImage,R.id.ItemTitle,R.id.ItemText}
        );
        return mSimpleAdapter;
//        System.out.println("mSimpleAdapter:"+mSimpleAdapter);
//
//        goDetail = findViewById(R.id.goDetail);//get the button view
//        System.out.println("goDetail"+goDetail);


    }
}