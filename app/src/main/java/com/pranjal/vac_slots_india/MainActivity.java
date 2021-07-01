package com.pranjal.vac_slots_india;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.pranjal.vac_slots_india.adapters.RecyclerViewAdapter;
import com.pranjal.vac_slots_india.fragments.DialogFragmentHelp;

import com.pranjal.vac_slots_india.models.Centers;
import com.pranjal.vac_slots_india.models.Districts;
import com.pranjal.vac_slots_india.models.States;
import com.pranjal.vac_slots_india.utils.DistrictResponseHandler;
import com.pranjal.vac_slots_india.utils.SlotResponseHandler;
import com.pranjal.vac_slots_india.utils.StateResponseHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    private ProgressBar progressBar;
    private ProgressBar progressBar2;
    private Button getSlots;
    private RadioButton radioButton;
    private Spinner state;
    private Spinner city;
    private EditText pincode;
    private CheckBox age18;
    private CheckBox age45;
    private CheckBox dose1;
    private CheckBox dose2;
    private CheckBox covishield;
    private CheckBox covaxin;
    private CheckBox sputnikv;
    private HashMap<String, Integer> stateAndId;
    private List<String> stateName;
    private HashMap<String, Integer> districtAndId;
    private List<String> distName;
    private ArrayList<States> statesArrayList;
    private ArrayList<Districts> districtsArrayList;
    private RecyclerView recyclerView;
    private boolean isStateLoaded;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progressBar);
        progressBar2 = findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.GONE);
        progressBar2.setVisibility(View.GONE);
        if(!isOnline()) {
            isStateLoaded=false;
            new AlertDialog.Builder(this)
                    .setTitle("Connection Failure")
                    .setMessage("Please Connect to the Internet to use the app.")
                    .setPositiveButton("OKAY", (dialog, which) -> {
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        MobileAds.initialize(this, initializationStatus -> {
        });
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        updateSlots();
        getSlots = findViewById(R.id.getSlots);
        getSlots.setEnabled(false);
        RadioGroup radioGroup1 = findViewById(R.id.radioGroup);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch aSwitch = findViewById(R.id.getNotif);
        pincode = findViewById(R.id.pincode);
        pincode.setCursorVisible(true);

        Button help = findViewById(R.id.help);

        help.setOnClickListener(v -> {
            DialogFragment helpFragment = new DialogFragmentHelp();
            helpFragment.show(getSupportFragmentManager(), helpFragment.getTag());
        });

        state = findViewById(R.id.stateSelect);
        city = findViewById(R.id.citySelect);
        loadStates();

        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String stateName = parent.getItemAtPosition(position).toString();
                loadDistName(stateName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        age18 = findViewById(R.id.age18);
        age45 = findViewById(R.id.age45);
        dose1 = findViewById(R.id.dose1);
        dose2 = findViewById(R.id.dose2);
        covishield = findViewById(R.id.covishield);
        covaxin = findViewById(R.id.covaxin);
        sputnikv = findViewById(R.id.sputnikv);

        RadioButton radioButton1 = findViewById(R.id.slotByDist);
        RadioButton radioButton2 = findViewById(R.id.getSlotByPin);
        radioButton1.setChecked(sharedPreferences.getBoolean("locationDist", false));
        radioButton2.setChecked(sharedPreferences.getBoolean("locationPin", false));

        if (radioButton1.isChecked()) {
            if(!isStateLoaded) loadStates();
            pincode.setVisibility(View.INVISIBLE);
            state.setVisibility(View.VISIBLE);
            city.setVisibility(View.VISIBLE);
            if(isOnline()) getSlots.setEnabled(true);
        } else if (radioButton2.isChecked()) {
            if(isOnline()) getSlots.setEnabled(true);
            pincode.setText(sharedPreferences.getString("apiKey", ""));
            state.setVisibility(View.INVISIBLE);
            city.setVisibility(View.INVISIBLE);
            pincode.setVisibility(View.VISIBLE);
        }

        int checkedRadioButtonId = radioGroup1.getCheckedRadioButtonId();
        radioButton = findViewById(checkedRadioButtonId);
        age18.setChecked(sharedPreferences.getBoolean("age18", false));
        age45.setChecked(sharedPreferences.getBoolean("age45", false));
        dose1.setChecked(sharedPreferences.getBoolean("dose1", false));
        dose2.setChecked(sharedPreferences.getBoolean("dose2", false));
        covishield.setChecked(sharedPreferences.getBoolean("covishield", false));
        covaxin.setChecked(sharedPreferences.getBoolean("covaxin", false));
        sputnikv.setChecked(sharedPreferences.getBoolean("sputnikv", false));
        aSwitch.setChecked(sharedPreferences.getBoolean("notify", true));

        aSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
            if (isChecked) {
                if(sharedPreferences.getBoolean("isServiceRunning", false)) {
                    new Thread(() -> startService(new Intent(MainActivity.this, MyJobService.class))).start();
                    editor.putBoolean("isServiceRunning", true);
                }
                editor.putBoolean("notify", true);
                editor.apply();
            } else {
                editor.putBoolean("notify", false);
                editor.apply();
                stopService(new Intent(MainActivity.this, MyJobService.class));
            }
        });

        radioGroup1.setOnCheckedChangeListener((radioGroup, i) -> {
            if (radioGroup.getCheckedRadioButtonId() != -1) {
                int checkedRadioButtonId1 = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(checkedRadioButtonId1);
                getSlots.setEnabled(isOnline());
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
                if (radioButton.getText().toString().equals("Get Slots by District Name")) {
                    if(!isStateLoaded) loadStates();
                    pincode.setVisibility(View.INVISIBLE);
                    state.setVisibility(View.VISIBLE);
                    city.setVisibility(View.VISIBLE);
                    editor.putBoolean("locationDist", true);
                    editor.putBoolean("locationPin", false);
                    editor.apply();
                } else if (radioButton.getText().toString().equals("Get Slots by PIN Code")) {
                    state.setVisibility(View.INVISIBLE);
                    city.setVisibility(View.INVISIBLE);
                    pincode.setVisibility(View.VISIBLE);
                    editor.putBoolean("locationDist", false);
                    editor.putBoolean("locationPin", true);
                    editor.apply();
                }
            }
        });

        getSlots.setOnClickListener(view -> {
            boolean toastFlag= false;
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
            if(!sharedPreferences.getBoolean("age18", false) && !sharedPreferences.getBoolean("age45", false)) {
                Toast.makeText(
                        MainActivity.this,
                        "Please select the desired age group!",
                        Toast.LENGTH_SHORT)
                        .show();
                toastFlag = true;
            }
            if(!sharedPreferences.getBoolean("dose1", false) && !sharedPreferences.getBoolean("dose2", false)) {
                Toast.makeText(
                        MainActivity.this,
                        "Please select whether you want slots for Dose 1 or Dose 1",
                        Toast.LENGTH_SHORT)
                        .show();
                toastFlag = true;
            }
            if(!sharedPreferences.getBoolean("covishield", false) && !sharedPreferences.getBoolean("covaxin", false) && !sharedPreferences.getBoolean("sputnikv", false)) {
                Toast.makeText(
                        MainActivity.this,
                        "Please select the desired vaccine!",
                        Toast.LENGTH_SHORT)
                        .show();
                toastFlag= true;
            }
            if(!toastFlag) {
                Toast.makeText(
                        MainActivity.this,
                        "Getting Slots...",
                        Toast.LENGTH_SHORT)
                        .show();
                progressBar.setVisibility(View.VISIBLE);
            }
            if (radioButton.getText().equals("Get Slots by District Name")) {
                editor.putInt("selectedState", state.getSelectedItemPosition());
                editor.putInt("selectedDist", city.getSelectedItemPosition());
                Integer distId = districtAndId.get(city.getSelectedItem().toString());
                editor.putString("apiKey", Integer.toString(distId));
                editor.apply();
                updateSlots();
                progressBar.setVisibility(View.GONE);
            } else if (radioButton.getText().equals("Get Slots by PIN Code")) {
                editor.putString("apiKey", pincode.getText().toString());
                editor.apply();
                updateSlots();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void ifChecked(View view){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
        editor.putBoolean("age18", age18.isChecked());
        editor.putBoolean("age45", age45.isChecked());
        editor.putBoolean("dose1", dose1.isChecked());
        editor.putBoolean("dose2", dose2.isChecked());
        editor.putBoolean("covishield", covishield.isChecked());
        editor.putBoolean("covaxin", covaxin.isChecked());
        editor.putBoolean("sputnikv", sputnikv.isChecked());
        editor.apply();
        updateSlots();
    }

    private void showDistricts() {
        city.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, distName));
        try {
            city.setSelection(sharedPreferences.getInt("selectedDist", 0), true);
        }
        catch(Exception e) {
            city.setSelection(0, true);
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
            editor.putInt("selectedDist", 0);
            editor.apply();
        }
    }

    private void showStates() {
        state.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, stateName));
        try {
            state.setSelection(sharedPreferences.getInt("selectedState", 0), true);
        }
        catch(Exception e) {
            state.setSelection(0, true);
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
            editor.putInt("selectedState", 0);
            editor.apply();
        }
    }

    private void loadDistName(String stateName) {
        districtAndId = new HashMap<>();
        distName = new ArrayList<>();
        String distURL = "https://cdn-api.co-vin.in/api/v2/admin/location/districts/"
                + stateAndId.get(stateName);
        StringRequest request = new StringRequest(Request.Method.GET, distURL, response -> {
            districtsArrayList = DistrictResponseHandler.handleJsonResponse(response);
            for(Districts districts : districtsArrayList) {
                distName.add(districts.getDistrictName());
                districtAndId.put(districts.getDistrictName(), districts.getDistrictId());
            }
            showDistricts();
            progressBar2.setVisibility(View.GONE);
        }, volleyError -> {
            volleyError.printStackTrace();
            Toast.makeText(
                    this,
                    "ERROR RETRIEVING NAMES OF DISTRICTS " + volleyError.getMessage(),
                    Toast.LENGTH_SHORT)
                    .show();
            progressBar2.setVisibility(View.GONE);
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void loadStates() {
        progressBar2.setVisibility(View.VISIBLE);
        stateAndId = new HashMap<>();
        stateName = new ArrayList<>();
        String stateURL = "https://cdn-api.co-vin.in/api/v2/admin/location/states";
        StringRequest request = new StringRequest(Request.Method.GET, stateURL, response -> {
            statesArrayList = StateResponseHandler.handleJsonResponse(response);
            for(States state : statesArrayList) {
                stateName.add(state.getStateName());
                stateAndId.put(state.getStateName(), state.getStateId());
            }
            isStateLoaded = true;
            showStates();
        }, volleyError -> {
            volleyError.printStackTrace();
            Toast.makeText(
                    this,
                    "ERROR RETRIEVING NAMES OF STATES " + volleyError.getMessage(),
                    Toast.LENGTH_SHORT)
                    .show();
            isStateLoaded = false;
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }

    private void updateSlots() {
        String apiKey = sharedPreferences.getString("apiKey", null);
        if(apiKey!= null) {
            if (apiKey.length() > 3)
                updateSlotByPin(apiKey);
            else
                updateSlotByDist(apiKey);
        }
    }

    private void updateSlotByDist(String apiKey) {
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String slotByDistURL="https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByDistrict?district_id="
                + apiKey + "&date=" + date + "extraStuffToPreventCaching" + System.currentTimeMillis();
        StringRequest request=new StringRequest(Request.Method.GET, slotByDistURL, this::onResponse , this::onErrorResponse);
        request.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.getCache().clear();
        requestQueue.add(request);
    }

    private void updateSlotByPin(String apiKey) {
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String slotByPinURL="https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByPin?pincode="
                + apiKey + "&date=" + date + "extraStuffToPreventCaching" + System.currentTimeMillis();
        StringRequest request=new StringRequest(Request.Method.GET, slotByPinURL, this::onResponse, this::onErrorResponse);
        request.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.getCache().clear();
        requestQueue.add(request);
    }

    private void onResponse(String response) {
        ArrayList<Centers> centersList = SlotResponseHandler.getCentersList(response, this);
        displaySlots(centersList);
    }

    private void onErrorResponse(VolleyError volleyError) {
        volleyError.printStackTrace();
        Toast.makeText(
                this,
                "ERROR RETRIEVING SLOTS " + volleyError.getMessage(),
                Toast.LENGTH_LONG)
                .show();
    }

    private void displaySlots(ArrayList<Centers> centersList) {
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this, centersList);
        recyclerView.setAdapter(recyclerViewAdapter);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
        editor.putBoolean("isServiceRunning", true);
        editor.apply();
        new Thread(() -> startService(new Intent(MainActivity.this, MyJobService.class))).start();
    }

    public void switchContent(DialogFragment fragment) {
        fragment.show(getSupportFragmentManager(), fragment.getTag());
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
