package com.example.expensesspark.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.expensesspark.R;
import com.example.expensesspark.model.AccountTable;
import com.example.expensesspark.model.DetailsModel;
import com.example.expensesspark.model.TransactionTable;
import com.example.expensesspark.realm.TransactionTableHelper;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;


public class ChartActivity extends AppCompatActivity {
    PieChart pieChart;
    PieData pieData;
    PieDataSet pieDataSet;
    ArrayList pieEntries;
    ArrayList PieEntryLabels;
    Realm realm;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        realm = Realm.getDefaultInstance();
        pieChart = findViewById(R.id.piechart);

        getAllEntries();

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    getAllEntries();
                } else if (tab.getPosition() == 1) {
                    getIncomeEntries();
                } else if (tab.getPosition() == 2) {
                    getExpensesEntries();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void getAllEntries() {
        TransactionTableHelper helper = new TransactionTableHelper(realm);
        List<TransactionTable> transactionTableList = helper.retrieveTransactionTableItems();

        double income, expense, transfer;
        income = 0;
        expense = 0;
        transfer = 0;

        for (TransactionTable transactionTableListObj : transactionTableList) {
            if (transactionTableListObj.getTransactionType().equals("Income")) {
                income = income + transactionTableListObj.getAmount();
            } else if (transactionTableListObj.getTransactionType().equals("Expense")) {
                expense = expense + transactionTableListObj.getAmount();
            } else if (transactionTableListObj.getTransactionType().equals("Transfer")) {
                transfer = transfer + transactionTableListObj.getAmount();
            }
        }

        pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry((float) income, 0));
        pieEntries.add(new PieEntry((float) expense, 1));
        pieEntries.add(new PieEntry((float) transfer, 2));

        pieDataSet = new PieDataSet(pieEntries, "Income, Expense, Transfer");
        pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setSliceSpace(2f);
        pieDataSet.setValueTextColor(R.color.white);
        pieDataSet.setValueTextSize(10f);
        pieDataSet.setSliceSpace(5f);
    }

    private void getIncomeEntries() {
        TransactionTableHelper helper = new TransactionTableHelper(realm);
        List<TransactionTable> transactionTableList = helper.retrieveTransactionTableItems();

        double food, shopping, vehicle, transfer, Life, Housing, Communication, FinancialExpenses, Income, Investments, Others;
        food = 0;
        shopping = 0;
        vehicle = 0;
        transfer = 0;
        Life = 0;
        Housing = 0;
        Communication = 0;
        FinancialExpenses = 0;
        Income = 0;
        Investments = 0;
        Others = 0;

        for (TransactionTable transactionTableListObj : transactionTableList) {
            if (transactionTableListObj.getTransactionType().equals("Income"))
            {
                if (transactionTableListObj.getCategory().equals("Food And Drinks"))
                {
                    food = food + transactionTableListObj.getAmount();
                } else if (transactionTableListObj.getCategory().equals("Shopping")) {
                    shopping = shopping + transactionTableListObj.getAmount();
                } else if (transactionTableListObj.getCategory().equals("Vehicle")) {
                    vehicle = vehicle + transactionTableListObj.getAmount();
                } else if (transactionTableListObj.getCategory().equals("Transportation")) {
                    transfer = transfer + transactionTableListObj.getAmount();
                } else if (transactionTableListObj.getCategory().equals("Life And Entertainment")) {
                    Life = Life + transactionTableListObj.getAmount();
                } else if (transactionTableListObj.getCategory().equals("Housing")) {
                    Housing = Housing + transactionTableListObj.getAmount();
                } else if (transactionTableListObj.getCategory().equals("Communication")) {
                    Communication = Communication + transactionTableListObj.getAmount();
                } else if (transactionTableListObj.getCategory().equals("Financial Expenses")) {
                    FinancialExpenses = FinancialExpenses + transactionTableListObj.getAmount();
                } else if (transactionTableListObj.getCategory().equals("Income")) {
                    Income = Income + transactionTableListObj.getAmount();
                } else if (transactionTableListObj.getCategory().equals("Investments")) {
                    Investments = Investments + transactionTableListObj.getAmount();
                } else if (transactionTableListObj.getCategory().equals("Others")) {
                    Others = Others + transactionTableListObj.getAmount();
                }
            }
        }

        pieEntries = new ArrayList<>();

        int dataInsertPosition = 0;

        if (food != 0)
        {
            pieEntries.add(new PieEntry((float) food, dataInsertPosition));
            dataInsertPosition = dataInsertPosition + 1;
        }

        if (shopping != 0)
        {
            pieEntries.add(new PieEntry((float) shopping, dataInsertPosition));
            dataInsertPosition = dataInsertPosition + 1;
        }
        if (vehicle != 0)
        {
            pieEntries.add(new PieEntry((float) vehicle, dataInsertPosition));
            dataInsertPosition = dataInsertPosition + 1;
        }
        if (transfer != 0)
        {
            pieEntries.add(new PieEntry((float) transfer, dataInsertPosition));
            dataInsertPosition = dataInsertPosition + 1;
        }
        if (Life != 0)
        {
            pieEntries.add(new PieEntry((float) Life, dataInsertPosition));
            dataInsertPosition = dataInsertPosition + 1;
        }
        if (Housing != 0)
        {
            pieEntries.add(new PieEntry((float) Housing, dataInsertPosition));
            dataInsertPosition = dataInsertPosition + 1;
        }
        if (Communication != 0)
        {
            pieEntries.add(new PieEntry((float) Communication, dataInsertPosition));
            dataInsertPosition = dataInsertPosition + 1;
        }
        if (FinancialExpenses != 0)
        {
            pieEntries.add(new PieEntry((float) FinancialExpenses, dataInsertPosition));
            dataInsertPosition = dataInsertPosition + 1;
        }
        if (Income != 0)
        {
            pieEntries.add(new PieEntry((float) Income, dataInsertPosition));
            dataInsertPosition = dataInsertPosition + 1;
        }
        if (Investments != 0)
        {
            pieEntries.add(new PieEntry((float) Investments, dataInsertPosition));
            dataInsertPosition = dataInsertPosition + 1;
        }
        if (Others != 0)
        {
            pieEntries.add(new PieEntry((float) Others, dataInsertPosition));
        }

        pieDataSet = new PieDataSet(pieEntries, "Food And Drinks, Shopping, Vehicle, transfer, Life, Housing, Communication, FinancialExpenses, Income, Investments, Others");
        pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        pieDataSet.setSliceSpace(2f);
        pieDataSet.setValueTextColor(R.color.white);
        pieDataSet.setValueTextSize(10f);
        pieDataSet.setSliceSpace(5f);
    }

    private void getExpensesEntries() {
        TransactionTableHelper helper = new TransactionTableHelper(realm);
        List<TransactionTable> transactionTableList = helper.retrieveTransactionTableItems();

        double income, expense, transfer;
        income = 0;
        expense = 0;
        transfer = 0;

        for (TransactionTable transactionTableListObj : transactionTableList) {
            if (transactionTableListObj.getTransactionType().equals("Income")) {
                income = income + transactionTableListObj.getAmount();
            } else if (transactionTableListObj.getTransactionType().equals("Expense")) {
                expense = expense + transactionTableListObj.getAmount();
            } else if (transactionTableListObj.getTransactionType().equals("Transfer")) {
                transfer = transfer + transactionTableListObj.getAmount();
            }
        }

        pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry((float) income, 0));
        pieEntries.add(new PieEntry((float) expense, 1));
        pieEntries.add(new PieEntry((float) transfer, 2));

        pieDataSet = new PieDataSet(pieEntries, "Income, Expense, Transfer");
        pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setSliceSpace(2f);
        pieDataSet.setValueTextColor(R.color.white);
        pieDataSet.setValueTextSize(10f);
        pieDataSet.setSliceSpace(5f);
    }

}