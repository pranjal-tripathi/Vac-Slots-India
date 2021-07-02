package com.pranjal.vac_slots_india;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.pranjal.vac_slots_india.models.Centers;
import com.pranjal.vac_slots_india.models.Sessions;
import com.pranjal.vac_slots_india.models.VaccineFees;

import java.util.List;

public class NotificationView extends Activity {
    @Override
    public void onCreate(Bundle bundle) {

        super.onCreate(bundle);
        setContentView((int) R.layout.activity_notification_view);
        MobileAds.initialize(this, initializationStatus -> {
        });

        AdView mAdView = findViewById(R.id.notifAd);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Intent intent = getIntent();
        Bundle bundle1 = intent.getExtras();
        Centers center = bundle1.getParcelable("center");
        StringBuilder text= new StringBuilder();
        text.append(center.toStringDialog());
        List<VaccineFees> vaccineFeesList = center.getVaccineFeesList();
        for(VaccineFees vaccineFees : vaccineFeesList)
            text.append(vaccineFees.toStringDialog());
        text.append("\n");
        List<Sessions> sessionsList = center.getSessionsList();
        for(Sessions session : sessionsList)
            text.append(session.toStringDialog());

        TextView notification_text = (TextView) findViewById(R.id.notifText);
        notification_text.setText(text);
    }
}
