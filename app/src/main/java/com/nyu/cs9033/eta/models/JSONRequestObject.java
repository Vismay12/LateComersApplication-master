package com.nyu.cs9033.eta.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Vismay on 5/5/2015.
 */
public class JSONRequestObject {
    private String uri;
    private String method;
    private JSONObject jsonObject;

    public JSONRequestObject(){
        jsonObject = new JSONObject();
        method = "POST";
    }

    public JSONObject getjsonObject() {
        return jsonObject;
    }
    public void setUri(String uri) {
        this.uri = uri;
    }
    public String getUri() {
        return uri;
    }
    public void setMethod(String method) {
        this.method = method;
    }
    public String getMethod() {
        return method;
    }

    public void putToJSON(String name, String value){
        try {
            jsonObject.put(name, value);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public void putToJSON(String key, JSONArray value){
        try {
            jsonObject.put(key, value);
        } catch (JSONException e) {
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
    public void putToJSON(String name, double value){
        try {
            jsonObject.put(name, value);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
