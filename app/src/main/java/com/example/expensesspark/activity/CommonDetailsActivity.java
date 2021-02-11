package com.example.expensesspark.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.example.expensesspark.R;
import com.example.expensesspark.adapters.DetailsModelAdapter;
import com.example.expensesspark.model.AccountTable;
import com.example.expensesspark.model.DetailsModel;
import com.example.expensesspark.model.TransactionTable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

public class CommonDetailsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Realm realm;
    FloatingActionButton fabBtn;

    private FloatingActionButton fab_main, fab1_mail, fab2_share;
    private Animation fab_open, fab_close, fab_clock, fab_anticlock;
    TextView textview_mail, textview_share;

    Boolean isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_details);

        recyclerView = (RecyclerView) findViewById(R.id.detailsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        fab_main = findViewById(R.id.fab);
        fab1_mail = findViewById(R.id.fab1);
        fab2_share = findViewById(R.id.fab2);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_clock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_clock);
        fab_anticlock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_anticlock);

        textview_mail = (TextView) findViewById(R.id.textview_mail);
        textview_share = (TextView) findViewById(R.id.textview_share);


        String activityName = getIntent().getStringExtra("ActivityName");

        long primaryKey = getIntent().getLongExtra("PrimaryKey", 1);
        Realm realm = Realm.getDefaultInstance();

        if (activityName.equals("AccountListActivity")) {

            RealmQuery<AccountTable> realmQuery = realm.where(AccountTable.class)
                    .equalTo("accountId", primaryKey);
            //RealmResults<AccountTable> AccountTableList = realmQuery.findAll();
            AccountTable accountTableObj = realmQuery.findFirst();


            List<DetailsModel> detailsModelList = new ArrayList<>();
            DetailsModel accountNameObj = new DetailsModel("Account Name", accountTableObj.getAccountName());
            DetailsModel accountNumberObj = new DetailsModel("Account Number", accountTableObj.getAccountNumber());
            DetailsModel accountBalanceObj = new DetailsModel("Account Balance", String.valueOf(accountTableObj.getBalance()));
            DetailsModel accountTypeObj = new DetailsModel("Account Type", accountTableObj.getAccountType());
            DetailsModel accountCurrencyObj = new DetailsModel("Currency Type", accountTableObj.getCurrencyType());


            detailsModelList.add(accountNameObj);
            detailsModelList.add(accountNumberObj);
            detailsModelList.add(accountBalanceObj);
            detailsModelList.add(accountTypeObj);
            detailsModelList.add(accountCurrencyObj);

            //BIND
            DetailsModelAdapter adapter = new DetailsModelAdapter(detailsModelList);
            recyclerView.setAdapter(adapter);


        } else if (activityName.equals("TransactionListActivity")) {
            RealmQuery<TransactionTable> realmQuery = realm.where(TransactionTable.class)
                    .equalTo("transactionId", primaryKey);
            //RealmResults<AccountTable> AccountTableList = realmQuery.findAll();
            TransactionTable transactionTableObj = realmQuery.findFirst();


            List<DetailsModel> detailsModelList = new ArrayList<>();
            DetailsModel transactionTypeObj = new DetailsModel("Transaction Type", transactionTableObj.getTransactionType());
            DetailsModel transactionAmountObj = new DetailsModel("Transaction Amount", String.valueOf(transactionTableObj.getAmount()));
            DetailsModel transactionAccountObj = new DetailsModel("Transaction Account", transactionTableObj.getTransactionAccount());
            DetailsModel transactionCategoryObj = new DetailsModel("Transaction Category", transactionTableObj.getCategory());
            DetailsModel transactionDateObj = new DetailsModel("Transaction date", transactionTableObj.getDate());
            DetailsModel transactionTimeObj = new DetailsModel("Transaction Time", transactionTableObj.getTime());
            DetailsModel transactionDescriptionObj = new DetailsModel("Transaction Description", transactionTableObj.getDescription());
            DetailsModel transactionPaymentModeObj = new DetailsModel("Transaction Payment Mode", transactionTableObj.getPaymentMode());
            DetailsModel transactionLocationObj = new DetailsModel("Transaction Location", transactionTableObj.getLocation());

            detailsModelList.add(transactionTypeObj);
            detailsModelList.add(transactionAmountObj);
            detailsModelList.add(transactionAccountObj);
            detailsModelList.add(transactionCategoryObj);
            detailsModelList.add(transactionDateObj);
            detailsModelList.add(transactionTimeObj);
            detailsModelList.add(transactionDescriptionObj);
            detailsModelList.add(transactionPaymentModeObj);
            detailsModelList.add(transactionLocationObj);

            //BIND
            DetailsModelAdapter adapter = new DetailsModelAdapter(detailsModelList);
            recyclerView.setAdapter(adapter);

        }


        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isOpen) {

                    textview_mail.setVisibility(View.INVISIBLE);
                    textview_share.setVisibility(View.INVISIBLE);
                    fab2_share.startAnimation(fab_close);
                    fab1_mail.startAnimation(fab_close);
                    fab_main.startAnimation(fab_anticlock);
                    fab2_share.setClickable(false);
                    fab1_mail.setClickable(false);
                    isOpen = false;
                } else {
                    textview_mail.setVisibility(View.VISIBLE);
                    textview_share.setVisibility(View.VISIBLE);
                    fab2_share.startAnimation(fab_open);
                    fab1_mail.startAnimation(fab_open);
                    fab_main.startAnimation(fab_clock);
                    fab2_share.setClickable(true);
                    fab1_mail.setClickable(true);
                    isOpen = true;
                }

            }
        });
    }
}
