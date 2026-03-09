package com.example.mouseappv4;

import static com.example.mouseappv4.retrofitbuilder.hostInterceptor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Objects;

public class Home_Activity extends AppCompatActivity {
    Button Findbutton;
    Button Touchpadbbutton;
    public Intent i = new Intent(Home_Activity.this,TouchPad.class);
    LinearLayout layout;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        Findbutton = findViewById(R.id.button2);
        Touchpadbbutton = findViewById(R.id.button3);
        Findbutton.setText("find a computer");
        Touchpadbbutton.setText("Touchpad");
        Findbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Home_Activity.this, FindingComputer.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }
        });

        Touchpadbbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Home_Activity.this,TouchPad.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }
        });


    }

    }

