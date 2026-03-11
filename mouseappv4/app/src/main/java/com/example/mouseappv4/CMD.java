package com.example.mouseappv4;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class CMD extends AppCompatActivity {

    final List<String> BLOCKED = Arrays.asList(
            "format", "diskpart", "del /f", "del /s",
            "rd /s", "rmdir /s", "reg delete", "reg add",
            "regedit", "net user", "net localgroup",
            "bcdedit", "bootrec", "shutdown", "wmic os",
            "powershell -enc", "powershell -e ",
            "certutil -decode", "bitsadmin",
            "wscript", "cscript", "runas", "psexec",
            "netsh firewall", "netsh advfirewall"
    );

    boolean ISkeybordON;
    public String ip = IP.getIP();
    static HostSelectionInterceptor hostInterceptor = retrofitbuilder.hostInterceptor;
    private final Retrofit retrofit = retrofitbuilder.builder();
    retrofitbuilder.CMD cmd = retrofit.create(retrofitbuilder.CMD.class);
    LinearLayout layout;
    ScrollView scrollView;
    @SuppressLint("MissingInflatedId")
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.cmd);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cmd), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        hostInterceptor.setHost(ip);
        layout = findViewById(R.id.textlayout);

    createEditable();

    }

    public void createEditable(){
        EditText editText = new EditText(this);
        TextView textView = new TextView(this);
        textView.setText("write_cmd->");
        editText.requestFocus();
        editText.setTextColor(Color.rgb(255,204,0));
        textView.setTextColor(Color.rgb(0,255,0));
        editText.setBackground(null);
        editText.setTextSize(10);
        layout.addView(textView);
        layout.addView(editText);
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText,InputMethodManager.SHOW_IMPLICIT);
        ISkeybordON = true;

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count > before) {
                    CharSequence lastchar = s.subSequence(s.length() - 1, s.length());
                    if (lastchar.toString().contains("\n")) {
                        String command = editText.getText().toString().substring(0,editText.getText().length()-1);
                        Log.d("cmd", "onTextChanged: " + command);
                            sendingcmd(command);
                    }
                }
            }
        });
    }

    public void Output(String output){
        TextView Outputlable = new TextView(this);
        Outputlable.setTextColor(Color.rgb(0,255,0));
        Outputlable.setText("Output->");
        TextView OutputContent = new TextView(this);
        OutputContent.setTextColor(Color.rgb(255,204,0));
        OutputContent.setText(output);
        OutputContent.setTextSize(10);
        layout.addView(Outputlable);
        layout.addView(OutputContent);
        createEditable();
    }

    public void sendingcmd(String command){
        cmd.cmd(command).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody body = response.body();
                try {
                    if(body != null){
                        Output(body.string());
                    } else {
                        Output("empty response from server");
                    }
                } catch (IOException e) {
                    Output("error reading response: " + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Output(t.getMessage().toString());
            }

        });
    }
}
