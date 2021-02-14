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
            TransactionTableHelper helper=new TransactionTableHelper(realm);
            transactionTableList=helper.retrieveTransactionItemsByType("Income");

            //BIND
            adapter=new TransactionTableAdapter(transactionTableList);
            recyclerView.setAdapter(adapter);
        }
        else
        {
            Toast.makeText(this,"Please config for show Expense",Toast.LENGTH_SHORT);
        }


        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addTransaction = new Intent(getApplicationContext(), AddTransaction.class);
                startActivity(addTransaction);
            }
        });
    }


}