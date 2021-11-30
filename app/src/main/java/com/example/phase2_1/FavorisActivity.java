package com.example.phase2_1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.QueryDocumentSnapshot;

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
import java.util.List;
import java.util.*;
public class FavorisActivity extends AppCompatActivity {
    ListView listView;
    List<Offer> listOffers;
    String user_id;
    private Map user;
    ProgressDialog dialog;
    TextView textFav;
    private static final String TAG = "DocSnippets";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoris);

        user = (HashMap)getIntent().getSerializableExtra("user");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_fav);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Vos favoris");
        toolbar.setTitleTextColor(Color.parseColor("#ecf0f1"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView)findViewById(R.id.listview_fav);
        listOffers = new ArrayList<>();

        dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.waiting));

        textFav = (TextView)findViewById(R.id.textFav);
        textFav.setVisibility(View.INVISIBLE);

        Map<String,Object> favoriteJobs = new HashMap<>();
        favoriteJobs = (Map) user.get("favoriteJobs");

        for (String key : favoriteJobs.keySet()) {
            Map<String,Object> favoriteVal = new HashMap<>();
            favoriteVal = (Map) favoriteJobs.get(key);

            System.out.println("Key = " + key + ", Value = " + favoriteVal);
            String id = key;
            String title = favoriteVal.get("title").toString();
            String place = favoriteVal.get("place").toString();
            String contract = favoriteVal.get("contract").toString();
            String dateCreate = favoriteVal.get("data").toString();
            String company = favoriteVal.get("company").toString();
            Offer offer = new Offer(id,title,place,contract,dateCreate,company);
            listOffers.add(offer);
        }
        Toast.makeText(FavorisActivity.this, listOffers.get(0).toString(), Toast.LENGTH_SHORT).show();
        CustomAdaptderFav customAdaptder = new CustomAdaptderFav();
        listView.setAdapter(customAdaptder);

        //user_id = getIntent().getStringExtra("user_id");
        FavServer favServer = new FavServer();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(FavorisActivity.this,OfferActivity.class);
                intent.putExtra("offer_id",String.valueOf(listOffers.get(position).getId()));
                //intent.putExtra("user_id",user_id);
                intent.putExtra("user",(Serializable) user);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        FavServer favServer = new FavServer();
    }

    protected class FavServer extends AsyncTask<String,Void,String>{
        @Override
        protected void onPreExecute() {
            //dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(params[0]);
                ResponseHandler<String> buffer = new BasicResponseHandler();
                String result = client.execute(get, buffer);
                return result;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            //dialog.dismiss();

            if(s == null){
                Toast.makeText(FavorisActivity.this, "error", Toast.LENGTH_SHORT).show();
                return;
            }

            try{
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = new JSONArray(jsonObject.getString("offers"));
                listOffers.clear();
                for (int i = 0 ; i < jsonArray.length();i++){
                    JSONObject element = jsonArray.getJSONObject(i);

                    Offer offer = new Offer(element.getString("id"),element.getString("title"),element.getString("place"),element.getString("contract"),element.getString("dateCreate"),element.getString("company"));
                    listOffers.add(offer);
                }
                if(listOffers.isEmpty()){
                    textFav.setVisibility(View.VISIBLE);
                }
                CustomAdaptderFav customAdaptder = new CustomAdaptderFav();
                listView.setAdapter(customAdaptder);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    class CustomAdaptderFav extends BaseAdapter{

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
}
