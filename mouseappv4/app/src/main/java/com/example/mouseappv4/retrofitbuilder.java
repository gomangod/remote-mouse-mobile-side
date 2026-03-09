package com.example.mouseappv4;

import android.annotation.SuppressLint;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public class retrofitbuilder {

    interface sendspeed{
        @POST("sendspeed")
        Call<ResponseBody> send(@Body XYspeed xYspeed);
    }

    interface Leftclick{
        @GET("action/leftclick")
        Call<String> leftClicked();
    }

    interface Rightclick{
        @GET("action/rightclick")
        Call<String> rightClicked();
    }

    interface middleclick{
        @GET("action/middleclick")
        Call<String> middleClicked();
    }

    interface Connection{
        @POST("Connect")
        Call<String> connection(@Body String Dvicename);
    }

    interface Keybord{
        @POST("/action/key")
        Call<Void> keybord(@Body String key);
    }

    interface Enter{
        @GET("/action/key/enter")
        Call<Void> enter();
    }
    interface Backspace{
        @GET("/action/key/backspace")
        Call<Void> backspace();
    }
    interface Scroll{
        @POST("/action/Scroll")
        Call<Void> scroll(@Body ScrollState scrollState);
    }
    interface CMD{
        @POST("/cmd")
        Call<ResponseBody> cmd(@Body String command);
    }
    public static String ip = IP.getIP();


    public static HostSelectionInterceptor hostInterceptor = new HostSelectionInterceptor();
    public static OkHttpClient okHttpClient = new OkHttpClient().newBuilder().addInterceptor(hostInterceptor).build();
    static String baseUrl = "http://"+ip+":8080/";

    static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static Retrofit builder(){
        return retrofit;
    }
}
