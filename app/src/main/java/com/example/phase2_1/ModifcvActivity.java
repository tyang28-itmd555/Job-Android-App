package com.example.phase2_1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModifcvActivity extends AppCompatActivity {
    LinearLayout linearLayoutP ;
    LinearLayout linearLayoutP2 ;
    Map user_cv,user,resumeExeperience,resumeFormation;
    String mFirstName,mLastName,mAdress,mTel,mEmail;
    EditText lastName,firstName,email,adress,tel;
    List<String> expAsupp,formationAsupp;
    String midExp,mbegin,mend,mposition,mcompany,mabout;
    String midFormation,mdate,mname,mschool;
    int a = 0;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifcv);
        db = FirebaseFirestore.getInstance();

        Toolbar toolbar = (Toolbar)findViewById(R.id.modifcvToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Modify My CV");
        toolbar.setTitleTextColor(Color.parseColor("#ecf0f1"));
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        user_cv = (HashMap)getIntent().getSerializableExtra("userCv");
        user = (HashMap)getIntent().getSerializableExtra("user");

        expAsupp = new ArrayList<>();
        formationAsupp = new ArrayList<>();

        linearLayoutP = (LinearLayout) findViewById(R.id.linearLayoutModifcv);
        linearLayoutP2 = (LinearLayout) findViewById(R.id.linearLayoutModifcv2);

        firstName =(EditText)findViewById(R.id.txtPrenom);
        lastName =(EditText)findViewById(R.id.txtNom);
        email =(EditText)findViewById(R.id.txtEmail);
        adress =(EditText)findViewById(R.id.txtAdresse);
        tel =(EditText)findViewById(R.id.txtTel);

        infoCvServer();//render the cv information
    }
    public void onAddRow(View v) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.row_modif_experience, null);
        linearLayoutP.addView(rowView, linearLayoutP.getChildCount() );
        TextView idExp = (TextView) rowView.findViewById(R.id.idExp);
        idExp.setText("-1");
        Log.i("debug","1111");
    }
    public void onAddRow2(View v) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.row_modif_formation, null);
        TextView idFormation = (TextView) rowView.findViewById(R.id.idFormation);
        idFormation.setText("-1");
        linearLayoutP2.addView(rowView, linearLayoutP2.getChildCount() );
    }
    public void onDelete(View v) {
        View view = (View) v.getParent();
        TextView idExp = (TextView)view.findViewById(R.id.idExp);
        if(!idExp.getText().toString().equals("-1") ){
            expAsupp.add(idExp.getText().toString());
            Log.i("debug","idExp a supp => "+idExp.getText().toString());
        }
        linearLayoutP.removeView((View) v.getParent());
    }
    public void onDelete2(View v) {
        View view = (View) v.getParent();
        TextView idFormation = (TextView)view.findViewById(R.id.idFormation);
        if(!idFormation.getText().toString().equals("-1") ){
            formationAsupp.add(idFormation.getText().toString());
            Log.i("debug","idFormation a supp => "+idFormation.getText().toString());
        }
        linearLayoutP2.removeView((View) v.getParent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        System.out.println("onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.menu_modif_cv, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuModifcvValider){

             mFirstName = firstName.getText().toString();
             mLastName = lastName.getText().toString();
             mAdress = adress.getText().toString();
             mTel = tel.getText().toString();
             mEmail = email.getText().toString();

            for (int i = 0 ; i < linearLayoutP.getChildCount() ; i++ ){
                View rowView = linearLayoutP.getChildAt(i);
                EditText debut = (EditText)rowView.findViewById(R.id.txtDebut);
                EditText fin = (EditText)rowView.findViewById(R.id.txtFin);
                EditText position = (EditText)rowView.findViewById(R.id.txtPoste);
                EditText societe = (EditText)rowView.findViewById(R.id.txtSociete);
                EditText about = (EditText)rowView.findViewById(R.id.txtAbout);
                TextView idExp = (TextView) rowView.findViewById(R.id.idExp);

                midExp = idExp.getText().toString();
                mbegin = debut.getText().toString();
                mend = fin.getText().toString();
                mposition = position.getText().toString();
                mcompany = societe.getText().toString();
                mabout = about.getText().toString();
                Log.i("debug",midExp+" "+mbegin+" "+mend+" "+mposition+" "+mcompany+" "+mabout);

                try {
                    Thread.sleep(100);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            for (int i = 0 ; i < linearLayoutP2.getChildCount() ; i++ ){
                View rowView = linearLayoutP2.getChildAt(i);

                EditText date = (EditText)rowView.findViewById(R.id.txtDate);
                EditText diplome = (EditText)rowView.findViewById(R.id.txtDiplome);
                EditText ecole = (EditText)rowView.findViewById(R.id.txtEcole);
                TextView idFormation = (TextView) rowView.findViewById(R.id.idFormation);

                midFormation = idFormation.getText().toString();
                mdate = date.getText().toString();
                mschool = ecole.getText().toString();
                mname = diplome.getText().toString();

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            updateCvServer();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        //finish();
        return true;
    }

    public void infoCvServer(){

        if (user_cv == null) {
            Toast.makeText(ModifcvActivity.this, "connection error", Toast.LENGTH_SHORT).show();
            return;
        }

        try{
            JSONObject jsonObject = new JSONObject(user_cv);

            if(jsonObject.getString("email") != "null") {
                email.setText(jsonObject.getString("email"));
            }
            if(!jsonObject.getString("tel").equals("0")) {
                tel.setText(jsonObject.getString("tel"));
            }
            if (jsonObject.getString("adress") !="null") {
                adress.setText(jsonObject.getString("adress"));
            }
            if(jsonObject.getString("firstName") != "null") {
                firstName.setText(jsonObject.getString("firstName"));
            }
            if(jsonObject.getString("lastName") != "null") {
                lastName.setText(jsonObject.getString("lastName"));
            }

            Map<String,Object> formationsMap = new HashMap<>();
            formationsMap = (Map) user_cv.get("formations");

            Map<String,Object> experiencesMap = new HashMap<>();
            experiencesMap = (Map) user_cv.get("experiences");

            JSONObject jsonObjectExperiences = new JSONObject(experiencesMap);

            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.row_modif_experience, null);

            EditText debut = (EditText)rowView.findViewById(R.id.txtDebut);
            EditText fin = (EditText)rowView.findViewById(R.id.txtFin);
            EditText position = (EditText)rowView.findViewById(R.id.txtPoste);
            EditText societe = (EditText)rowView.findViewById(R.id.txtSociete);
            EditText about = (EditText)rowView.findViewById(R.id.txtAbout);
            TextView idExp = (TextView) rowView.findViewById(R.id.idExp);

            //idExp.setText(String.valueOf(jsonObjectExperiences.getLong("id")));
            if(jsonObjectExperiences.getString("begin") != "null") {
                debut.setText(jsonObjectExperiences.getString("begin"));
            }
            if(jsonObjectExperiences.getString("end") != "null") {
                fin.setText(jsonObjectExperiences.getString("end"));
            }
            if(jsonObjectExperiences.getString("position") != "null") {
                position.setText(jsonObjectExperiences.getString("position"));
            }
            if(jsonObjectExperiences.getString("company") != "null") {
                societe.setText(jsonObjectExperiences.getString("company"));
            }
            if (jsonObjectExperiences.getString("about") != "null") {
                about.setText(jsonObjectExperiences.getString("about"));
            }

            linearLayoutP.addView(rowView, linearLayoutP.getChildCount() );

            JSONObject jsonObjectFormations = new JSONObject(formationsMap);

            LayoutInflater inflaterModif = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View ModifRowView = inflaterModif.inflate(R.layout.row_modif_formation, null);

            EditText date = (EditText)ModifRowView.findViewById(R.id.txtDate);
            EditText diplome = (EditText)ModifRowView.findViewById(R.id.txtDiplome);
            EditText ecole = (EditText)ModifRowView.findViewById(R.id.txtEcole);
            TextView idFormation = (TextView) ModifRowView.findViewById(R.id.idFormation);

            if (jsonObjectFormations.getString("date") != "null") {
                date.setText(jsonObjectFormations.getString("date"));
            }
            if(jsonObjectFormations.getString("name") != "null") {
                diplome.setText(jsonObjectFormations.getString("name"));
            }
            if(jsonObjectFormations.getString("school") != "null") {
                ecole.setText(jsonObjectFormations.getString("school"));
            }

            linearLayoutP2.addView(ModifRowView, linearLayoutP2.getChildCount() );

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void updateCvServer() {
        CollectionReference usersCollection = db.collection("users");//get the usercollection from firebae
        Map<String, Object> resume = new HashMap<>();

        resume.put("firstName", mFirstName);
        resume.put("lastName", mLastName);
        resume.put("adress", mAdress);
//        resume.put("age", mAge);
        resume.put("email", mEmail);
        resume.put("tel", mTel);

        resumeExeperience = new HashMap<>();
        resumeExeperience.put("begin",mbegin);
        resumeExeperience.put("end",mend);
        resumeExeperience.put("position",mposition);
        resumeExeperience.put("company",mcompany);
        resumeExeperience.put("about",mabout);

        resumeFormation = new HashMap<>();
        resumeFormation.put("date",mdate);
        resumeFormation.put("school",mschool);
        resumeFormation.put("name",mname);

        resume.put("experiences",resumeExeperience);
        resume.put("formations",resumeFormation);

        user.put("resume",resume);

        usersCollection.document(user.get("username").toString()).set(user);
        //pass in parameter to moncvActivity
        Intent intent = new Intent(ModifcvActivity.this,MoncvActivity.class);
        intent.putExtra("user",(Serializable) user);
        startActivity(intent);
    }

    protected class DeleteExp extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... params) {
            try {
                HttpClient client = new DefaultHttpClient();
                HttpDelete delete = new HttpDelete(params[0]);
                ResponseHandler<String> buffer = new BasicResponseHandler();
                String result = client.execute(delete, buffer);
                return result;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if (s == null) {
                Toast.makeText(ModifcvActivity.this, "erreur de connexion", Toast.LENGTH_SHORT).show();
                return;
            }
            Log.i("debug","in delete exp");
        }
    }

    protected class DeleteFormation extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... params) {
            try {
                HttpClient client = new DefaultHttpClient();
                HttpDelete delete = new HttpDelete(params[0]);
                ResponseHandler<String> buffer = new BasicResponseHandler();
                String result = client.execute(delete, buffer);
                return result;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if (s == null) {
                Toast.makeText(ModifcvActivity.this, "erreur de connexion", Toast.LENGTH_SHORT).show();
                return;
            }
            Log.i("debug","in delete formation");
        }
    }

}
