package com.example.swipingtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class tutorial extends AppCompatActivity {

    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        back = findViewById(R.id.back);
        back.setOnClickListener(v -> {
            Intent i = new Intent(this, com.example.swipingtest.MainActivity.class);
            startActivity(i);
        });
    }
}