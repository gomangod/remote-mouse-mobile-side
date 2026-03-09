package com.example.mouseappv4;
import android.util.Log;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HostSelectionInterceptor implements Interceptor {

    private volatile String host;
    private volatile int port = 8080;

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();

        if (host != null) {
            try {
                HttpUrl newUrl = request.url().newBuilder()
                        .host(host)
                        .port(port)
                        .build();
                request = request.newBuilder()
                        .url(newUrl)
                        .build();
            }catch (IllegalArgumentException e){
                Log.d("lol", "intercept: error");
            }




        }

        return chain.proceed(request);
    }
}