package com.example.expensesspark.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensesspark.R;
import com.example.expensesspark.adapters.AccountTableAdapter;
import com.example.expensesspark.adapters.TransactionTableAdapter;
import com.example.expensesspark.model.AccountTable;
import com.example.expensesspark.model.TransactionTable;
import com.example.expensesspark.realm.AccountTableHelper;
import com.example.expensesspark.realm.TransactionTableHelper;

import java.util.List;

import io.realm.Realm;

public class AccountListActivity extends AppCompatActivity {

    List<AccountTable> accountTableList;
    AccountTableAdapter adapter;
    RecyclerView recyclerView;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_list);

        //SETUP RECYCLERVIEW
        recyclerView= (RecyclerView) findViewById(R.id.accountRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //SETUP REEALM
        realm = Realm.getDefaultInstance();

        //RETRIEVE
        AccountTableHelper helper=new AccountTableHelper(realm);
        accountTableList = helper.retrieveAccountTableItems();

        //BIND
        adapter=new AccountTableAdapter(accountTableList);
        recyclerView.setAdapter(adapter);
    }
}