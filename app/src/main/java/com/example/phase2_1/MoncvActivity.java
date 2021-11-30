package com.example.phase2_1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import 	androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MoncvActivity extends AppCompatActivity {
    String user_cv;
    ListView expListview,formationListview;
    ProgressDialog dialog;
    List<Formations> listFormations;
    List<Experiences> listExperiences;
    TextView name,email,adress,tel,age;
    LinearLayout linearLayoutExp,linearLayoutFormation;
    boolean testEffacer = true;
    private Map user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moncv);

        dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.waiting));

        Toolbar toolbar = (Toolbar)findViewById(R.id.moncvToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Mon CV");
        toolbar.setTitleTextColor(Color.parseColor("#ecf0f1"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        user_cv = getIntent().getStringExtra("user_cv");
        user = (HashMap)getIntent().getSerializableExtra("user");

        listFormations = new ArrayList<>();
        listExperiences = new ArrayList<>();
        name =(TextView)findViewById(R.id.cvName);
        email =(TextView)findViewById(R.id.cvEmail);
        adress =(TextView)findViewById(R.id.cvAdress);
        tel =(TextView)findViewById(R.id.cvTel);
        age =(TextView)findViewById(R.id.cvAge);

        linearLayoutExp = (LinearLayout)findViewById(R.id.linearCvExp);

        linearLayoutFormation = (LinearLayout)findViewById(R.id.linearCvFormation);
        moncvServer();
        Log.i("debug","enter in onCreate ....");
        testEffacer = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(testEffacer) {
            if (linearLayoutExp.getChildCount() > 0)
                linearLayoutExp.removeAllViews();

            if (linearLayoutFormation.getChildCount() > 0)
                linearLayoutFormation.removeAllViews();

              moncvServer();
        }
        Log.i("debug","enter in onResume ....");
        testEffacer = true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.menuModifcv){
            Intent intent = new Intent(MoncvActivity.this,ModifcvActivity.class);
            intent.putExtra("user_cv",user_cv);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cv, menu);
        return true;
    }

        public void  moncvServer() {
            Map<String,Object> userCv = new HashMap<>();
            userCv = (Map) user.get("resume");
            System.out.println("user=========================================>");
            System.out.println(user);
            //dialog.dismiss();
            if (userCv == null) {
                Toast.makeText(MoncvActivity.this, "erreur de connexion", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                Log.i("debug", "enter in async");
                JSONObject jsonObject = new JSONObject(userCv);
                Log.i("debug", jsonObject.getString("firstName") + " " + jsonObject.getString("tel") + " " + jsonObject.getString("adress") + " " + jsonObject.getInt("age"));

                if(jsonObject.getString("firstName") != "null" && jsonObject.getString("lastName") != "null" ) {
                    name.setText(jsonObject.getString("firstName") + " " + jsonObject.getString("lastName"));
                }
                if(jsonObject.getString("email") != "null") {
                    email.setText(jsonObject.getString("email"));
                }
                if (jsonObject.getInt("age") != 0) {
                    age.setText(String.valueOf(jsonObject.getInt("age")) + "years old");
                }
                if(!jsonObject.getString("tel").equals("0")) {
                    tel.setText(jsonObject.getString("tel"));
                }
                if (jsonObject.getString("adress") !="null") {
                    adress.setText(jsonObject.getString("adress"));
                }
                Map<String,Object> formationsMap = new HashMap<>();
                formationsMap = (Map) userCv.get("formations");

                Map<String,Object> experiencesMap = new HashMap<>();
                experiencesMap = (Map) userCv.get("experiences");

                System.out.println("experiencesMap============================>");
                System.out.println(experiencesMap);

                System.out.println("formationsMap============================>");
                System.out.println(formationsMap);

                listExperiences.clear();
                listFormations.clear();

                JSONObject jsonObjectExperiences = new JSONObject(experiencesMap);
                JSONObject jsonObjectFormations = new JSONObject(formationsMap);

                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View view = inflater.inflate(R.layout.row_experience2, null);

                    TextView dateDebut = (TextView)view.findViewById(R.id.dateDebut);
                    TextView dateFin = (TextView)view.findViewById(R.id.dateFin);
                    TextView expPosition = (TextView)view.findViewById(R.id.expPosition);
                    TextView expCompany = (TextView)view.findViewById(R.id.expCompany);
                    TextView expAbout = (TextView)view.findViewById(R.id.expAbout);

                    if(jsonObjectExperiences.getString("begin") != "null") {
                        dateDebut.setText(jsonObjectExperiences.getString("begin"));
                    }
                    if(jsonObjectExperiences.getString("end") != "null") {
                        dateFin.setText(jsonObjectExperiences.getString("end"));
                    }
                    if(jsonObjectExperiences.getString("position") != "null") {
                        expPosition.setText(jsonObjectExperiences.getString("position"));
                    }
                    if(jsonObjectExperiences.getString("company") != "null") {
                        expCompany.setText(jsonObjectExperiences.getString("company"));
                    }
                    if (jsonObjectExperiences.getString("about") != "null") {
                        expAbout.setText(jsonObjectExperiences.getString("about"));
                    }
                    linearLayoutExp.addView(view,linearLayoutExp.getChildCount());
                    LayoutInflater formationInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View formationView = formationInflater.inflate(R.layout.row_formation2, null);

                    TextView dateFormation = (TextView)formationView.findViewById(R.id.dateFormation);
                    TextView nameFormation = (TextView)formationView.findViewById(R.id.nameFormation);
                    TextView schoolFormation = (TextView)formationView.findViewById(R.id.schoolformation);

                    if (jsonObjectFormations.getString("date") != "null") {
                        dateFormation.setText(jsonObjectFormations.getString("date"));
                    }
                    if(jsonObjectFormations.getString("name") != "null") {
                        nameFormation.setText(jsonObjectFormations.getString("name"));
                    }
                    if(jsonObjectFormations.getString("school") != "null") {
                        schoolFormation.setText(jsonObjectFormations.getString("school"));
                    }
                    linearLayoutFormation.addView(formationView,linearLayoutFormation.getChildCount());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
}
