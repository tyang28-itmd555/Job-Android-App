package com.example.phase2_1;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.Lists;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OfferActivity extends AppCompatActivity  implements OnMapReadyCallback {
    private String offer_id,id;
    private Double lon,lat;
    private Offer offer;
    private ProgressDialog dialog;
    private TextView title,company,contract,salary,location,about;
    MenuItem item;
    int test_fav;
    FrameLayout map;
    private View.OnTouchListener mListener;
    private static final String TAG = "DocSnippets";
    private FirebaseFirestore db;
    private GoogleMap mMap;
    private Map jobDetailData;//jobDetailData
    private Map user;
    private Map<String,Object> favoriteJobs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);
        db = FirebaseFirestore.getInstance();

        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar_offer);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Job Detail");
        toolbar.setTitleTextColor(Color.parseColor("#ecf0f1"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.waiting));
        favoriteJobs = new HashMap<>();
        user = new HashMap();
        user = (HashMap)getIntent().getSerializableExtra("user");
        favoriteJobs = (Map) user.get("favoriteJobs");
        offer_id = getIntent().getStringExtra("offer_id");

        System.out.println("lon------------------------");
        System.out.println(lon);

        title = (TextView)findViewById(R.id.offer_title);
        company = (TextView)findViewById(R.id.offer_company);
        contract = (TextView)findViewById(R.id.offer_contract);
        salary = (TextView)findViewById(R.id.offer_salary);
        location = (TextView)findViewById(R.id.offer_location);
        about = (TextView)findViewById(R.id.offer_about);

        //get job Detail
        db.collection("jobDetail")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                jobDetailData = document.getData();
                                Log.d(TAG, document.getId() + " ==> " + document.getData());
                                if(document.getId().trim().equals(offer_id)){
                                    Log.d(TAG, document.getId() + "  ===> " + document.getData());
                                    title.setText(document.getData().get("title").toString());
                                    contract.setText(document.getData().get("contract").toString());
                                    salary.setText(document.getData().get("salary").toString());
                                    location.setText(document.getData().get("place").toString());
                                    about.setText(document.getData().get("description").toString());
                                    company.setText(document.getData().get("company").toString());
                                }
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.offer_action_bar, menu);
        item = menu.findItem(R.id.fav);
        System.out.println("item");
        System.out.println(item);

        VerifServer();

        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(test_fav == 0) {
                    System.out.println("will be invok AddFavServer");
                    AddFavServer();
                }
                else if(test_fav == 1) {
                    System.out.println("will be invok removeFavServer");
                    RemoveFavServer();
                }
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        System.out.println("jobLonLat in onMapReady function --------------------");
        lon = (Double) getIntent().getSerializableExtra("lon");
        lat = (Double) getIntent().getSerializableExtra("lat");
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(lat, lon);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public void VerifServer (){
        //judge the user whether favorite this job
        if( user.containsKey("favoriteJobs") && ((Map<?, ?>) user.get("favoriteJobs")).containsKey(offer_id)){
            //initial favorite icon
            item.setIcon(R.drawable.ic_star_white_24dp);
            test_fav = 1;
        }else {
            test_fav = 0;
            item.setIcon(R.drawable.ic_star_border_black_24dp);
        }
    }

    protected void AddFavServer (){
        CollectionReference usersCollection = db.collection("users");

        favoriteJobs = (Map) user.get("favoriteJobs");
        favoriteJobs.put(offer_id,jobDetailData);

        user.put("favoriteJobs",favoriteJobs);

        usersCollection.document(user.get("username").toString()).set(user);

        Toast.makeText(OfferActivity.this, "Add to favorite!" , Toast.LENGTH_LONG).show();
        item.setIcon(R.drawable.ic_star_white_24dp);
        test_fav = 1;

    }

    protected void RemoveFavServer (){
        CollectionReference usersCollection = db.collection("users");
        favoriteJobs = (Map) user.get("favoriteJobs");
        favoriteJobs.remove(offer_id);

        user.put("favoriteJobs",favoriteJobs);

        usersCollection.document(user.get("username").toString()).set(user);

        Toast.makeText(OfferActivity.this, "remove from favorite!" , Toast.LENGTH_LONG).show();
        item.setIcon(R.drawable.ic_star_border_black_24dp);
        test_fav = 0;
    }
}
