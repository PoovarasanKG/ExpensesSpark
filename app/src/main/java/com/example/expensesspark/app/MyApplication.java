package com.example.expensesspark.app;

import android.app.Application;

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
    }
}

//    RealmResults<User> results = realm.where(User.class).findAll();
//    long   sum     = results.sum("age").longValue();
//    long   min     = results.min("age").longValue();
//    long   max     = results.max("age").longValue();
//    double average = results.average("age");
//
//    long   matches = results.size();