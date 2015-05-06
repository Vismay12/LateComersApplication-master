package com.nyu.cs9033.eta.controllers;

import com.nyu.cs9033.eta.models.Person;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Vismay on 5/5/2015.
 */
public class ParseJSON {
    public static long parseId(String content) {
        try {
            JSONObject obj = new JSONObject(content);
            if (obj.get("response_code") != null)
                return obj.getLong("trip_id");
            else
                return 0;
        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static ArrayList<Person> parseInfo(String content) {
        try {
            ArrayList<Person> people = new ArrayList<Person>();
            JSONObject obj = new JSONObject(content);
            JSONArray distLeft = obj.getJSONArray("distance_left");
            JSONArray timeLeft = obj.getJSONArray("time_left");
            JSONArray friends = obj.getJSONArray("people");
            for (int i = 0; i < friends.length(); i++) {
                people.add(new Person(friends.getString(i),distLeft.getDouble(i), timeLeft.getLong(i)));
            }
            return people;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
