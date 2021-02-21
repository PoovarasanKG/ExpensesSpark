package com.example.expensesspark.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.expensesspark.R;
import com.example.expensesspark.model.AccountTable;
import com.example.expensesspark.model.TransactionTable;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class AddTransaction extends AppCompatActivity implements View.OnClickListener {

    TextInputLayout selectDate, selectTime, amountTxt, descriptionTxt, locationTxt;
    private int mYear, mMonth, mDay, mHour, mMinute;
    Button saveBtn;
    Spinner transactionTypeSpinner, accountSpinner, categorySpinner, paymentModeSpinner;
    Realm realm;
    String activity, activityType = "";
    long primaryKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        realm = Realm.getDefaultInstance();

        selectDate = (TextInputLayout) findViewById(R.id.dateTxt);
        selectTime = (TextInputLayout) findViewById(R.id.timeTxt);
        amountTxt = (TextInputLayout) findViewById(R.id.amountTxt);
        descriptionTxt = (TextInputLayout) findViewById(R.id.descriptionTxt);
        locationTxt = (TextInputLayout) findViewById(R.id.locationTxt);
        transactionTypeSpinner = (Spinner) findViewById(R.id.transactionTypeSpinner);
        accountSpinner = (Spinner) findViewById(R.id.accountTypeSpinner);
        categorySpinner = (Spinner) findViewById(R.id.categorySpinner);
        paymentModeSpinner = (Spinner) findViewById(R.id.paymentModeSpinner);
        saveBtn = (Button) findViewById(R.id.submitBtn);

        selectTime.getEditText().setOnClickListener(this);
        selectDate.getEditText().setOnClickListener(this);

        activity = getIntent().getStringExtra("Activity");
        primaryKey = getIntent().getLongExtra("transactionId", 0);
        activityType = getIntent().getStringExtra("ActivityType");

        if (activity.equals("Edit"))
        {
            RealmQuery<TransactionTable> realmQuery = realm.where(TransactionTable.class)
                    .equalTo("transactionId", primaryKey);
            TransactionTable transactionTablelObj = realmQuery.findAll().first();

            if (transactionTablelObj.getTransactionType().equalsIgnoreCase("Income"))
            {
                transactionTypeSpinner.setSelection(1);
            }
            else if (transactionTablelObj.getTransactionType().equalsIgnoreCase("Expense"))
            {
                transactionTypeSpinner.setSelection(2);
            }
            else if (transactionTablelObj.getTransactionType().equalsIgnoreCase("Transfer"))
            {
                transactionTypeSpinner.setSelection(3);
            }

            amountTxt.getEditText().setText(String.valueOf(transactionTablelObj.getAmount()));
            selectDate.getEditText().setText(transactionTablelObj.getDate());
            selectTime.getEditText().setText(transactionTablelObj.getTime());
            descriptionTxt.getEditText().setText(transactionTablelObj.getDescription());
        }
        else
        {
            String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            String currentTime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());

            selectDate.getEditText().setText(currentDate);
            selectTime.getEditText().setText(currentTime);
        }

        saveBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (validationSuccess()) {

                    if (activity.equals("Edit"))
                    {
                        Toast.makeText(getApplicationContext(), "Transaction Updated Successfully", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Transaction Added Successfully", Toast.LENGTH_LONG).show();
                    }

                    final TransactionTable transactionTableModelObj = new TransactionTable();

                    long nextId;

                    if (activity.equalsIgnoreCase("Edit"))
                    {
                        nextId = primaryKey;
                    }
                    else {
                        Number current_id = realm.where(TransactionTable.class).max("transactionId");
                        if (current_id == null) {
                            nextId = 1;
                        } else {
                            nextId = current_id.intValue() + 1;
                        }
                    }

                    transactionTableModelObj.setTransactionId(nextId);
                    transactionTableModelObj.setTransactionType(transactionTypeSpinner.getSelectedItem().toString());
                    transactionTableModelObj.setAmount(Double.parseDouble(amountTxt.getEditText().getText().toString()));
                    transactionTableModelObj.setTransactionAccount(accountSpinner.getSelectedItem().toString());
                    transactionTableModelObj.setCategory(categorySpinner.getSelectedItem().toString());
                    transactionTableModelObj.setDate(selectDate.getEditText().getText().toString());
                    transactionTableModelObj.setTime(selectTime.getEditText().getText().toString());
                    transactionTableModelObj.setDescription(descriptionTxt.getEditText().getText().toString());
                    transactionTableModelObj.setPaymentMode(paymentModeSpinner.getSelectedItem().toString());
                    transactionTableModelObj.setLocation(locationTxt.getEditText().getText().toString());

                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.insertOrUpdate(transactionTableModelObj);
                                    //copyToRealm(transactionTableModelObj);
                            //showData();
                            showTransactionsListActivity();
                        }
                    });

                } else {

                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_patient_home_screen, menu);
        menu.add(0, 1, 1, menuIconWithText(ContextCompat.getDrawable(getApplicationContext(), R.drawable.home), getResources().getString(R.string.Dashboard)));
        menu.add(0, 2, 2, menuIconWithText(ContextCompat.getDrawable(getApplicationContext(), R.drawable.add), getResources().getString(R.string.AddNewTransaction)));
        menu.add(0, 3, 3, menuIconWithText(ContextCompat.getDrawable(getApplicationContext(), R.drawable.details), getResources().getString(R.string.ViewTransaction)));
        menu.add(0, 4, 4, menuIconWithText(ContextCompat.getDrawable(getApplicationContext(), R.drawable.analytics), getResources().getString(R.string.Reports)));
        menu.add(0, 4, 4, menuIconWithText(ContextCompat.getDrawable(getApplicationContext(), R.drawable.settings), getResources().getString(R.string.AccountSettings)));
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case 1:
                Toast.makeText(this, "Profile is Clicked", Toast.LENGTH_SHORT).show();
                return true;
            case 2:
                Toast.makeText(this, "Add New User is Clicked", Toast.LENGTH_SHORT).show();
                return true;
            case 3:
                Toast.makeText(this, "Switch Profile is Clicked", Toast.LENGTH_SHORT).show();
                return true;
            case 4:
                Toast.makeText(this, "Sign Out is Clicked", Toast.LENGTH_SHORT).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private CharSequence menuIconWithText(Drawable r, String title) {
        r.setBounds(0, 0, r.getIntrinsicWidth(), r.getIntrinsicHeight());
        SpannableString sb = new SpannableString("    " + title);
        ImageSpan imageSpan = new ImageSpan(r, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return sb;
    }

    @Override
    public void onClick(View view) {

        if (view == selectDate.getEditText()) {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            selectDate.getEditText().setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }

        if (view == selectTime.getEditText()) {
            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @SuppressLint("DefaultLocale")
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            boolean isPM = (hourOfDay >= 12);
                            Objects.requireNonNull(selectTime.getEditText()).setText(String.format("%02d:%02d %s", (hourOfDay == 12 || hourOfDay == 0) ? 12 : hourOfDay % 12, minute, isPM ? "PM" : "AM"));
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }

    private Boolean validationSuccess() {

        if (transactionTypeSpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(getApplicationContext(), "Please select transaction type", Toast.LENGTH_SHORT).show();
            return false;
        }
        if ((amountTxt.getEditText().getText().toString().equalsIgnoreCase("")) && (amountTxt.getEditText().getText().toString().equalsIgnoreCase("0"))) {
            Toast.makeText(getApplicationContext(), "Please enter the transaction amount", Toast.LENGTH_LONG).show();
            return false;
        }
        if (accountSpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(getApplicationContext(), "Please select account", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (categorySpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(getApplicationContext(), "Please select category type", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (selectDate.getEditText().getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(), "Please select date", Toast.LENGTH_LONG).show();
            return false;
        }
        if (selectTime.getEditText().getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(), "Please select time", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (descriptionTxt.getEditText().getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(), "Please enter description", Toast.LENGTH_LONG).show();
            return false;
        }
        if (paymentModeSpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(getApplicationContext(), "Please select payment mode", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (locationTxt.getEditText().getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(), "Please enter location", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void showData() {
        List<TransactionTable> transactionTableModels = realm.where(TransactionTable.class).findAll();

        for (int i = 0; i < transactionTableModels.size(); i++) {
            Toast.makeText(this, "You are added:" + transactionTableModels.get(i).getTransactionType(), Toast.LENGTH_SHORT);
        }
    }

    private void showTransactionsListActivity() {

        if (activityType.equalsIgnoreCase("Income") || transactionTypeSpinner.getSelectedItem().toString().equalsIgnoreCase("Income"))
        {
            Intent transactionsListActivity = new Intent(this, TransactionsListActivity.class);
            transactionsListActivity.putExtra("ActivityName", "Income");
            startActivity(transactionsListActivity);
        }
        else if (activityType.equalsIgnoreCase("Expense") || transactionTypeSpinner.getSelectedItem().toString().equalsIgnoreCase("Expense"))
        {
            Intent transactionsListActivity = new Intent(this, TransactionsListActivity.class);
            transactionsListActivity.putExtra("ActivityName", "Expense");
            startActivity(transactionsListActivity);
        }
        else
        {
            Intent dashboard = new Intent(this, Dashboard.class);
            startActivity(dashboard);
        }
    }
}


