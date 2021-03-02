package com.example.expensesspark.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.expensesspark.R;
import com.example.expensesspark.model.AccountTable;
import com.example.expensesspark.model.TransactionTable;
import com.example.expensesspark.realm.TransactionTableHelper;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.internal.util.Pair;

public class ReportActivity extends AppCompatActivity {

    TextView flowReportTV, flowReportMonthTV, incomeTV, incomeValTV, expenseTV, expenseValTv, creditTV, creditValTV, cashTV,
             cashValTV, bankTV, bankValTV,
             categoryReportTV, categoryFlowMonthReportTV, foodTV, foodIncomeTV, foodExpenseTV, shoppingTv,
             shoppingIncomeTv, shoppingExpenseTv, transporationTV, transporationIncomeTV, transporationExpenseTV, vehicleTV, vehicleIncomeTV, vehicleExpenseTV,
             lifeAndEntertainmentTV, lifeAndEntertainmentIncomeTV, lifeAndEntertainmentExpenseTV, housingTv, housingIncomeTv, housingExpenseTv,
             communicationTV, communicationIncomeTV, communicationExpenseTV, financialExpensesTV, financialExpensesIncomeTV, financialExpensesExpenseTV,
             investmentsTV, investmentsIncomeTV, investmentsExpenseTV, categoryIncomeTV, categoryIncomeIncomeTV, categoryIncomeExpenseTV, othersTV, othersIncomeTV, othersExpenseTV, repoterName ;
    File imagePath;


    Realm realm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        flowReportTV = findViewById(R.id.flowReportTV);
        flowReportMonthTV = findViewById(R.id.flowReportMonthTV);
        incomeTV = findViewById(R.id.incomeTV);
        incomeValTV  = findViewById(R.id.incomeValTV);
        expenseTV = findViewById(R.id.expenseTV);
        expenseValTv = findViewById(R.id.expenseValTv);
        creditTV = findViewById(R.id.creditTV);
        creditValTV = findViewById(R.id.creditValTV);
        cashTV = findViewById(R.id.cashTV);
        cashValTV = findViewById(R.id.cashValTV);
        bankTV = findViewById(R.id.bankTV);
        bankValTV = findViewById(R.id.bankValTV);
        repoterName = findViewById(R.id.repoterName);

        categoryReportTV = findViewById(R.id.categoryReportTV);
        categoryFlowMonthReportTV = findViewById(R.id.categoryFlowMonthReportTV);
        foodTV = findViewById(R.id.foodTV); foodIncomeTV = findViewById(R.id.foodIncomeTV); foodExpenseTV = findViewById(R.id.foodExpenseTV);
        shoppingTv = findViewById(R.id.shoppingTv); shoppingIncomeTv = findViewById(R.id.shoppingIncomeTv); shoppingExpenseTv = findViewById(R.id.shoppingExpenseTv);
        transporationTV = findViewById(R.id.transporationTV); transporationIncomeTV = findViewById(R.id.transporationIncomeTV); transporationExpenseTV = findViewById(R.id.transporationExpenseTV);
        vehicleTV = findViewById(R.id.vehicleTV); vehicleIncomeTV = findViewById(R.id.vehicleIncomeTV); vehicleExpenseTV = findViewById(R.id.vehicleExpenseTV);
        lifeAndEntertainmentTV = findViewById(R.id.lifeAndEntertainmentTV); lifeAndEntertainmentIncomeTV = findViewById(R.id.lifeAndEntertainmentIncomeTV); lifeAndEntertainmentExpenseTV = findViewById(R.id.lifeAndEntertainmentExpenseTV);
        housingTv = findViewById(R.id.housingTv); housingIncomeTv = findViewById(R.id.housingIncomeTv); housingExpenseTv = findViewById(R.id.housingExpenseTv);
        communicationTV = findViewById(R.id.communicationTV); communicationIncomeTV = findViewById(R.id.communicationIncomeTV); communicationExpenseTV = findViewById(R.id.communicationExpenseTV);
        financialExpensesTV = findViewById(R.id.financialExpensesTV); financialExpensesIncomeTV = findViewById(R.id.financialExpensesIncomeTV); financialExpensesExpenseTV = findViewById(R.id.financialExpensesExpenseTV);
        investmentsTV = findViewById(R.id.investmentsTV); investmentsIncomeTV = findViewById(R.id.investmentsIncomeTV); investmentsExpenseTV = findViewById(R.id.investmentsExpenseTV);
        categoryIncomeTV = findViewById(R.id.categoryIncomeTV); categoryIncomeIncomeTV = findViewById(R.id.categoryIncomeIncomeTV); categoryIncomeExpenseTV = findViewById(R.id.categoryIncomeExpenseTV);
        othersTV = findViewById(R.id.othersTV); othersIncomeTV = findViewById(R.id.othersIncomeTV); othersExpenseTV = findViewById(R.id.othersExpenseTV);

        realm = Realm.getDefaultInstance();
        showIncomeAndExpenseFlowDetails();
        showReportDetails();

        FloatingActionButton share = (FloatingActionButton)findViewById(R.id.shareBtn);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPermission();
            }
        });
    }

    private void showIncomeAndExpenseFlowDetails() {
        //RealmQuery<TransactionTable> incomeTransactionTableResults = realm.where(TransactionTable.class)
        //      .equalTo("transactionType", "Income");

        String currentMonth = new SimpleDateFormat("MMMM, yyyy", Locale.getDefault()).format(new Date());
        flowReportMonthTV.setText("For month of " + currentMonth);
        categoryFlowMonthReportTV.setText("For month of " + currentMonth);


        Date startDate = getDateRange().first;
        Date endDate = getDateRange().second;

        //Income and Expenses by Flow Report
        RealmResults<TransactionTable> incomeTransactionTableResults = realm.where(TransactionTable.class)
                .equalTo("transactionType", "Income")
                .greaterThanOrEqualTo("dateType", startDate)
                .lessThan("dateType", endDate)
                .findAll().sort("dateType", Sort.ASCENDING);
        double totalIncomeAmt = incomeTransactionTableResults.sum("amount").doubleValue();
        incomeValTV.setText("₹" + String.valueOf(totalIncomeAmt));

        RealmResults<TransactionTable> expenseTransactionTableResults = realm.where(TransactionTable.class)
                .equalTo("transactionType", "Expense")
                .greaterThanOrEqualTo("dateType", startDate)
                .lessThan("dateType", endDate)
                .findAll().sort("dateType", Sort.ASCENDING);
        double totalExpenseAmt = expenseTransactionTableResults.sum("amount").doubleValue();
        expenseValTv.setText("₹" + String.valueOf(totalExpenseAmt));

        RealmQuery<AccountTable> creditAccountTableResults = realm.where(AccountTable.class)
                .equalTo("accountType", "Credit");
        double totalCreditAmt = creditAccountTableResults.sum("balance").doubleValue();
        creditValTV.setText("₹" + String.valueOf(totalCreditAmt));

        RealmQuery<AccountTable> cashAccountTableResults = realm.where(AccountTable.class)
                .equalTo("accountType", "Cash");
        double totalCashAmt = cashAccountTableResults.sum("balance").doubleValue();
        cashValTV.setText("₹" + String.valueOf(totalCashAmt));

        RealmQuery<AccountTable> bankAccountTableResults = realm.where(AccountTable.class)
                .equalTo("accountType", "Bank");
        double totalBankAmt = bankAccountTableResults.sum("balance").doubleValue();
        bankValTV.setText("₹" + String.valueOf(totalBankAmt));

        //Income and Expenses by Category Report
        RealmResults<TransactionTable> incomeCategoryTransactionTableResults = realm.where(TransactionTable.class)
                .greaterThanOrEqualTo("dateType", startDate)
                .lessThan("dateType", endDate)
                .findAll().sort("dateType", Sort.ASCENDING);

        double incomeFood = 0, incomeShopping = 0, incomeVehicle = 0;
        double expenseFood = 0, expenseShopping = 0, expenseVehicle = 0;
        double incomeTransportation = 0, incomeLife_And_Entertainment = 0, incomeHousing = 0;
        double expenseTransportation = 0, expenseLife_And_Entertainment = 0, expenseHousing = 0;
        double incomeCommunication = 0, incomeFinancial_Expenses = 0, incomeIncome = 0;
        double expenseCommunication = 0, expenseFinancial_Expenses = 0, expenseIncome = 0;
        double incomeInvestments = 0, incomeOthers = 0;
        double expenseInvestments = 0, expenseOthers = 0;

        for (TransactionTable transactionTableListObj : incomeCategoryTransactionTableResults) {
            if (transactionTableListObj.getTransactionType().equals("Income")) {
                if (transactionTableListObj.getCategory().equals("Food And Drinks")) {
                    incomeFood = incomeFood + transactionTableListObj.getAmount();
                } else if (transactionTableListObj.getCategory().equals("Shopping")) {
                    incomeShopping = incomeShopping + transactionTableListObj.getAmount();
                } else if (transactionTableListObj.getCategory().equals("Vehicle")) {
                    incomeVehicle = incomeVehicle + transactionTableListObj.getAmount();
                } else if (transactionTableListObj.getCategory().equals("Transportation")) {
                    incomeTransportation = incomeTransportation + transactionTableListObj.getAmount();
                } else if (transactionTableListObj.getCategory().equals("Life And Entertainment")) {
                    incomeLife_And_Entertainment = incomeLife_And_Entertainment + transactionTableListObj.getAmount();
                } else if (transactionTableListObj.getCategory().equals("Housing")) {
                    incomeHousing = incomeHousing + transactionTableListObj.getAmount();
                } else if (transactionTableListObj.getCategory().equals("Communication")) {
                    incomeCommunication = incomeCommunication + transactionTableListObj.getAmount();
                } else if (transactionTableListObj.getCategory().equals("Financial Expenses")) {
                    incomeFinancial_Expenses = incomeFinancial_Expenses + transactionTableListObj.getAmount();
                } else if (transactionTableListObj.getCategory().equals("Income")) {
                    incomeIncome = incomeIncome + transactionTableListObj.getAmount();
                } else if (transactionTableListObj.getCategory().equals("Investments")) {
                    incomeInvestments = incomeInvestments + transactionTableListObj.getAmount();
                } else if (transactionTableListObj.getCategory().equals("Others")) {
                    incomeOthers = incomeOthers + transactionTableListObj.getAmount();
                }
            } else if (transactionTableListObj.getTransactionType().equals("Expense")) {
                if (transactionTableListObj.getCategory().equals("Food And Drinks")) {
                    expenseFood = expenseFood + transactionTableListObj.getAmount();
                } else if (transactionTableListObj.getCategory().equals("Shopping")) {
                    expenseShopping = expenseShopping + transactionTableListObj.getAmount();
                } else if (transactionTableListObj.getCategory().equals("Vehicle")) {
                    expenseVehicle = expenseVehicle + transactionTableListObj.getAmount();
                } else if (transactionTableListObj.getCategory().equals("Transportation")) {
                    expenseTransportation = expenseTransportation + transactionTableListObj.getAmount();
                } else if (transactionTableListObj.getCategory().equals("Life_And_Entertainment")) {
                    expenseLife_And_Entertainment = expenseLife_And_Entertainment + transactionTableListObj.getAmount();
                } else if (transactionTableListObj.getCategory().equals("Housing")) {
                    expenseHousing = expenseHousing + transactionTableListObj.getAmount();
                } else if (transactionTableListObj.getCategory().equals("Communication")) {
                    expenseCommunication = expenseCommunication + transactionTableListObj.getAmount();
                } else if (transactionTableListObj.getCategory().equals("Financial_Expenses")) {
                    expenseFinancial_Expenses = expenseFinancial_Expenses + transactionTableListObj.getAmount();
                } else if (transactionTableListObj.getCategory().equals("Income")) {
                    expenseIncome = expenseIncome + transactionTableListObj.getAmount();
                } else if (transactionTableListObj.getCategory().equals("Investments")) {
                    expenseInvestments = expenseInvestments + transactionTableListObj.getAmount();
                } else if (transactionTableListObj.getCategory().equals("Others")) {
                    expenseOthers = expenseOthers + transactionTableListObj.getAmount();
                }
            }
        }

        //Income
        foodIncomeTV.setText("₹" + String.valueOf(incomeFood));
        vehicleIncomeTV.setText("₹" + String.valueOf(incomeVehicle));
        shoppingIncomeTv.setText("₹" + String.valueOf(incomeShopping));
        transporationIncomeTV.setText("₹" + String.valueOf(incomeTransportation));
        lifeAndEntertainmentIncomeTV.setText("₹" + String.valueOf(incomeLife_And_Entertainment));
        housingIncomeTv.setText("₹" + String.valueOf(incomeHousing));
        communicationIncomeTV.setText("₹" + String.valueOf(incomeCommunication));
        financialExpensesIncomeTV.setText("₹" + String.valueOf(incomeFinancial_Expenses));
        categoryIncomeIncomeTV.setText("₹" + String.valueOf(incomeIncome));
        investmentsIncomeTV.setText("₹" + String.valueOf(incomeInvestments));
        othersIncomeTV.setText("₹" + String.valueOf(incomeOthers));
        //Expense
        foodExpenseTV.setText("- ₹" + String.valueOf(expenseFood));
        vehicleExpenseTV.setText("- ₹" + String.valueOf(expenseVehicle));
        shoppingExpenseTv.setText("- ₹" + String.valueOf(expenseShopping));
        transporationExpenseTV.setText("- ₹" + String.valueOf(expenseTransportation));
        lifeAndEntertainmentExpenseTV.setText("- ₹" + String.valueOf(expenseLife_And_Entertainment));
        housingExpenseTv.setText("- ₹" + String.valueOf(expenseHousing));
        communicationExpenseTV.setText("- ₹" + String.valueOf(expenseCommunication));
        financialExpensesExpenseTV.setText("- ₹" + String.valueOf(expenseFinancial_Expenses));
        categoryIncomeExpenseTV.setText("- ₹" + String.valueOf(expenseIncome));
        investmentsExpenseTV.setText("- ₹" + String.valueOf(expenseInvestments));
        othersExpenseTV.setText("- ₹" + String.valueOf(expenseOthers));
    }

    public Pair<Date, Date> getDateRange() {
        Date begining, end;

        {
            Calendar calendar = getCalendarForNow();
            calendar.set(Calendar.DAY_OF_MONTH,
                    calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
            setTimeToBeginningOfDay(calendar);
            begining = calendar.getTime();
        }

        {
            Calendar calendar = getCalendarForNow();
            calendar.set(Calendar.DAY_OF_MONTH,
                    calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            setTimeToEndofDay(calendar);
            end = calendar.getTime();
        }

        return Pair.create(begining, end);
    }

    private static Calendar getCalendarForNow() {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(new Date());
        return calendar;
    }

    private static void setTimeToBeginningOfDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    private static void setTimeToEndofDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
    }

    private void showReportDetails() {
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            repoterName.setText(acct.getDisplayName() + "!");
        } else {
            repoterName.setText("Welcome!");
        }
    }

    // Share Screen Methods
    private void getPermission()
    {
        if (ContextCompat.checkSelfPermission(ReportActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //Permission was denied
            //Request for permission
            ActivityCompat.requestPermissions(ReportActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    501);
        }
        else if (ContextCompat.checkSelfPermission(ReportActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //Permission was denied
            //Request for permission
            ActivityCompat.requestPermissions(ReportActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    502);
        }
        else
        {
            Bitmap bitmap = takeScreenshot();
            saveBitmap(bitmap);
            shareIt();
        }
    }

    public Bitmap takeScreenshot() {
        View rootView = findViewById(android.R.id.content).getRootView();
        rootView.setDrawingCacheEnabled(true);
        return rootView.getDrawingCache();
    }

    public void saveBitmap(Bitmap bitmap) {
        imagePath = new File(Environment.getExternalStorageDirectory() + "/screenshot.png");
        FileOutputStream fos;
        if (ContextCompat.checkSelfPermission(ReportActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //Permission was denied
            //Request for permission
            ActivityCompat.requestPermissions(ReportActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    501);
        }

        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }
    }

    private void shareIt() {
        Uri uri = FileProvider.getUriForFile(
                ReportActivity.this,
                "com.example.expensesspark.example.provider", //(use your app signature + ".provider" )
                imagePath);
        //Uri uri = Uri.fromFile(imagePath);
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("image/*");
        String shareBody = "My income & expenses screenshot";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Expenses Spark");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);

        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }
}