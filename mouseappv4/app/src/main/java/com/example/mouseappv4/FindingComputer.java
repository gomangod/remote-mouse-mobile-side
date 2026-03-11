package com.example.mouseappv4;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FindingComputer extends AppCompatActivity {

    Button button;
    TextView text;
    String pcip;
    String MyIP;
    public String subnet;
    LinearLayout buttonlayout;
    Button ipbutton;
    static List<String> clentipadd = new ArrayList<String>();
    List<String> buttonName = new ArrayList<String>();

    Handler discoveryHandler = new Handler(Looper.getMainLooper());
    Runnable discoveryRunnable = () -> {
        if(buttonName.size() == 0){
            text.setText("no device found");
        } else {
            text.setText("devices found");
            create(buttonName.size());
            buttonName.clear();
        }
    };



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        button = findViewById(R.id.button);
        text = findViewById(R.id.text);
        buttonlayout = findViewById(R.id.buttonlayout);
        text.setTextColor(Color.rgb(255,204,0));
        button.setBackgroundColor(Color.rgb(255,204,0));
        button.setTextColor(Color.rgb(0,0,0));
        text.setText("press get device button");
        String MyIP = getLocalIpAddress();
        if(MyIP == null) {
            text.setText("No network connection");
            return; // stop here safely
        }
        else {
            text.setText("");
        }
        try {
            subnet = getBroadcastAddress(MyIP);
        } catch (UnknownHostException e) {
            Log.e("finding", "instance initializer: ",e );
        }


        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                new Thread(() -> {
                    try {
                        clinet();
                    } catch (IOException e) {
                        Log.e("fin", "onClick: ",e );
                    }
                }).start();
                new Thread(() -> {
                    try {
                        server();
                    } catch (IOException e) {
                        Log.e("fin", "onClick: ",e );
                    }
                }).start();
                discoveryHandler.postDelayed(discoveryRunnable, 1000);
            }
        });


    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent); // important!
    }
    public void clinet() throws IOException {
        DatagramSocket socket = new DatagramSocket();
        byte[] buffer = "9122".getBytes();
        InetAddress address = InetAddress.getByName(subnet);
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 9123);
        socket.send(packet);
        socket.close();

    }

    public void server() throws IOException {
        ServerSocket ss = new ServerSocket(9122);
        ss.setSoTimeout(1000);
        try {
            while (true) {
                Socket s = ss.accept();
                BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                pcip = br.readLine();
                Log.d("ipof", "server: " + pcip);
                clentipadd.add(pcip);
                buttonName.add(pcip);
                s.close();

            }
        } catch ( SocketTimeoutException e) {
            Log.d("fin", "server: "+e );
        }
        ss.close();
        }


    public String getLocalIpAddress() {
        try {
            for (NetworkInterface intf : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                for (InetAddress addr : Collections.list(intf.getInetAddresses())) {
                    if (!addr.isLoopbackAddress() && addr instanceof Inet4Address) {
                        return addr.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            Log.e("fin", "getLocalIpAddress: ", e);
        }
        return null;
    }

    public void create(int noclient){

        for(int i = 0; i < noclient; i++){
            buttonlayout.removeViewInLayout(findViewById(i));
        }
        for (int i = 0; i < noclient; i++) {
            ipbutton = new Button(this);
            ipbutton.setBackgroundColor(Color.rgb(255,204,0));
            ipbutton.setTextColor(Color.rgb(0,0,0));
            ipbutton.setId(i);
            ipbutton.setText(buttonName.get(i));
            buttonlayout.addView(ipbutton);
            ipbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    discoveryHandler.removeCallbacks(discoveryRunnable);
                    Log.d("like", "onClick: " + v.getId());
                    Connection.setIp(clentipadd.get(v.getId()));
                    Intent i = new Intent(FindingComputer.this, Connection.class);
                    startActivity(i);
                }
            });
        }


    }


    public String getBroadcastAddress( final String address ) throws UnknownHostException {
        InetAddress addr = InetAddress.getByName( address );
        if( addr instanceof Inet4Address ) {
            byte[] bytes = addr.getAddress();
            bytes[3] = (byte) 255;

            return InetAddress.getByAddress( bytes ).getHostAddress();
        }
        return "x.x.x.";
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        discoveryHandler.removeCallbacks(discoveryRunnable);
    }

}