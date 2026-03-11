package com.example.mouseappv4;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.ComponentActivity;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Connection extends AppCompatActivity {

String devicename = Build.DEVICE;
public static String ip;
    public static void setIp(String ip) {
        Connection.ip = ip;
    }

@SuppressLint("MissingInflatedId")
protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    setContentView(R.layout.connection);
    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.connection), (v, insets) -> {
        Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
        v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
        return insets;
    });

    new Thread(() ->{
        try {
            Socket s = new Socket(ip,9111);
            OutputStreamWriter opsw = new OutputStreamWriter(s.getOutputStream());
            PrintWriter pw = new PrintWriter(opsw);
            pw.println(devicename);
            opsw.close();
            s.close();
            listner();
        } catch (IOException e) {
            Log.e("con", "onCreate: ",e );
        }


    }).start();

}

public void listner() throws IOException {
    while (true) {
        ServerSocket ss = new ServerSocket(9112);
        DataInputStream dataInputStream = new DataInputStream(ss.accept().getInputStream());
        String message = dataInputStream.readLine();
        ss.close();
        if (Objects.equals(message, "Connect")) {
            IP.setIP(ip);
            Intent i = new Intent(Connection.this,Home_Activity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i);
        } else if (Objects.equals(message, "refused")) {
            IP.setIP("0.0.0.0");
            Intent i = new Intent(Connection.this, FindingComputer.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i);
        }
    }

}


}
