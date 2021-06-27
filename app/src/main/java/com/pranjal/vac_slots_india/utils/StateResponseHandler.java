package com.pranjal.vac_slots_india.utils;

import android.content.Context;
import com.pranjal.vac_slots_india.models.States;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class StateResponseHandler {
    static ArrayList<States> statesList;

    public static ArrayList<States> handleJsonResponse(String response) {
        statesList = new ArrayList<>();
        try {
            JSONObject jsonObject=new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("states");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String state_name = jsonObject1.getString("state_name");
                Integer state_id = Integer.parseInt(jsonObject1.getString("state_id"));
                States state = new States(state_id, state_name);
                statesList.add(state);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return statesList;
    }
}
