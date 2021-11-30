package com.example.phase2_1;

import android.app.ProgressDialog;
import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

public class ModifcvActivity extends AppCompatActivity {
    LinearLayout linearLayoutP ;
    LinearLayout linearLayoutP2 ;
    String user_cv,mFirstName,mLastName,mAdress,mTel,mAge,mEmail;
    EditText lastName,firstName,email,adress,tel,age;
    ProgressDialog dialog;
    List<String> expAsupp,formationAsupp;
    String midExp,mbegin,mend,mposition,mcompany,mabout;
    String midFormation,mdate,mname,mschool;
    int a = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifcv);
        Toolbar toolbar = (Toolbar)findViewById(R.id.modifcvToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Modifier mon CV");
        toolbar.setTitleTextColor(Color.parseColor("#ecf0f1"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.waiting));

        user_cv = getIntent().getStringExtra("user_cv");

        expAsupp = new ArrayList<>();
        formationAsupp = new ArrayList<>();

        linearLayoutP = (LinearLayout) findViewById(R.id.linearLayoutModifcv);
        linearLayoutP2 = (LinearLayout) findViewById(R.id.linearLayoutModifcv2);

        firstName =(EditText)findViewById(R.id.txtPrenom);
        lastName =(EditText)findViewById(R.id.txtNom);
        email =(EditText)findViewById(R.id.txtEmail);
        adress =(EditText)findViewById(R.id.txtAdresse);
        tel =(EditText)findViewById(R.id.txtTel);
        age =(EditText)findViewById(R.id.txtAge);

        InfoCvServer infoCvServer = new InfoCvServer();
        infoCvServer.execute("http://192.168.56.1:8080/api/cv/"+user_cv);


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
        Log.i("debug","1111");
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
             mAge = age.getText().toString();
             mTel = tel.getText().toString();
             mEmail = email.getText().toString();

            UpdateCvServer updateCvServer = new UpdateCvServer();
            updateCvServer.execute("http://192.168.56.1:8080/api/cv/"+user_cv);

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
                    UpdateOrAddExp updateOrAddExp = new UpdateOrAddExp();
                    updateOrAddExp.execute("http://192.168.56.1:8080/api/experiences");
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
                    UpdateOrAddFormation updateOrAddFormation = new UpdateOrAddFormation();
                    updateOrAddFormation.execute("http://192.168.56.1:8080/api/formations");
                    Thread.sleep(100);


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            for (int i = 0 ; i < expAsupp.size() ; i++ ){
                DeleteExp deleteExp = new DeleteExp();
                deleteExp.execute("http://192.168.56.1:8080/api/experiences/"+expAsupp.get(i));
            }
            for (int i = 0 ; i < formationAsupp.size() ; i++ ){
                DeleteFormation deleteFormation = new DeleteFormation();
                deleteFormation.execute("http://192.168.56.1:8080/api/formations/"+formationAsupp.get(i));
            }
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    protected class InfoCvServer extends AsyncTask<String,Void,String>{
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
            if (s == null) {
                Toast.makeText(ModifcvActivity.this, "erreur de connexion", Toast.LENGTH_SHORT).show();
                return;
            }

            try{
                JSONObject jsonObject = new JSONObject(s);
                if(jsonObject.getString("email") != "null") {
                    email.setText(jsonObject.getString("email"));
                }
                if (jsonObject.getInt("age") != 0) {
                    age.setText(String.valueOf(jsonObject.getInt("age")));
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

                JSONArray jsonFormation = jsonObject.getJSONArray("formations");
                JSONArray jsonExperience = jsonObject.getJSONArray("experiences");

                for (int i = 0 ; i < jsonExperience.length() ; i++){
                    JSONObject element = jsonExperience.getJSONObject(i);
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                     View rowView = inflater.inflate(R.layout.row_modif_experience, null);

                    EditText debut = (EditText)rowView.findViewById(R.id.txtDebut);
                    EditText fin = (EditText)rowView.findViewById(R.id.txtFin);
                    EditText position = (EditText)rowView.findViewById(R.id.txtPoste);
                    EditText societe = (EditText)rowView.findViewById(R.id.txtSociete);
                    EditText about = (EditText)rowView.findViewById(R.id.txtAbout);
                    TextView idExp = (TextView) rowView.findViewById(R.id.idExp);
                    idExp.setText(String.valueOf(element.getLong("id")));
                    if(element.getString("begin") != "null") {
                        debut.setText(element.getString("begin"));
                    }
                    if(element.getString("end") != "null") {
                        fin.setText(element.getString("end"));
                    }
                    if(element.getString("position") != "null") {
                        position.setText(element.getString("position"));
                    }
                    if(element.getString("company") != "null") {
                        societe.setText(element.getString("company"));
                    }
                    if (element.getString("about") != "null") {
                        about.setText(element.getString("about"));
                    }

                    linearLayoutP.addView(rowView, linearLayoutP.getChildCount() );

                }

                for (int i = 0 ; i < jsonFormation.length() ; i++){
                    JSONObject element = jsonFormation.getJSONObject(i);
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View rowView = inflater.inflate(R.layout.row_modif_formation, null);

                    EditText date = (EditText)rowView.findViewById(R.id.txtDate);
                    EditText diplome = (EditText)rowView.findViewById(R.id.txtDiplome);
                    EditText ecole = (EditText)rowView.findViewById(R.id.txtEcole);
                    TextView idFormation = (TextView) rowView.findViewById(R.id.idFormation);

                    idFormation.setText(String.valueOf(element.getLong("id")));
                    if (element.getString("date") != "null") {
                        date.setText(element.getString("date"));
                    }
                    if(element.getString("name") != "null") {
                        diplome.setText(element.getString("name"));
                    }
                    if(element.getString("school") != "null") {
                        ecole.setText(element.getString("school"));
                    }

                    linearLayoutP2.addView(rowView, linearLayoutP2.getChildCount() );
                }

            }catch(Exception e){
                e.printStackTrace();
            }

        }
    }

    protected class UpdateCvServer extends AsyncTask<String,Void,String>{
        @Override
        protected void onPreExecute() {
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try{


                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(params[0]);
                List<NameValuePair> form = new ArrayList<>();
                form.add(new BasicNameValuePair("firstName",mFirstName));
                form.add(new BasicNameValuePair("lastName",mLastName));
                form.add(new BasicNameValuePair("adress",mAdress));
                form.add(new BasicNameValuePair("age",mAge));
                form.add(new BasicNameValuePair("email",mEmail));
                form.add(new BasicNameValuePair("tel",mTel));
                post.setEntity(new UrlEncodedFormEntity(form, HTTP.UTF_8));
                ResponseHandler<String> buffer = new BasicResponseHandler();
                String result = client.execute(post,buffer);

                return result;
            }catch(Exception e){
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

            try{
                JSONObject jsonObject = new JSONObject(s);
                if(jsonObject.getString("status").equals("ok")){
                    Toast.makeText(ModifcvActivity.this, "Cv modifié", Toast.LENGTH_SHORT).show();
                }

            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    protected class UpdateOrAddExp extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... params) {
            try{
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(params[0]);
                List<NameValuePair> form = new ArrayList<>();
                form.add(new BasicNameValuePair("idCV",user_cv));
                form.add(new BasicNameValuePair("id",midExp));
                form.add(new BasicNameValuePair("begin",mbegin));
                form.add(new BasicNameValuePair("end",mend));
                form.add(new BasicNameValuePair("position",mposition));
                form.add(new BasicNameValuePair("company",mcompany));
                form.add(new BasicNameValuePair("about",mabout));
                post.setEntity(new UrlEncodedFormEntity(form,HTTP.UTF_8));
                ResponseHandler<String> buffer = new BasicResponseHandler();
                String result = client.execute(post,buffer);
                a++;
                Log.i("debug","a ==> "+a);
                return result;

            }catch(Exception e){
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
            Log.i("debug","in modif or add exp");


        }
    }

    protected class UpdateOrAddFormation extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... params) {
            try{
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(params[0]);
                List<NameValuePair> form = new ArrayList<>();
                form.add(new BasicNameValuePair("idCV",user_cv));
                form.add(new BasicNameValuePair("id",midFormation));
                form.add(new BasicNameValuePair("date",mdate));
                form.add(new BasicNameValuePair("name",mname));
                form.add(new BasicNameValuePair("school",mschool));
                post.setEntity(new UrlEncodedFormEntity(form,HTTP.UTF_8));
                ResponseHandler<String> buffer = new BasicResponseHandler();
                String result = client.execute(post,buffer);
                return result;

            }catch(Exception e){
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
            Log.i("debug","in modif or add formation");
        }
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
