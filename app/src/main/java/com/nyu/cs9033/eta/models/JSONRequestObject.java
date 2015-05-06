package com.nyu.cs9033.eta.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vismay on 5/5/2015.
 */
public class JSONRequestObject {
    private String uri;
    private String method;
    private Map<String,String> params = new HashMap<String,String>();
    private JSONObject jsonObject;

    public JSONRequestObject(){
        jsonObject = new JSONObject();
        method = "POST";
    }

    public JSONObject getjsonObject() {
        return jsonObject;
    }

    public String getUri() {
        return uri;
    }


    public Map<String, String> getParams() {
        return params;
    }


    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setMethod(String method) {
        this.method = method;
    }
    public String getMethod() {
        return method;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }
    public void setParam(String key, String value){
        this.params.put(key, value);
    }

    public void setjsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public void putToJSON(String key, JSONArray value){
        try {
            jsonObject.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void putToJSON(String name, String value){
        try {
            jsonObject.put(name, value);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void putToJSON(String name, int value){
        try {
            jsonObject.put(name, value);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void putToJSON(String name, double value){
        try {
            jsonObject.put(name, value);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void putToJSON(String name, boolean value){
        try {
            jsonObject.put(name, value);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void putToJSON(String name, long value){
        try {
            jsonObject.put(name, value);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}
