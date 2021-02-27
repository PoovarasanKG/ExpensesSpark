package com.example.expensesspark.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.expensesspark.R;
import com.example.expensesspark.adapters.TransactionTableAdapter;
import com.example.expensesspark.model.TransactionTable;
import com.example.expensesspark.realm.TransactionTableHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class TransactionsListActivity extends AppCompatActivity {

    List<TransactionTable> transactionTableList;
    TransactionTableAdapter adapter;
    RecyclerView recyclerView;
    Realm realm;
    FloatingActionButton fabButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions_list);

        fabButton = (FloatingActionButton) findViewById(R.id.fabButton);

        //SETUP RECYCLERVIEW
        recyclerView= (RecyclerView) findViewById(R.id.transactionsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //SETUP REEALM
        realm = Realm.getDefaultInstance();

        String activityName = getIntent().getStringExtra("ActivityName");

        if (activityName.equals("Income")) {
            //RETRIEVE
            //TransactionTableHelper helper=new TransactionTableHelper(realm);
            //transactionTableList=helper.retrieveTransactionItemsByType("Income");

            transactionTableList = realm.where(TransactionTable.class)
                    .equalTo("transactionType", "Income")
                    .findAll().sort("dateType", Sort.DESCENDING);
            //BIND
            adapter=new TransactionTableAdapter(transactionTableList, activityName);
            recyclerView.setAdapter(adapter);
        }
        else if (activityName.equals("Expense")) {
            //RETRIEVE
            TransactionTableHelper helper=new TransactionTableHelper(realm);
            transactionTableList=helper.retrieveTransactionItemsByType("Expense");

            //BIND
            adapter=new TransactionTableAdapter(transactionTableList, activityName);
            recyclerView.setAdapter(adapter);
        }
        else
        {
           Toast.makeText(getApplicationContext(),"Please config for List", Toast.LENGTH_SHORT);
        }

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addTransaction = new Intent(getApplicationContext(), AddTransaction.class);
                addTransaction.putExtra("Activity", "Add");
                addTransaction.putExtra("ActivityType", "");
                startActivity(addTransaction);
            }
        });
    }
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

}