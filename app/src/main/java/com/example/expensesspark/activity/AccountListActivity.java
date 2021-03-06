package com.example.expensesspark.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensesspark.R;
import com.example.expensesspark.adapters.AccountTableAdapter;
import com.example.expensesspark.adapters.TransactionTableAdapter;
import com.example.expensesspark.model.AccountTable;
import com.example.expensesspark.model.TransactionTable;
import com.example.expensesspark.realm.AccountTableHelper;
import com.example.expensesspark.realm.TransactionTableHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import io.realm.Realm;
import io.realm.Sort;

public class AccountListActivity extends AppCompatActivity {

    List<AccountTable> accountTableList;
    AccountTableAdapter adapter;
    RecyclerView recyclerView;
    Realm realm;
    FloatingActionButton fabBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_list);

        fabBtn = findViewById(R.id.fabBtn);


        //SETUP RECYCLERVIEW
        recyclerView= (RecyclerView) findViewById(R.id.accountRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //SETUP REEALM
        realm = Realm.getDefaultInstance();
        String activityName = getIntent().getStringExtra("ActivityName");


            //RETRIEVE
            AccountTableHelper helper=new AccountTableHelper(realm);
            accountTableList = helper.retrieveAccountTableItems();

            //BIND
            adapter = new AccountTableAdapter(accountTableList, activityName);
            recyclerView.setAdapter(adapter);


        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newAccountActivity = new Intent(getApplicationContext(), NewAccountActivity.class);
                newAccountActivity.putExtra("Activity", "Add");
                newAccountActivity.putExtra("ActivityType", "");
                startActivity(newAccountActivity);
            }
        });
    }
}