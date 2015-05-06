package com.nyu.cs9033.eta.controllers;

import android.util.Log;

import com.nyu.cs9033.eta.models.JSONRequestObject;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Vismay on 5/5/2015.
 */
public class ConnectInternet {
    public static final String SERVERURI = "http://cs9033-homework.appspot.com/";

    public static String getData(JSONRequestObject p){
        String uri = p.getUri();
        BufferedReader reader = null;
        try{
            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(p.getMethod());
            JSONObject json = p.getjsonObject();
            if(p.getMethod().equals("POST")){
                Log.d("get","post");
                con.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
                Log.d("JSON REQUEST", json.toString());
                writer.write(json.toString());
                writer.flush();
            }
            StringBuilder builder = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;
            while((line = reader.readLine()) != null){
                builder.append(line + "\n");
            }
            return builder.toString();
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }
        finally{
            try{
                reader.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
