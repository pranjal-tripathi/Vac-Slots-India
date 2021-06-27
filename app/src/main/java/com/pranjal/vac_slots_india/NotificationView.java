package com.pranjal.vac_slots_india;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

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

        TextView notification_text = (TextView) findViewById(R.id.notifText);
        Intent intent = getIntent();
        String text = intent.getStringExtra("notifMsg");
        notification_text.setText(text);
    }
}
