package com.example.expensesspark.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;

import android.accounts.Account;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
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
    TextView incomeTv, greetTv, cashTv, savingAccountTv, expenseTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        realm = Realm.getDefaultInstance();

        menuGridLayout = (GridLayout) findViewById(R.id.menuGridLayout);
        incomeTv = (TextView) findViewById(R.id.incomeTv);
        greetTv = (TextView) findViewById(R.id.welcomeHintTv);
        cashTv = (TextView) findViewById(R.id.cashTv);
        savingAccountTv = (TextView) findViewById(R.id.bankTv);
        expenseTv = (TextView) findViewById(R.id.expenseTv);

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
        sendNotification();
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
         Intent i = new Intent(this, ReportActivity.class);
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

        expenseTv.setText("₹" + String.valueOf(totalExpenseAmt));

        RealmQuery<AccountTable> cashAccountTableResults = realm.where(AccountTable.class)
                .equalTo("accountType", "Cash");
        double totalCashAmt = cashAccountTableResults.sum("balance").doubleValue();

        cashTv.setText("₹" + String.valueOf(totalCashAmt));

        RealmQuery<AccountTable> bankAccountTableResults = realm.where(AccountTable.class)
                .equalTo("accountType", "Bank");
        double totalBankAmt = bankAccountTableResults.sum("balance").doubleValue();

        savingAccountTv.setText("₹" + String.valueOf(totalBankAmt));
    }

    public void sendNotification()
    {
        Intent intent = new Intent(getApplicationContext(), Dashboard.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder b = new NotificationCompat.Builder(getApplicationContext());

        b.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.receivecash)
                .setTicker("Hearty365")
                .setContentTitle("Default notification")
                .setContentText("Lorem ipsum dolor sit amet, consectetur adipiscing elit.")
                .setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent)
                .setContentInfo("Info");


        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, b.build());
    }
}