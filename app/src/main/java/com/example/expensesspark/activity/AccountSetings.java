package com.example.expensesspark.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.expensesspark.R;
import com.example.expensesspark.app.NotificationHelper;
import com.example.expensesspark.model.TransactionTable;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmQuery;

public class AccountSetings extends AppCompatActivity {

    Button logOutBtn, deleteAccountBtn;
    TextView nameTv, emailTv, greetTv;
    Switch enableNotification;
    Spinner repeatAlertSpinner;
    private FirebaseAuth mAuth;
    ImageView profileImgView;
    private GoogleSignInClient mGoogleSignInClient;
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setings);

        nameTv = (TextView) findViewById(R.id.nameValueTv);
        emailTv = (TextView) findViewById(R.id.emailValueTv);
        greetTv = (TextView) findViewById(R.id.greetTv);
        repeatAlertSpinner = (Spinner) findViewById(R.id.repeatNotificationSpinner);
        logOutBtn = (Button) findViewById(R.id.logOutBtn);
        deleteAccountBtn = (Button) findViewById(R.id.deleteAccountBtn);
        enableNotification = (Switch) findViewById(R.id.enableNotificationSwitch);
        profileImgView = (ImageView) findViewById(R.id.profile_img_view);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            nameTv.setText(personName);
            emailTv.setText(personEmail);
            greetTv.setText("Hello " + personGivenName + "!");
            Glide.with(this).load(personPhoto).into(profileImgView);
        }

//        if (currentUser != null) {
//            nameTv.setText(currentUser.getDisplayName());
//            emailTv.setText(currentUser.getEmail());
//            greetTv.setText("Hello " + currentUser.getdi + "!");
//
//            Uri imgUri = currentUser.getPhotoUrl();
//            profileImgView.setImageURI(null);
//            profileImgView.setImageURI(imgUri);
//        } else {
//            nameTv.setText("--");
//            emailTv.setText("--");
//            greetTv.setText("Hello!");
//            profileImgView.setBackgroundResource(R.drawable.profile);
//        }

        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        deleteAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                revokeAccess();
                clearLocalDB();
            }
        });

        scheduleNotification(getNotification( "5 second delay" ), 5000);
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(),"Successfully Logged Out",Toast.LENGTH_LONG);
                        loginActivity();
                    }
                });
    }

    private void revokeAccess() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(),"Successfully account deleted and logged Out",Toast.LENGTH_LONG);
                        loginActivity();
                    }
                });
    }

    private void loginActivity() {
        Intent mainActivity = new Intent(this, LoginActivity.class);
        startActivity(mainActivity);
    }

    private void clearLocalDB() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        // delete all realm objects
        realm.deleteAll();
        //commit realm changes
        realm.commitTransaction();
    }

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

        Intent intent = new Intent(this, Dashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder( this, default_notification_channel_id ) ;
        builder.setContentTitle( "Expenses Spark Notification" ) ;
        builder.setContentText("You've earned ₹" + String.valueOf(totalIncomeAmt) + " & spent ₹" + String.valueOf(totalExpenseAmt) + " of " + currentDate);
        builder.setSmallIcon(R.drawable. receivecash ) ;
        builder.setAutoCancel( true ) ;
        builder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
        builder.setContentIntent(pendingIntent);
        return builder.build() ;
    }
}