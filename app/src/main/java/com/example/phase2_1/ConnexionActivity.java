package com.example.phase2_1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApiNotAvailableException;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Query.Direction;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Source;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import android.widget.Toast;


public class ConnexionActivity extends AppCompatActivity {
    Button signUp,login;
    EditText txtUsername,txtPassword;
    String username,password;
    ProgressDialog dialog;
//    private TextView tv_date;
    private TextView tv_content;
    private DatabaseReference mReference;
    private  FirebaseFirestore db;
    private static final String TAG = "DocSnippets";
    int tryNum = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);
        // [START get_firestore_instance]
         db = FirebaseFirestore.getInstance();

        txtUsername = (EditText)findViewById(R.id.txtUsername);
        txtPassword = (EditText)findViewById(R.id.txtPassword);
        dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.waiting));

        login = (Button)findViewById(R.id.btnLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = txtUsername.getText().toString();
                password = txtPassword.getText().toString();
                //at most try to three times
                if(tryNum  == 0){
                    Toast.makeText(ConnexionActivity.this, "Try too many times, your account has been blocked, please contact customer service!", Toast.LENGTH_LONG).show();
                    return;
                };
                if (username.length() == 0) {
                    tryNum--;
                    Toast.makeText(ConnexionActivity.this, "Wrong Credentials, Please enter a valid User name," +
                    tryNum + " attempts are left ",
                    Toast.LENGTH_LONG).show();
                    return;
                }
                if (password.length() == 0) {
                    tryNum--;
                    Toast.makeText(ConnexionActivity.this, "Wrong Credentials, Please enter a valid Password," +
                    tryNum + " attempts are left ",
                    Toast.LENGTH_LONG).show();
                }
                else{
                    DocumentReference docRef = db.collection("users").document(username);
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                    //judg the password wether right
                                    Log.d(TAG, "DocumentSnapshot data password: " + document.getData().get("password"));
                                    Log.d(TAG, "password: " + password);

                                    if(password.equals(document.getData().get("password"))){
                                        Log.d(TAG, "password: right" );
                                        Toast.makeText(ConnexionActivity.this, "Login successful!", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent();
                                        intent.setClass(ConnexionActivity.this,HomeActivity.class);
                                        intent.putExtra("user", (Serializable) document.getData());
                                        startActivity(intent);
                                    }
                                    else {
                                        Toast.makeText(ConnexionActivity.this, "Wrong password, Please try again," +
                                        tryNum + " attempts are left ",
                                        Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    //have no this user name in the database
                                    Log.d(TAG, "No such document");
                                    Toast.makeText(ConnexionActivity.this, "Wrong user name, Please try again," +
                                    tryNum + " attempts are left ",
                                    Toast.LENGTH_LONG).show();
                                    return;
                                }
                            } else {
                                Log.d(TAG, "get failed with ", task.getException());
                            }
                        }
                    });
                }
            }
        });

        signUp = (Button)findViewById(R.id.btnSignUp);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConnexionActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    public void collectionReference() {
        // [START collection_reference]
        CollectionReference usersCollectionRef = db.collection("users");
        // [END collection_reference]
    }

    public void docReference() {
        // [START doc_reference]
        DocumentReference alovelaceDocumentRef = db.collection("users").document("alovelace");
        // [END doc_reference]
    }

    private void iniUI() {
        login =  findViewById(R.id.btnLogin);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
