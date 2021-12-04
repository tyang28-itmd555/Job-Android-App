package com.example.phase2_1;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentChange.Type;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

public class RegisterActivity extends AppCompatActivity {
    private Button btnConnectR ,btnSignUp;
    EditText txtUsername,txtPassword,txtEmail;
    String username,password,email;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        db = FirebaseFirestore.getInstance();

        btnConnectR = (Button)findViewById(R.id.btnConnectR);
        btnSignUp = (Button)findViewById(R.id.btnSignUpR);
        txtUsername = (EditText) findViewById(R.id.txtUsernameR);
        txtPassword = (EditText) findViewById(R.id.txtPasswordR);
        txtEmail = (EditText) findViewById(R.id.txtEmailR);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = txtUsername.getText().toString();
                password = txtPassword.getText().toString();
                email = txtEmail.getText().toString();
                addUser(username,password,email);
            }
        });

        btnConnectR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this,ConnexionActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    public void addUser(String username,String password,String email) {
        //any input can not be empty
        if(username.length() == 0 || password.length() == 0 || email.length() == 0){
            Toast.makeText(RegisterActivity.this, "any input can not be empty, Please try again,",
            Toast.LENGTH_LONG).show();
            return;
        }
        //Create a new user with a username and password
        CollectionReference usersCollection = db.collection("users");
        Map<String, Object> users = new HashMap<>();
        users.put("username", username);
        users.put("password", password);
        users.put("email", email);
        users.put("favoriteJobs",new HashMap<>());
        users.put("resume",new HashMap<>());
        usersCollection.document(username).set(users);
        Toast.makeText(RegisterActivity.this, "register successful!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(RegisterActivity.this,DescActivity.class);
        intent.putExtra("user", (Serializable) users);
        startActivity(intent);
    }
}
