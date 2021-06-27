package com.pranjal.vac_slots_india.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.pranjal.vac_slots_india.models.Centers;
import com.pranjal.vac_slots_india.models.Sessions;
import com.pranjal.vac_slots_india.models.VaccineFees;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SlotResponseHandler {
    public static ArrayList<Centers> getCentersList(String response, Context context) {
        ArrayList<Centers> centersList = new ArrayList<>();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        try {
            boolean notifiable_age, notifiable_dose, notifiable_vac;
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("centers");
            for (int i = 0; i < jsonArray.length(); i++) {
                Centers center = new Centers();
                VaccineFees vaccineFees = new VaccineFees();
                ArrayList<VaccineFees> vaccineFeesList = new ArrayList<>();
                ArrayList<Sessions> sessionsList = new ArrayList<>();
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                center.setCenterId(Integer.parseInt(jsonObject1.getString("center_id")));
                center.setCenterName(jsonObject1.getString("name"));
                center.setAddress(jsonObject1.getString("address"));
                center.setCenterDistrict(jsonObject1.getString("district_name"));
                center.setCenterState(jsonObject1.getString("state_name"));
                center.setPincode(jsonObject1.getInt("pincode"));
                center.setFee_type(jsonObject1.getString("fee_type"));
                JSONArray jsonArray1 = jsonObject1.getJSONArray("sessions");
                int j = 0;
                while (j < jsonArray1.length()) {
                    Sessions session = new Sessions();
                    JSONObject jsonObject2 = jsonArray1.getJSONObject(j);
                    session.setCenterId(center.getCenterId());
                    session.setSession_id(jsonObject2.getString("session_id"));
                    session.setDate(jsonObject2.getString("date"));
                    session.setAvailable_capacity(jsonObject2.getInt("available_capacity"));
                    session.setMin_age_limit(jsonObject2.getInt("min_age_limit"));
                    session.setVaccine(jsonObject2.getString("vaccine"));
                    session.setAvailable_capacity_dose1(jsonObject2.getInt("available_capacity_dose1"));
                    session.setAvailable_capacity_dose2(jsonObject2.getInt("available_capacity_dose2"));
                    session.setNotified(false);
                    if (session.getAvailable_capacity() > 10) {
                        notifiable_age = (sp.getBoolean("age18", false) && session.getMin_age_limit() == 18) || (sp.getBoolean("age45", false) && session.getMin_age_limit() == 45);
                        notifiable_dose = (sp.getBoolean("dose1", false) && session.getAvailable_capacity_dose1() > 0) || (sp.getBoolean("dose2", false) && session.getAvailable_capacity_dose2() > 0);
                        notifiable_vac = (sp.getBoolean("covishield", false) && session.getVaccine().equalsIgnoreCase("covishield")) || (sp.getBoolean("covaxin", false) && session.getVaccine().equalsIgnoreCase("covaxin")) || (sp.getBoolean("sputnikv", false) && session.getVaccine().equalsIgnoreCase("sputnik v"));
                        if (notifiable_age && notifiable_dose && notifiable_vac)
                            sessionsList.add(session);
                    }
                    j++;
                }
                if (center.getFee_type().equalsIgnoreCase("paid")) {
                    JSONArray jsonArray2 = jsonObject1.getJSONArray("vaccine_fees");
                    for (int k = 0; k < jsonArray2.length(); k++) {
                        JSONObject jsonObject3 = jsonArray2.getJSONObject(k);
                        vaccineFees.setCenterId(center.getCenterId());
                        vaccineFees.setVaccine(jsonObject3.getString("vaccine"));
                        vaccineFees.setFee(jsonObject3.getString("fee"));
                        vaccineFeesList.add(vaccineFees);
                    }

                }
                if (!sessionsList.isEmpty()) {
                    center.setSessionsList(sessionsList);
                    center.setVaccineFeesList(vaccineFeesList);
                    centersList.add(center);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return centersList;
    }
}
