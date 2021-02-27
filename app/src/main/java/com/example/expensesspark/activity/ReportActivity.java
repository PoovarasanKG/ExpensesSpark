package com.example.expensesspark.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.expensesspark.R;
import com.example.expensesspark.model.AccountTable;
import com.example.expensesspark.model.TransactionTable;
import com.example.expensesspark.realm.TransactionTableHelper;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

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
             investmentsTV, investmentsIncomeTV, investmentsExpenseTV, categoryIncomeTV, categoryIncomeIncomeTV, categoryIncomeExpenseTV, othersTV, othersIncomeTV, othersExpenseTV ;
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
    }

    private void showIncomeAndExpenseFlowDetails() {
        //RealmQuery<TransactionTable> incomeTransactionTableResults = realm.where(TransactionTable.class)
          //      .equalTo("transactionType", "Income");

        String currentMonth = new SimpleDateFormat("MMMM, yyyy", Locale.getDefault()).format(new Date());
        flowReportMonthTV.setText("For month of " + currentMonth);

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

        RealmResults<TransactionTable> creditTransactionTableResults = realm.where(TransactionTable.class)
                .equalTo("transactionType", "Credit")
                .greaterThanOrEqualTo("dateType", startDate)
                .lessThan("dateType", endDate)
                .findAll().sort("dateType", Sort.ASCENDING);
        double totalCreditAmt = creditTransactionTableResults.sum("amount").doubleValue();
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
                .equalTo("transactionType", "Income")
                .greaterThanOrEqualTo("dateType", startDate)
                .lessThan("dateType", endDate)
                .findAll().sort("dateType", Sort.ASCENDING);

        double incomeFood = 0, incomeShopping = 0, incomeVehicle = 0;
        double expenseFood = 0, expenseShopping = 0, expenseVehicle = 0;

        for (TransactionTable transactionTableListObj : incomeCategoryTransactionTableResults)
        {
            if (transactionTableListObj.getTransactionType().equals("Income"))
            {
                if (transactionTableListObj.getCategory().equals("Food And Drinks"))
                {
                    incomeFood = incomeFood + transactionTableListObj.getAmount();
                } else if (transactionTableListObj.getCategory().equals("Shopping")) {
                    incomeShopping = incomeShopping + transactionTableListObj.getAmount();
                } else if (transactionTableListObj.getCategory().equals("Vehicle")) {
                    incomeVehicle = incomeVehicle + transactionTableListObj.getAmount();
                } else if (transactionTableListObj.getCategory().equals("Transportation")) {
                    //transfer = transfer + transactionTableListObj.getAmount();
                } else if (transactionTableListObj.getCategory().equals("Life And Entertainment")) {
                   // Life = Life + transactionTableListObj.getAmount();
                } else if (transactionTableListObj.getCategory().equals("Housing")) {
                   // Housing = Housing + transactionTableListObj.getAmount();
                } else if (transactionTableListObj.getCategory().equals("Communication")) {
                   // Communication = Communication + transactionTableListObj.getAmount();
                } else if (transactionTableListObj.getCategory().equals("Financial Expenses")) {
                   // FinancialExpenses = FinancialExpenses + transactionTableListObj.getAmount();
                } else if (transactionTableListObj.getCategory().equals("Income")) {
                   // Income = Income + transactionTableListObj.getAmount();
                } else if (transactionTableListObj.getCategory().equals("Investments")) {
                   // Investments = Investments + transactionTableListObj.getAmount();
                } else if (transactionTableListObj.getCategory().equals("Others")) {
                   // Others = Others + transactionTableListObj.getAmount();
                }
            }
            else if (transactionTableListObj.getTransactionType().equals("Expense"))
            {
                if (transactionTableListObj.getCategory().equals("Food And Drinks"))
                {
                    expenseFood = expenseFood + transactionTableListObj.getAmount();
                } else if (transactionTableListObj.getCategory().equals("Shopping")) {
                    expenseShopping = expenseShopping + transactionTableListObj.getAmount();
                } else if (transactionTableListObj.getCategory().equals("Vehicle")) {
                    expenseVehicle = expenseVehicle + transactionTableListObj.getAmount();
                }
            }
        }

        //Income
        foodIncomeTV.setText("₹" + String.valueOf(incomeFood));
        vehicleIncomeTV.setText("₹" + String.valueOf(incomeVehicle));
        shoppingIncomeTv.setText("₹" + String.valueOf(incomeShopping));


        //Expense
        foodExpenseTV.setText("- ₹" + String.valueOf(expenseFood));
        vehicleExpenseTV.setText("- ₹" + String.valueOf(expenseVehicle));
        shoppingExpenseTv.setText("- ₹" + String.valueOf(expenseShopping));
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
}