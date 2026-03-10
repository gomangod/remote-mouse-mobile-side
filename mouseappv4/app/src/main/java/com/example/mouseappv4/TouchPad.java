package com.example.mouseappv4;



import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.google.android.material.slider.Slider;

import java.io.IOException;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class TouchPad extends AppCompatActivity {

    boolean MclickOn;
    boolean ISkeybordON = false;
    boolean backspoace;
    int NumberofClicks = 0;
    int Speedx = 10;
    int Speedy = 10;
    int settingbuttonclickedtimes;
    static int speedadder = 1;
    View view;
    LinearLayout body;
    TextView textView;
    EditText editText;
    Slider slider;
    int Xdown = 0 , Ydown = 0;

    Button Leftbutton;
    Button Rightbutton;
    Button Middlebutton;
    Button Keybord;
    ImageButton setting;
    public static String ip = IP.getIP();
    private static final int MOVE_INTERVAL = 16;
    private long lastMoveTime = 0;
    XYspeed xYspeed = new XYspeed(0,0);
    ScrollState scrollState = new ScrollState();
    static HostSelectionInterceptor hostInterceptor = retrofitbuilder.hostInterceptor;
    private final Retrofit retrofit = retrofitbuilder.builder();
    retrofitbuilder.sendspeed sendspeed = retrofit.create(retrofitbuilder.sendspeed.class);
    retrofitbuilder.Leftclick leftclick = retrofit.create(retrofitbuilder.Leftclick.class);
    retrofitbuilder.Rightclick rightclick = retrofit.create(retrofitbuilder.Rightclick.class);
    retrofitbuilder.middleclick middleclick = retrofit.create(retrofitbuilder.middleclick.class);
    retrofitbuilder.Keybord keybord = retrofit.create(retrofitbuilder.Keybord.class);
    retrofitbuilder.Enter enter = retrofit.create(retrofitbuilder.Enter.class);
    retrofitbuilder.Backspace backspace = retrofit.create(retrofitbuilder.Backspace.class);
    retrofitbuilder.Scroll scroll = retrofit.create(retrofitbuilder.Scroll.class);

    Handler handler = new Handler();
    Runnable clickAction = new Runnable() {
        @Override
        public void run() {
            if(NumberofClicks == 1){
                LClicker();
            }
            if(NumberofClicks == 2){
                LClicker();
                LClicker();
            }
            NumberofClicks = 0;
        }
    };


    @SuppressLint({"MissingInflatedId", "ClickableViewAccessibility"})
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.touchpad);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.touchpad), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        body = findViewById(R.id.body);
        view = findViewById(R.id.circle);
        textView = findViewById(R.id.text);
        Leftbutton = findViewById(R.id.button4);
        Rightbutton = findViewById(R.id.button5);
        Middlebutton = findViewById(R.id.button6);
        Keybord = findViewById(R.id.Keybord);
        editText = findViewById(R.id.edit_text);
        slider = findViewById(R.id.slider);
        setting = findViewById(R.id.settingsButton);
        hostInterceptor.setHost(ip);
        editText.setVisibility(View.INVISIBLE);
        slider.setVisibility(View.INVISIBLE);
        textView.setTextColor(Color.rgb(255,204,0));
        Leftbutton.setBackgroundColor(Color.rgb(255,204,0));
        Leftbutton.setTextColor(Color.rgb(0,0,0));
        Rightbutton.setBackgroundColor(Color.rgb(255,204,0));
        Rightbutton.setTextColor(Color.rgb(0,0,0));
        Middlebutton.setBackgroundColor(Color.rgb(255,204,0));
        Middlebutton.setTextColor(Color.rgb(0,0,0));
        Keybord.setBackgroundColor(Color.rgb(255,204,0));
        Keybord.setTextColor(Color.rgb(0,0,0));
        slider.setBackgroundColor(Color.rgb(0,0,0));
        movingmouse();
        Keybord();
        Keyboradkeylistner();
        settings();
        Leftbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LClicker();

            }
        });
        Rightbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Rclick();
            }
        });
        Middlebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mclick();
            }
        });

        Log.d("lol", "onCreate: "+ip);

    }
    @Override
    protected void onNewIntent(@NonNull Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent); // important!
    }
    @SuppressLint("ClickableViewAccessibility")
    public void movingmouse(){
        body.setOnTouchListener(new View.OnTouchListener()
        {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        Xdown = (int) event.getX();
                        Ydown = (int) event.getY();
                        NumberofClicks++;
                        handler.removeCallbacks(clickAction);
                        handler.postDelayed(clickAction,300);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int movex = (int) event.getX();
                        int movey = (int) event.getY();
                        Speedx = Xdown - movex;
                        Speedy = Ydown - movey;

                        Xdown = movex;
                        Ydown = movey;
                        long now = System.currentTimeMillis();
                        if(now - lastMoveTime < MOVE_INTERVAL) return true;
                        lastMoveTime = now;
                        if(event.getPointerCount() == 1){
                            xYspeed.speedx = Speedx * speedadder;
                            xYspeed.speedy = Speedy * speedadder;

                            sendingData(xYspeed);
                        } else if (event.getPointerCount() == 2) {
                            if(Speedy > 2 || Speedy < -2){
                                scrollState.dis = Speedy/3;
                                scrollState.Side = false;
                                Scrolldatasending(scrollState);
                            } else if (Speedx > 2 || Speedx < -2) {
                                scrollState.dis = Speedx/3;
                                scrollState.Side = true;
                                Scrolldatasending(scrollState);
                            }
                        }
                        view.setX(movex - 55);
                        view.setY(movey - 55);
                        break;
                }
                return true;
            }

        });



    }

    public void Keybord(){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        Keybord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ISkeybordON){
                    editText.setVisibility(View.VISIBLE);
                    editText.requestFocus();
                    imm.showSoftInput(editText,InputMethodManager.SHOW_IMPLICIT);
                    ISkeybordON = true;

                }
                else{
                    imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
                    ISkeybordON = false;
                    editText.setVisibility(View.INVISIBLE);
                }
            }
        });


    }

    public void Keyboradkeylistner(){
        keyboradlistner();
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    backspoace = true;
                } else {
                    backspoace = false;
                }


                return false;
            }
        });
    }
    public boolean keyboradlistner(){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(backspoace){
                    backspace();
                }
                else {
                    CharSequence lastchar = s.subSequence(s.length()-1,s.length());
                    if(lastchar.toString().contains("\n"))
                    {
                        enter();
                    }
                    else if (count > before)
                    {

                        String addedText = s.subSequence(start, start + count).toString();
                        sendKey(addedText);

                    }
                }

                if(before > 0 && count > 0){

                    String replaced = s.subSequence(start, start + count).toString();
                    sendKey(replaced);
                }

            }
        });
        return false;
    }

    public void sendKey(String  key){
        keybord.keybord(key).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("done", "onResponse: done");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("done", "onFailure: notdone" + t.getMessage());
            }
        });
    }
    public void enter(){
        enter.enter().enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }
    public void backspace(){
        backspace.backspace().enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }
    public void sendingData(XYspeed xyspeed)
    {
        sendspeed.send(xyspeed).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse( Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure( Call<ResponseBody> call, Throwable t) {
                textView.setText("not in sync");
            }
        });
    }

    public void Scrolldatasending(ScrollState scrollState){
        scroll.scroll(scrollState).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("lol", "onResponse: done");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("lol", "onFailure: " + t.getMessage());
            }
        });
    }
    public void LClicker(){
        leftclick.leftClicked().enqueue(new Callback<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                textView.setText("left");
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    public void Rclick(){
        rightclick.rightClicked().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                textView.setText("rigth");
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    public void Mclick(){
        middleclick.middleClicked().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                textView.setText("middle");
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    public void settings(){

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingbuttonclickedtimes++;
                if(settingbuttonclickedtimes == 1){
                    slider.setVisibility(View.VISIBLE);
                } else if (settingbuttonclickedtimes == 2) {
                    slider.setVisibility(View.INVISIBLE);
                    settingbuttonclickedtimes = 0;
                }
            }
        });

        slider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float v, boolean b) {
                speedadder = (int) v;
            }
        });
    }

}