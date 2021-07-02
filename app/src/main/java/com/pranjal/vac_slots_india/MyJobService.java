package com.pranjal.vac_slots_india;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pranjal.vac_slots_india.data.DbHandler;
import com.pranjal.vac_slots_india.models.Centers;
import com.pranjal.vac_slots_india.models.Sessions;
import com.pranjal.vac_slots_india.params.Params;
import com.pranjal.vac_slots_india.utils.SlotResponseHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MyJobService extends Service {
    SharedPreferences sharedPreferences;
    SQLiteDatabase db_write, db_read;
    DbHandler dbHandler;
    Notification notification;
    NotificationCompat.Builder notif;
    Notification.Builder notifBuild;
    NotificationCompat.Builder notifCompatBuild;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Slot Notif";
            String description = "";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("Slot-Finder-Notif-Channel", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        //Log.d("test", "onStartCommand function start");
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(sharedPreferences.getBoolean("notify", true)) {
            dbHandler = new DbHandler(this);
            db_write = dbHandler.getWritableDatabase();
            db_read = dbHandler.getReadableDatabase();
            createNotificationChannel();
            updateNotification();
            updateSlots();
        }
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int sometime = 10 * 1000;
        long triggerTime = SystemClock.elapsedRealtime() + sometime;
        Intent intent1 = new Intent(this, MyJobService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent1, 0);
        alarmManager.cancel(pendingIntent);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, pendingIntent);
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    private void updateNotification() {
        //Log.d("test", "updateNotification function start");
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent, 0);
        String bigText = "", text = "", title = "";
        if(!isOnline()) {
            title = "Searching Stopped!";
            bigText = "Unable to search for slots. " +
                    "Internet connection not detected. ";
            text = "Unable to search slots. No Internet.";
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                notification = notifBuild.setContentTitle(title)
                        .setStyle(new Notification.BigTextStyle()
                                .bigText(bigText))
                        .setSmallIcon(R.drawable.small_icon)
                        .setContentIntent(pendingIntent)
                        .setPriority(Notification.PRIORITY_LOW)
                        .build();
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                notificationManager.notify(2, notification);
            }
            else {
                notifCompatBuild = new NotificationCompat.Builder(this, "Slot-Finder-Permanent-Notif-Channel");
                notif = notifCompatBuild
                        .setContentTitle(title)
                        .setContentText(text)
                        .setSmallIcon(R.drawable.small_icon)
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_LOW);
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                notificationManager.notify(2, notif.build());
            }
        }
        else {
            title = "Searching Slots";
            bigText = "App is searching for available slots in the background. " +
                    "Make sure that you remain connected to the internet. " +
                    "Please DO NOT kill the process.";
            text = "Searching for available slots.";
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                notifBuild = new Notification.Builder(this, "Slot-Finder-Permanent-Notif-Channel");
                notification = notifBuild.setContentTitle(title)
                        .setStyle(new Notification.BigTextStyle()
                                .bigText(bigText))
                        .setSmallIcon(R.drawable.small_icon)
                        .setContentIntent(pendingIntent)
                        .setPriority(Notification.PRIORITY_LOW)
                        .build();
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                notificationManager.notify(2, notification);
            }
            else {
                notifCompatBuild = new NotificationCompat.Builder(this, "Slot-Finder-Permanent-Notif-Channel");
                notif = notifCompatBuild
                        .setContentTitle(title)
                        .setContentText(text)
                        .setSmallIcon(R.drawable.small_icon)
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_LOW);
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                notificationManager.notify(2, notif.build());
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        //Log.d("test", "onCreate function start");
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(sharedPreferences.getBoolean("notify", true)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = "Slot Searching (Permanent Notification)";
                String description = "";
                int importance = NotificationManager.IMPORTANCE_LOW;
                NotificationChannel channel = new NotificationChannel("Slot-Finder-Permanent-Notif-Channel", name, importance);
                channel.setDescription(description);
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }
            Intent notificationIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent =
                    PendingIntent.getActivity(this, 0, notificationIntent, 0);
            String bigText = "", text = "", title = "";
            title = "Searching Slots";
            bigText = "App is searching for available slots in the background. " +
                    "Make sure that you remain connected to the internet. " +
                    "Please DO NOT kill the process.";
            text = "Searching for available slots.";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                notifBuild = new Notification.Builder(this, "Slot-Finder-Permanent-Notif-Channel");
                notification = notifBuild.setContentTitle(title)
                        .setStyle(new Notification.BigTextStyle()
                                .bigText(bigText))
                        .setSmallIcon(R.drawable.small_icon)
                        .setContentIntent(pendingIntent)
                        .setPriority(Notification.PRIORITY_LOW)
                        .build();
                startForeground(2, notification);
            } else {
                notifCompatBuild = new NotificationCompat.Builder(this, "Slot-Finder-Permanent-Notif-Channel");
                notif = notifCompatBuild
                        .setContentTitle(title)
                        .setContentText(text)
                        .setSmallIcon(R.drawable.small_icon)
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_LOW);
                startForeground(2, notif.build());
            }
        }
        super.onCreate();
    }

    private void updateSlots() {

        if(isOnline()) {
            String apiKey = sharedPreferences.getString("apiKey", null);
            if (apiKey != null) {
                if (apiKey.length() > 3)
                    updateSlotByPin(apiKey);
                else
                    updateSlotByDist(apiKey);
            }
        }
    }

    private void updateSlotByDist(String apiKey) {
        //Log.d("test", "updateSlotByDist function start");
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
        //Log.d("test", "onResponse function start - response received");
        ArrayList<Centers> centersList = SlotResponseHandler.getCentersList(response, this);
        notifiable_sessions(centersList);
    }

    private void onErrorResponse(VolleyError volleyError) {
        volleyError.printStackTrace();
    }

    private void notifiable_sessions(List<Centers> centersList1) {
        //Log.d("test", "notifiable_sessions function start");
        String[] projection = {
                Params.KEY_NOTIFIED
        };
        String selection = Params.KEY_SESSIONID + " = ?";
        for(Centers center : centersList1) {
            List<Sessions> new_session_list = new ArrayList<>();
            String sessionId;
            List<Sessions> sessionsList1 = center.getSessionsList();
            for(Sessions session : sessionsList1) {
                sessionId = session.getSession_id();
                String[] selectionArgs = {sessionId};
                Cursor cursor = db_read.query(
                        Params.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null
                );
                if (cursor.getCount() == 0){
                    //Log.d("test", "inside if block, new session added!");
                    new_session_list.add(session);
                }
                cursor.close();
            }
            center.setSessionsList(new_session_list);
        }
        sendNotif(centersList1);
        updateDb(centersList1);
    }

    private void sendNotif(List<Centers> centersListnotif) {
        //Log.d("test", "sendNotif function start");
        StringBuilder notif_text= new StringBuilder();
        String notif_title="";
        int notif_id=3;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(preferences.getBoolean("notify", true)) {
            for(Centers center : centersListnotif) {
                for (Sessions session : center.getSessionsList()) {
                    notif_text.append("Age ").append(session.getMin_age_limit()).append("+ | ")
                            .append(session.getVaccine()).append(" | ").append(session.getDate())
                            .append(" | ").append("D1 - ").append(session.getAvailable_capacity_dose1())
                            .append(" | D2 - ").append(session.getAvailable_capacity_dose2()).append("\n");
                }
                if(center.getSessionsList().size() != 0) {
                    notif_title = "Slot Available at " + center.getCenterName() + " | " + center.getPincode();
                    Intent intent = new Intent(this, NotificationView.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("notifMsg", notif_title+"\n\n"+notif_text);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("center", center);
                    intent.putExtras(bundle);
                    int requestId = (int) System.currentTimeMillis();
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, requestId, intent, 0);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Slot-Finder-Notif-Channel")
                            .setSmallIcon(R.drawable.small_icon)
                            .setContentTitle(notif_title)
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(notif_text.toString()))
                            .setPriority(NotificationCompat.PRIORITY_MAX)
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true);
                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                    notificationManager.notify(notif_id, builder.build());
                    //Log.d("test", "sendNotif notification sent - " + requestId);
                    notif_text = new StringBuilder();
                    notif_id++;
                }
            }
        }
    }

    private void updateDb(List<Centers> centersList1) {
        //Log.d("test", "updateDb function start");
        for (Centers center : centersList1) {
            List<Sessions> list;
            list = center.getSessionsList();
            for (Sessions session : list) {
                String sessionId = session.getSession_id();
                String sessionName = center.getCenterName();
                String notified = "true";
                ContentValues values = new ContentValues();
                values.put(Params.KEY_SESSIONID, sessionId);
                values.put(Params.KEY_SESSION_NAME, sessionName);
                values.put(Params.KEY_NOTIFIED, notified);
                long newRowId = db_write.insert(Params.TABLE_NAME, null, values);
                //Log.d("test", "DB Updated - " + newRowId);
            }
        }
        clearDb();
    }

    private void clearDb() {
        String[] projection = {
                Params.KEY_ID
        };
        Cursor cursor = db_read.query(
                Params.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );
        int size = cursor.getCount();
        int i=0;
        if(size > 0) {
            cursor.moveToFirst();
            i = cursor.getInt(cursor.getColumnIndexOrThrow(Params.KEY_ID));
        }
        cursor.close();
        if(size > 200) {
            int extra = size - 200;
            extra += i;
            for (; i < extra; i++) {
                String selection = Params.KEY_ID + " LIKE ?";
                String[] selectionArgs = {Integer.toString(i)};
                int deletedRows = db_write.delete(Params.TABLE_NAME, selection, selectionArgs);
                //Log.d("test", "rows deleted");
            }
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void onDestroy() {
        Log.d("test", "onDestroy function called");
        dbHandler.close();
        super.onDestroy();
    }
}
