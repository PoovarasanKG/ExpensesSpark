package com.example.expensesspark.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        realm = Realm.getDefaultInstance();
        pieChart = findViewById(R.id.piechart);

        getAllEntries();
    }

    private void getAllEntries()
    {
        TransactionTableHelper helper=new TransactionTableHelper(realm);
        List<TransactionTable> transactionTableList = helper.retrieveTransactionTableItems();

        double income, expense, transfer;
        income = 0;
        expense = 0;
        transfer = 0;

        for(TransactionTable transactionTableListObj:transactionTableList)
        {
            if (transactionTableListObj.getTransactionType().equals("Income"))
            {
                income = income + transactionTableListObj.getAmount();
            }
            else if (transactionTableListObj.getTransactionType().equals("Expense"))
            {
                expense = expense + transactionTableListObj.getAmount();
            }
            else if (transactionTableListObj.getTransactionType().equals("Transfer"))
            {
                transfer = transfer + transactionTableListObj.getAmount();
            }
        }

        pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry((float)income, 0));
        pieEntries.add(new PieEntry((float)expense, 1));
        pieEntries.add(new PieEntry((float)transfer, 2));

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