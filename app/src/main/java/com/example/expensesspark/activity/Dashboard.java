package com.example.expensesspark.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.accounts.Account;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.expensesspark.R;
import com.example.expensesspark.model.AccountTable;
import com.example.expensesspark.model.TransactionTable;
import com.example.expensesspark.realm.TransactionTableHelper;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class Dashboard extends AppCompatActivity {

    GridLayout menuGridLayout;
    Realm realm;
    TextView incomeTv, greetTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        realm = Realm.getDefaultInstance();

        menuGridLayout = (GridLayout) findViewById(R.id.menuGridLayout);
        incomeTv = (TextView) findViewById(R.id.incomeTv);
        greetTv = (TextView) findViewById(R.id.welcomeHintTv);

        int child_count = menuGridLayout.getChildCount();
        for(int i =0;i<child_count;i++){
            menuGridLayout.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int selectedPosition = menuGridLayout.indexOfChild(v);
                   navigate(selectedPosition);
                }
            });
        }

        showDashboardDetails();
    }

    public void navigate(int position)
    {
        if (position == 0)
        {
            Intent i = new Intent(this, AccountListActivity.class);
            startActivity(i);
        }
        else if (position == 1)
        {
            Intent i = new Intent(this, TransactionsListActivity.class);
            i.putExtra("ActivityName", "Income");
            startActivity(i);
        }
        else if (position == 2)
        {
            Intent i = new Intent(this, TransactionsListActivity.class);
            i.putExtra("ActivityName", "Expense");
            startActivity(i);
        }
        else if (position == 3)
       {
         Intent i = new Intent(this, AddTransaction.class);
         startActivity(i);
       }
        else if (position == 4)
        {
            Intent i = new Intent(this, ChartActivity.class);
            startActivity(i);
        }
        else if (position == 5)
        {
            Intent i = new Intent(this, AccountSetings.class);
            startActivity(i);
        }
    }

    private void showDashboardDetails() {
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            greetTv.setText("Welcome " + acct.getDisplayName() + "!");
        } else {
            greetTv.setText("Welcome!");
        }

        RealmQuery<TransactionTable> incomeTransactionTableResults = realm.where(TransactionTable.class)
                .equalTo("transactionType", "Income");
        double totalIncomeAmt = incomeTransactionTableResults.sum("amount").doubleValue();

        incomeTv.setText("₹" + String.valueOf(totalIncomeAmt));

        RealmQuery<TransactionTable> expenseTransactionTableResults = realm.where(TransactionTable.class)
                .equalTo("transactionType", "Expense");
        double totalExpenseAmt = expenseTransactionTableResults.sum("amount").doubleValue();

        incomeTv.setText("₹" + String.valueOf(totalExpenseAmt));

    }
}