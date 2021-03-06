package com.example.expensesspark.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;

import android.accounts.Account;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
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
import com.example.expensesspark.app.NotificationHelper;
import com.example.expensesspark.model.AccountTable;
import com.example.expensesspark.model.TransactionTable;
import com.example.expensesspark.realm.TransactionTableHelper;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class Dashboard extends AppCompatActivity {

    GridLayout menuGridLayout;
    Realm realm;
    TextView incomeTv, greetTv, cashTv, savingAccountTv, expenseTv;
    //notifi in acc settings
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;


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

        //notifi in acc settings
        scheduleNotification(getNotification( "1 second delay" ), 1000);

        showDashboardDetails();
        sendNotification();
    }

    public void navigate(int position)
    {
        if (position == 0)
        {
            Intent i = new Intent(this, AccountListActivity.class);
            i.putExtra("ActivityName", "Account");
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

    //notifi in acc settings
    private void scheduleNotification (Notification notification , int delay) {
        Intent notificationIntent = new Intent( this, NotificationHelper. class ) ;
        notificationIntent.putExtra(NotificationHelper. NOTIFICATION_ID , 1 ) ;
        notificationIntent.putExtra(NotificationHelper. NOTIFICATION , notification) ;
        PendingIntent pendingIntent = PendingIntent. getBroadcast ( this, 0 , notificationIntent , PendingIntent. FLAG_UPDATE_CURRENT ) ;
        long futureInMillis = SystemClock. elapsedRealtime () + delay ;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context. ALARM_SERVICE ) ;
        assert alarmManager != null;
        alarmManager.set(AlarmManager. ELAPSED_REALTIME_WAKEUP , futureInMillis , pendingIntent) ;
    }
    private Notification getNotification (String content) {
        Realm realm = Realm.getDefaultInstance();
        RealmQuery<TransactionTable> incomeTransactionTableResults = realm.where(TransactionTable.class)
                .equalTo("transactionType", "Income");
        double totalIncomeAmt = incomeTransactionTableResults.sum("amount").doubleValue();

        RealmQuery<TransactionTable> expenseTransactionTableResults = realm.where(TransactionTable.class)
                .equalTo("transactionType", "Expense");
        double totalExpenseAmt = expenseTransactionTableResults.sum("amount").doubleValue();

        String currentDate = new SimpleDateFormat("MMMM, yyyy", Locale.getDefault()).format(new Date());

        //Intent intent = new Intent(this, Dashboard.class);
       // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder( this, default_notification_channel_id ) ;
        builder.setContentTitle( "Expenses Spark Notification" ) ;
        builder.setContentText("You've earned ₹" + String.valueOf(totalIncomeAmt) + " & spent ₹" + String.valueOf(totalExpenseAmt) + " of " + currentDate);
        builder.setSmallIcon(R.drawable. receivecash ) ;
        builder.setAutoCancel( true ) ;
        builder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
      //  builder.setContentIntent(pendingIntent);
        return builder.build() ;
    }
}