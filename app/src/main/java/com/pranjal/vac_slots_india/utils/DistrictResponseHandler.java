package com.pranjal.vac_slots_india.utils;

import android.content.Context;

import com.pranjal.vac_slots_india.models.Districts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DistrictResponseHandler {
static ArrayList<Districts> districtsList;
    public static ArrayList<Districts> handleJsonResponse(String response) {
        districtsList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("districts");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String dist_name = jsonObject1.getString("district_name");
                Integer dist_code = Integer.parseInt(jsonObject1.getString("district_id"));
                Districts district = new Districts(dist_code, dist_name);
                districtsList.add(district);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return districtsList;
    }
}
