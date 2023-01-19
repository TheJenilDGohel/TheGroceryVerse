package com.example.mygrocerystore.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.example.mygrocerystore.R;

public class HomeActivity extends AppCompatActivity {
    private AppCompatButton NextToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Change StatusBar Color
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.dark_green));

        NextToLogin = findViewById(R.id.next);
        NextToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, LoginRegisterActivity.class));
                finish();
            }
        });

    }
}