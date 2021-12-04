package com.example.phase2_1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import 	android.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ListView homeListView;
    private List<Offer> listOffers;
    private ProgressDialog dialog;
    private SearchView searchView;
    private String user_id;
    private Map user;

    SwipeRefreshLayout swipeHome;
    private FirebaseFirestore db;
    private static final String TAG = "DocSnippets";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
        db = FirebaseFirestore.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        homeListView = (ListView) findViewById(R.id.homeListView);
        listOffers = new ArrayList<>();
        dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.waiting));
        swipeHome = (SwipeRefreshLayout)findViewById(R.id.swipeHome);
        swipeHome.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeHome.setRefreshing(false);
            }
        });

        getSupportActionBar().setTitle("Job List");
        toolbar.setTitleTextColor(Color.parseColor("#ecf0f1"));

        user = (HashMap)getIntent().getSerializableExtra("user");
        Log.i("debug","DEBUGME ==================================>  user = "+ user );
        Log.i("debug","DEBUGME => id user = "+ user.get("username") );
        homeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(HomeActivity.this,OfferActivity.class);
                intent.putExtra("offer_id",listOffers.get(position).getId());
//                intent.putExtra("user_id",user_id);
                intent.putExtra("user", (Serializable) user);
                startActivity(intent);
            }
        });

        //get job list data
        db.collection("jobs")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String id = document.getId();
                                String title = document.getData().get("title").toString();
                                String place = document.getData().get("place").toString();
//                                String contract = document.getData().get("contract").toString();
//                                String dateCreate = document.getData().get("data").toString();
                                String company = document.getData().get("company").toString();
                                Offer offer = new Offer(id,title,place,company);
                                listOffers.add(offer);
                            }
                            Toast.makeText(HomeActivity.this, listOffers.get(0).toString(), Toast.LENGTH_SHORT).show();
                            CustomAdaptder customAdaptder = new CustomAdaptder();
                            homeListView.setAdapter(customAdaptder);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        OfferServer offerServer = new OfferServer();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_moncv) {
            Intent intent = new Intent(HomeActivity.this,MoncvActivity.class);
            //intent.putExtra("user_id",user_id);
            intent.putExtra("user",(Serializable) user);
            startActivity(intent);
        } else if (id == R.id.nav_favoris) {
            Intent intent = new Intent(HomeActivity.this,FavorisActivity.class);
            //intent.putExtra("user_id",user_id);
            intent.putExtra("user",(Serializable) user);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    class CustomAdaptder extends BaseAdapter{

        @Override
        public int getCount() {
            return listOffers.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup parent) {
            view = getLayoutInflater().inflate(R.layout.offer_layout, null);

            TextView title = (TextView)view.findViewById(R.id.offerTitle);
            TextView place = (TextView)view.findViewById(R.id.offerPlace);
            TextView contract = (TextView)view.findViewById(R.id.offerContract);
            TextView date = (TextView)view.findViewById(R.id.offerDate);
            TextView company = (TextView)view.findViewById(R.id.companyName);

            title.setText(listOffers.get(i).getTitle());
            place.setText(listOffers.get(i).getPlace());
            contract.setText(listOffers.get(i).getContract());
            company.setText(listOffers.get(i).getCompany());

            //date
            String dateStr = listOffers.get(i).getDateCreate();
            SimpleDateFormat  sdf = new SimpleDateFormat("yyyy-MM-dd ");
            long numberOfDay=0;
            try{
                long CONST_DURATION_OF_DAY = 1000l * 60 * 60 * 24;
                Date  datee = sdf.parse(dateStr);
                Date today = new Date();
                long diff = Math.abs(datee.getTime() - today.getTime());
                 numberOfDay = (long)diff/CONST_DURATION_OF_DAY;
            }catch (Exception e){
                e.printStackTrace();
               // date.setText(listOffers.get(i).getDateCreate());
            }
            return view;
        }
    }

    protected class InfoServer extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... params) {
            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(params[0]);
                ResponseHandler<String> buffer = new BasicResponseHandler();
                String result = client.execute(get, buffer);
                return result;

            }catch (Exception e){
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            try{
                JSONObject json = new JSONObject(s);
                TextView username = (TextView)findViewById(R.id.username);
                TextView email = (TextView)findViewById(R.id.email);
                username.setText(json.getString("username"));
                email.setText(json.getString("email"));

            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}
