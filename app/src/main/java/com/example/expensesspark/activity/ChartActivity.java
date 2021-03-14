package com.example.expensesspark.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.expensesspark.R;
import com.example.expensesspark.model.AccountTable;
import com.example.expensesspark.model.DetailsModel;
import com.example.expensesspark.model.TransactionTable;
import com.example.expensesspark.realm.TransactionTableHelper;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.internal.util.Pair;


public class ChartActivity extends AppCompatActivity {
    PieChart pieChart;
    PieData pieData;
    PieDataSet pieDataSet;
    ArrayList pieEntries;
    ArrayList PieEntryLabels;
    Realm realm;
    TabLayout tabLayout;
    File imagePath;

    TextView allChartTV, allChartIncomeColor, allChartIncomeTV, allChartExpenseColor, allChartExpenseTV;
    TextView incomeChartTV, incomeChartFodanddrinkColr, incomeChartFodanddrinkTV, incomeChartShoppingColr,
            incomeChartShoppingTV, incomeChartTransportionColr, incomeChartTransportionTV;

    ScrollView allegendScroll, incomeLegendScroll, expenseLegendScroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        realm = Realm.getDefaultInstance();
        pieChart = findViewById(R.id.piechart);

        allegendScroll = findViewById(R.id.allegendScroll);
        // allChartTV = findViewById(R.id.allChartTV); allChartIncomeColor = findViewById(R.id.allChartIncomeColor);
        //  allChartIncomeTV = findViewById(R.id.allChartIncomeTV);allChartExpenseColor = findViewById(R.id.allChartExpenseColor);
        //  allChartExpenseTV = findViewById(R.id.allChartExpenseTV);
        incomeLegendScroll = findViewById(R.id.incomeLegendScroll);
        //incomeChartTV = findViewById(R.id.incomeChartTV); incomeChartFodanddrinkColr = findViewById(R.id.incomeChartFodanddrinkColr);
        // incomeChartFodanddrinkTV = findViewById(R.id.incomeChartFodanddrinkTV);incomeChartShoppingColr = findViewById(R.id.incomeChartShoppingColr);
        // incomeChartShoppingTV = findViewById(R.id.incomeChartShoppingTV);incomeChartTransportionColr = findViewById(R.id.incomeChartTransporationColr);
        // incomeChartTransportionTV = findViewById(R.id.incomeChartTransporationTV);
        expenseLegendScroll = findViewById(R.id.expenseLegendScroll);

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

        FloatingActionButton share = (FloatingActionButton) findViewById(R.id.shareBtn);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPermission();
            }
        });
    }

    // Load Chart Methods
    private void getAllEntries() {

        allegendScroll.setVisibility(View.VISIBLE);
        incomeLegendScroll.setVisibility(View.INVISIBLE);
        expenseLegendScroll.setVisibility(View.INVISIBLE);

        Date startDate = getDateRange().first;
        Date endDate = getDateRange().second;

        //Income and Expenses by Flow Report
        RealmResults<TransactionTable> transactionTableList = realm.where(TransactionTable.class)
                .greaterThanOrEqualTo("dateType", startDate)
                .lessThan("dateType", endDate)
                .findAll().sort("dateType", Sort.ASCENDING);

        //TransactionTableHelper helper = new TransactionTableHelper(realm);
        //List<TransactionTable> transactionTableList = helper.retrieveTransactionTableItems();

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

        pieChart.invalidate();
        pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry((float) income, 0));
        pieEntries.add(new PieEntry((float) expense, 1));
        // pieEntries.add(new PieEntry((float) transfer, 2));

        pieDataSet = new PieDataSet(pieEntries, "Income, Expense");
        pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieDataSet.setColors(new int[]{R.color.Blue_water, R.color.red_600}, getApplicationContext());
        //pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setSliceSpace(2f);
        pieDataSet.setValueTextColor(R.color.white);
        pieDataSet.setValueTextSize(13f);
        pieDataSet.setSliceSpace(5f);

        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieData.setValueTextColor(Color.BLACK);
        pieDataSet.setValueLinePart1OffsetPercentage(10.f);
        pieDataSet.setValueLinePart1Length(0.43f);
        pieDataSet.setValueLinePart2Length(.1f);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieChart.setEntryLabelColor(Color.BLUE);

        pieChart.setExtraBottomOffset(10f);
        pieChart.setExtraLeftOffset(8f);
        pieChart.setExtraRightOffset(10f);

        pieChart.getLegend().setWordWrapEnabled(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.getDescription().setEnabled(false);

    }

    private void getIncomeEntries() {

        allegendScroll.setVisibility(View.INVISIBLE);
        incomeLegendScroll.setVisibility(View.VISIBLE);
        expenseLegendScroll.setVisibility(View.INVISIBLE);

        Date startDate = getDateRange().first;
        Date endDate = getDateRange().second;

        //Income and Expenses by Flow Report
        RealmResults<TransactionTable> transactionTableList = realm.where(TransactionTable.class)
                .equalTo("transactionType", "Income")
                .greaterThanOrEqualTo("dateType", startDate)
                .lessThan("dateType", endDate)
                .findAll().sort("dateType", Sort.ASCENDING);

        //TransactionTableHelper helper = new TransactionTableHelper(realm);
        //List<TransactionTable> transactionTableList = helper.retrieveTransactionTableItems();

        double foodAndDrinks, shopping, Transporation, vehicle,  LifeAndEntertainment, Housing, Communication, FinancialExpenses, Investments, Income,  Others;
        foodAndDrinks = 0;
        shopping = 0;
        Transporation = 0;
        vehicle = 0;
        LifeAndEntertainment = 0;
        Housing = 0;
        Communication = 0;
        FinancialExpenses = 0;
        Investments = 0;
        Income = 0;
        Others = 0;

        for (TransactionTable transactionTableListObj : transactionTableList) {
            if (transactionTableListObj.getCategory().equals("Food And Drinks")) {
                foodAndDrinks = foodAndDrinks + transactionTableListObj.getAmount();
            } else if (transactionTableListObj.getCategory().equals("Shopping")) {
                shopping = shopping + transactionTableListObj.getAmount();
            } else if (transactionTableListObj.getCategory().equals("Transportation")) {
                Transporation = Transporation + transactionTableListObj.getAmount();
            } else if (transactionTableListObj.getCategory().equals("Vehicle")) {
                vehicle = vehicle + transactionTableListObj.getAmount();
            } else if (transactionTableListObj.getCategory().equals("Life And Entertainment")) {
                LifeAndEntertainment = LifeAndEntertainment + transactionTableListObj.getAmount();
            } else if (transactionTableListObj.getCategory().equals("Housing")) {
                Housing = Housing + transactionTableListObj.getAmount();
            } else if (transactionTableListObj.getCategory().equals("Communication")) {
                Communication = Communication + transactionTableListObj.getAmount();
            } else if (transactionTableListObj.getCategory().equals("Financial Expenses")) {
                FinancialExpenses = FinancialExpenses + transactionTableListObj.getAmount();
            }else if (transactionTableListObj.getCategory().equals("Investments")) {
                Investments = Investments + transactionTableListObj.getAmount();
            } else if (transactionTableListObj.getCategory().equals("Income")) {
                Income = Income + transactionTableListObj.getAmount();
            } else if (transactionTableListObj.getCategory().equals("Others")) {
                Others = Others + transactionTableListObj.getAmount();
            }
        }

        pieChart.invalidate();
        pieEntries = new ArrayList<>();
        int dataInsertPosition = 0;

        if (foodAndDrinks != 0) {
            pieEntries.add(new PieEntry((float) foodAndDrinks, dataInsertPosition));
            dataInsertPosition = dataInsertPosition + 1;
        }

        if (shopping != 0) {
            pieEntries.add(new PieEntry((float) shopping, dataInsertPosition));
            dataInsertPosition = dataInsertPosition + 1;
        }
        if (vehicle != 0) {
            pieEntries.add(new PieEntry((float) vehicle, dataInsertPosition));
            dataInsertPosition = dataInsertPosition + 1;
        }
        if (Transporation != 0) {
            pieEntries.add(new PieEntry((float) Transporation, dataInsertPosition));
            dataInsertPosition = dataInsertPosition + 1;
        }
        if (LifeAndEntertainment != 0) {
            pieEntries.add(new PieEntry((float) LifeAndEntertainment, dataInsertPosition));
            dataInsertPosition = dataInsertPosition + 1;
        }
        if (Housing != 0) {
            pieEntries.add(new PieEntry((float) Housing, dataInsertPosition));
            dataInsertPosition = dataInsertPosition + 1;
        }
        if (Communication != 0) {
            pieEntries.add(new PieEntry((float) Communication, dataInsertPosition));
            dataInsertPosition = dataInsertPosition + 1;
        }
        if (FinancialExpenses != 0) {
            pieEntries.add(new PieEntry((float) FinancialExpenses, dataInsertPosition));
            dataInsertPosition = dataInsertPosition + 1;
        }
        if (Investments != 0) {
            pieEntries.add(new PieEntry((float) Investments, dataInsertPosition));
            dataInsertPosition = dataInsertPosition + 1;
        }
        if (Income != 0) {
            pieEntries.add(new PieEntry((float) Income, dataInsertPosition));
            dataInsertPosition = dataInsertPosition + 1;
        }

        if (Others != 0) {
            pieEntries.add(new PieEntry((float) Others, dataInsertPosition));
        }

        pieDataSet = new PieDataSet(pieEntries, "Food And Drinks, Shopping, Transporation, Vehicle, Life And Entertainment, Housing, Communication, FinancialExpenses, Investments, Income, Others");
        pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
       // pieChart.invalidate();

        pieDataSet.setColors(new int[]{R.color.Yellow, R.color.Light_green, R.color.colorPrimary, R.color.color_one,
                R.color.accent, R.color.Blue_water, R.color.green_500, R.color.purple_500,
                R.color.black, R.color.colorAccent, R.color.red_200}, getApplicationContext());
        //  pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        pieDataSet.setSliceSpace(2f);
        pieDataSet.setValueTextColor(R.color.white);
        pieDataSet.setValueTextSize(13f);
        pieDataSet.setSliceSpace(5f);

        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieData.setValueTextColor(Color.BLACK);
        pieDataSet.setValueLinePart1OffsetPercentage(10.f);
        pieDataSet.setValueLinePart1Length(0.43f);
        pieDataSet.setValueLinePart2Length(.1f);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieChart.setEntryLabelColor(Color.BLUE);

        pieChart.getLegend().setWordWrapEnabled(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.getDescription().setEnabled(false);

    }

    private void getExpensesEntries() {

        allegendScroll.setVisibility(View.INVISIBLE);
        incomeLegendScroll.setVisibility(View.INVISIBLE);
        expenseLegendScroll.setVisibility(View.VISIBLE);

        Date startDate = getDateRange().first;
        Date endDate = getDateRange().second;

        //Income and Expenses by Flow Report
        RealmResults<TransactionTable> transactionTableList = realm.where(TransactionTable.class)
                .equalTo("transactionType", "Expense")
                .greaterThanOrEqualTo("dateType", startDate)
                .lessThan("dateType", endDate)
                .findAll().sort("dateType", Sort.ASCENDING);
        //TransactionTableHelper helper = new TransactionTableHelper(realm);
        // List<TransactionTable> transactionTableList = helper.retrieveTransactionTableItems();

        double foodAndDrinks, shopping, Transporation, vehicle,  LifeAndEntertainment, Housing, Communication, FinancialExpenses, Investments, Income,  Others;
        foodAndDrinks = 0;
        shopping = 0;
        Transporation = 0;
        vehicle = 0;
        LifeAndEntertainment = 0;
        Housing = 0;
        Communication = 0;
        FinancialExpenses = 0;
        Investments = 0;
        Income = 0;
        Others = 0;

        for (TransactionTable transactionTableListObj : transactionTableList) {
            if (transactionTableListObj.getCategory().equals("Food And Drinks")) {
                foodAndDrinks = foodAndDrinks + transactionTableListObj.getAmount();
            } else if (transactionTableListObj.getCategory().equals("Shopping")) {
                shopping = shopping + transactionTableListObj.getAmount();
            } else if (transactionTableListObj.getCategory().equals("Transportation")) {
                Transporation = Transporation + transactionTableListObj.getAmount();
            } else if (transactionTableListObj.getCategory().equals("Vehicle")) {
                vehicle = vehicle + transactionTableListObj.getAmount();
            }  else if (transactionTableListObj.getCategory().equals("Life And Entertainment")) {
                LifeAndEntertainment = LifeAndEntertainment + transactionTableListObj.getAmount();
            } else if (transactionTableListObj.getCategory().equals("Housing")) {
                Housing = Housing + transactionTableListObj.getAmount();
            } else if (transactionTableListObj.getCategory().equals("Communication")) {
                Communication = Communication + transactionTableListObj.getAmount();
            } else if (transactionTableListObj.getCategory().equals("Financial Expenses")) {
                FinancialExpenses = FinancialExpenses + transactionTableListObj.getAmount();
            } else if (transactionTableListObj.getCategory().equals("Investments")) {
                Investments = Investments + transactionTableListObj.getAmount();
            } else if (transactionTableListObj.getCategory().equals("Income")) {
                Income = Income + transactionTableListObj.getAmount();
            }else if (transactionTableListObj.getCategory().equals("Others")) {
                Others = Others + transactionTableListObj.getAmount();
            }
        }

        pieChart.invalidate();
        pieEntries = new ArrayList<>();
        int dataInsertPosition = 0;

        if (foodAndDrinks != 0) {
            pieEntries.add(new PieEntry((float) foodAndDrinks, dataInsertPosition));
            dataInsertPosition = dataInsertPosition + 1;
        }

        if (shopping != 0) {
            pieEntries.add(new PieEntry((float) shopping, dataInsertPosition));
            dataInsertPosition = dataInsertPosition + 1;
        }
        if (Transporation != 0) {
            pieEntries.add(new PieEntry((float) Transporation, dataInsertPosition));
            dataInsertPosition = dataInsertPosition + 1;
        }
        if (vehicle != 0) {
            pieEntries.add(new PieEntry((float) vehicle, dataInsertPosition));
            dataInsertPosition = dataInsertPosition + 1;
        }
        if (LifeAndEntertainment != 0) {
            pieEntries.add(new PieEntry((float) LifeAndEntertainment, dataInsertPosition));
            dataInsertPosition = dataInsertPosition + 1;
        }
        if (Housing != 0) {
            pieEntries.add(new PieEntry((float) Housing, dataInsertPosition));
            dataInsertPosition = dataInsertPosition + 1;
        }
        if (Communication != 0) {
            pieEntries.add(new PieEntry((float) Communication, dataInsertPosition));
            dataInsertPosition = dataInsertPosition + 1;
        }
        if (FinancialExpenses != 0) {
            pieEntries.add(new PieEntry((float) FinancialExpenses, dataInsertPosition));
            dataInsertPosition = dataInsertPosition + 1;
        }
        if (Investments != 0) {
            pieEntries.add(new PieEntry((float) Investments, dataInsertPosition));
            dataInsertPosition = dataInsertPosition + 1;
        }
        if (Income != 0) {
            pieEntries.add(new PieEntry((float) Income, dataInsertPosition));
            dataInsertPosition = dataInsertPosition + 1;
        }
        if (Others != 0) {
            pieEntries.add(new PieEntry((float) Others, dataInsertPosition));
        }


        pieDataSet = new PieDataSet(pieEntries, "Food And Drinks, Shopping, Transporation, Vehicle, LifeAndEntertainment, Housing, Communication, FinancialExpenses, Investments, Income, Others");
        pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);

        pieDataSet.setColors(new int[]{R.color.Yellow, R.color.Light_green, R.color.colorPrimary, R.color.color_one,
                R.color.accent, R.color.Blue_water, R.color.green_500, R.color.purple_500,
                R.color.black, R.color.colorAccent, R.color.red_200}, getApplicationContext());
        // pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        pieDataSet.setSliceSpace(2f);
        pieDataSet.setValueTextColor(R.color.white);
        pieDataSet.setValueTextSize(13f);
        pieDataSet.setSliceSpace(5f);

        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieData.setValueTextColor(Color.BLACK);
        pieDataSet.setValueLinePart1OffsetPercentage(10.f);
        pieDataSet.setValueLinePart1Length(0.43f);
        pieDataSet.setValueLinePart2Length(.1f);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieChart.setEntryLabelColor(Color.BLUE);

        pieChart.setExtraBottomOffset(10f);
        pieChart.setExtraLeftOffset(10f);
        pieChart.setExtraRightOffset(10f);

        pieChart.getLegend().setWordWrapEnabled(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.getDescription().setEnabled(false);


    }

    // Share Screen Methods
    private void getPermission() {
        if (ContextCompat.checkSelfPermission(ChartActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //Permission was denied
            //Request for permission
            ActivityCompat.requestPermissions(ChartActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    501);
        } else if (ContextCompat.checkSelfPermission(ChartActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //Permission was denied
            //Request for permission
            ActivityCompat.requestPermissions(ChartActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    502);
        } else {
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
        if (ContextCompat.checkSelfPermission(ChartActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //Permission was denied
            //Request for permission
            ActivityCompat.requestPermissions(ChartActivity.this,
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
                ChartActivity.this,
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

    // Get Start and End Date Methods
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