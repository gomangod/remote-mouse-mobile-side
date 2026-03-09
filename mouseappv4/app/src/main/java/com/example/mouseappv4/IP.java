package com.example.mouseappv4;

import android.util.Log;

public class IP {
    public static String IP;

    public static String getIP() {

        return IP;
    }

    public static void setIP(String IP) {
        com.example.mouseappv4.IP.IP = IP;
        Log.e("lol",IP);
    }
}
