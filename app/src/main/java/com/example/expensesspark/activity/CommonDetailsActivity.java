package com.example.expensesspark.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
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
import io.realm.RealmResults;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

public class CommonDetailsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Realm realm;
    FloatingActionButton fabBtn;

    private FloatingActionButton menu_button, delete_button, edit_button ;
    private Animation fab_open, fab_close, fab_clock, fab_anticlock;
    TextView edit_text_view, delete_text_view;

    Boolean isOpen = false;
    long primaryKey;
    String activityName, activityType, listType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_details);

        recyclerView = (RecyclerView) findViewById(R.id.detailsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        menu_button = findViewById(R.id.menu_button);
        delete_button = findViewById(R.id.delete_button);
        edit_button = findViewById(R.id.edit_button);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_clock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_clock);
        fab_anticlock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_anticlock);

        edit_text_view = (TextView) findViewById(R.id.edit_text_view);
        delete_text_view = (TextView) findViewById(R.id.delete_text_view);


        activityName = getIntent().getStringExtra("ActivityName");
        activityType = getIntent().getStringExtra("ActivityType");


        primaryKey = getIntent().getLongExtra("PrimaryKey", 1);
        realm = Realm.getDefaultInstance();

        if (activityName.equals("AccountListActivity")) {
            listType = "account";
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
            listType = "transaction";

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
        else
        {
            listType = "";
        }


        menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isOpen) {
                    edit_text_view.setVisibility(View.INVISIBLE);
                    delete_text_view.setVisibility(View.INVISIBLE);
                    edit_button.startAnimation(fab_close);
                    delete_button.startAnimation(fab_close);
                    menu_button.startAnimation(fab_anticlock);
                    edit_button.setClickable(false);
                    delete_button.setClickable(false);
                    isOpen = false;
                } else {
                    edit_text_view.setVisibility(View.VISIBLE);
                    delete_text_view.setVisibility(View.VISIBLE);
                    edit_button.startAnimation(fab_open);
                    delete_button.startAnimation(fab_open);
                    menu_button.startAnimation(fab_clock);
                    edit_button.setClickable(true);
                    delete_button.setClickable(true);
                    isOpen = true;
                }

            }
        });

        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AddTransaction.class);
                i.putExtra("Activity", "Edit");
                i.putExtra("transactionId", primaryKey);
                i.putExtra("ActivityType", activityName);
                startActivity(i);
            }
        });

        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), NewAccountActivity.class);
                i.putExtra("Activity", "Edit");
                i.putExtra("accountId", primaryKey);
                i.putExtra("ActivityType", activityName);
                startActivity(i);
            }
        });

        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog diaBox = AskOption();
                diaBox.show();
            }
        });
    }

    private AlertDialog AskOption()
    {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                // set message, title, and icon
                .setTitle("Alert!")
                .setMessage("Do you want to delete this " + listType + "?")
                //.setIcon(R.drawable.add)

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code
                        if (activityName.equals("AccountListActivity")) {
                            realm.beginTransaction();
                            RealmResults<AccountTable> result = realm.where(AccountTable.class).equalTo("accountId",primaryKey).findAll();
                            result.deleteAllFromRealm();
                            realm.commitTransaction();
                            dialog.dismiss();
                            navigateActivity();

                        }
                        else  if (activityName.equals("TransactionListActivity")) {
                            realm.beginTransaction();
                            RealmResults<TransactionTable> result = realm.where(TransactionTable.class).equalTo("transactionId",primaryKey).findAll();
                            result.deleteAllFromRealm();
                            realm.commitTransaction();
                            dialog.dismiss();
                            navigateActivity();
                        }
                        }

                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                })
                .create();

        return myQuittingDialogBox;
    }

    private void navigateActivity()
    {
        if (activityType.equalsIgnoreCase("Income") || activityType.equalsIgnoreCase("Expense"))
        {
            Intent transactionsListActivity = new Intent(getApplicationContext(), TransactionsListActivity.class);
            transactionsListActivity.putExtra("ActivityName", activityType);
            startActivity(transactionsListActivity);
        }
        else if (activityType.equalsIgnoreCase("Accounts"))
        {
            Intent accountListActivity = new Intent(getApplicationContext(), AccountListActivity.class);
            accountListActivity.putExtra("ActivityName", activityType);
            startActivity(accountListActivity);
        }
        else
        {
            Intent dashboard = new Intent(getApplicationContext(), Dashboard.class);
            startActivity(dashboard);
        }
    }
}
