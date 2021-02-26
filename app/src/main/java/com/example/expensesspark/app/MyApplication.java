package com.example.expensesspark.app;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.example.expensesspark.R;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("ExpensesSpark.realm")
                .schemaVersion(11)
                .allowWritesOnUiThread(true)
                .build();
        Realm.setDefaultConfiguration(config);

        createNotificationChannel();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            String description = "Notifications for track expenses and incomes";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("10001", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}

//    RealmResults<User> results = realm.where(User.class).findAll();
//    long   sum     = results.sum("age").longValue();
//    long   min     = results.min("age").longValue();
//    long   max     = results.max("age").longValue();
//    double average = results.average("age");
//
//    long   matches = results.size();