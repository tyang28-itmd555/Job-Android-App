package com.example.phase2_1;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DescActivity extends AppCompatActivity {
    String id;
    private Map user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desc);

        Button button = (Button)findViewById(R.id.btnDesc);
        user = (HashMap)getIntent().getSerializableExtra("user");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DescActivity.this,HomeActivity.class);
                intent.putExtra("user", (Serializable) user);
                startActivity(intent);
            }
        });
    }
}
