package com.example.expensesspark.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.expensesspark.R;
import com.example.expensesspark.adapters.DetailsModelAdapter;
import com.example.expensesspark.adapters.TransactionTableAdapter;
import com.example.expensesspark.model.AccountTable;
import com.example.expensesspark.model.DetailsModel;
import com.example.expensesspark.realm.TransactionTableHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class TransactionDetailsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Realm realm;
    FloatingActionButton fabBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_details);

        recyclerView = (RecyclerView) findViewById(R.id.detailsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        long primaryKey = getIntent().getLongExtra("PrimaryKey", 1);
        Realm realm = Realm.getDefaultInstance();

        RealmQuery<AccountTable> realmQuery  = realm.where(AccountTable.class)
                .equalTo("accountId", primaryKey);
        //RealmResults<AccountTable> AccountTableList = realmQuery.findAll();
        AccountTable accountTableObj = realmQuery.findFirst();


        List<DetailsModel> detailsModelList = new ArrayList<>();
        DetailsModel accountNameObj = new DetailsModel("Account Name", accountTableObj.getAccountName());
        DetailsModel AccountNumberObj = new DetailsModel("Account Number", accountTableObj.getAccountNumber());
        detailsModelList.add(accountNameObj);
        detailsModelList.add(AccountNumberObj);

        //BIND
        DetailsModelAdapter adapter=new DetailsModelAdapter(detailsModelList);
        recyclerView.setAdapter(adapter);
    }
}

