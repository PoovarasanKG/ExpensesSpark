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

public class AddTransaction extends AppCompatActivity implements View.OnClickListener {

    TextInputLayout selectDate, selectTime, amountTxt, descriptionTxt, locationTxt;
    private int mYear, mMonth, mDay, mHour, mMinute;
    Button saveBtn;
    Spinner transactionTypeSpinner, accountSpinner, categorySpinner, paymentModeSpinner;
    Realm realm;


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

        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());

        selectDate.getEditText().setText(currentDate);
        selectTime.getEditText().setText(currentTime);

        selectTime.getEditText().setOnClickListener(this);
        selectDate.getEditText().setOnClickListener(this);

        saveBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (validationSuccess()) {
                    Toast.makeText(getApplicationContext(), "New Account Added", Toast.LENGTH_LONG).show();

                    final TransactionTable transactionTableModelObj = new TransactionTable();

                    Number current_id = realm.where(TransactionTable.class).max("transactionId");
                    long nextId;

                    if (current_id == null) {
                        nextId = 1;
                    } else {
                        nextId = current_id.intValue() + 1;
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
                            realm.copyToRealm(transactionTableModelObj);
                            //showData();
                            showTransactionsListActivity();
                        }
                    });

                }
                else {

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

        if (accountSpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(getApplicationContext(), "Please select account", Toast.LENGTH_SHORT).show();
            return false;
        }

        if ((amountTxt.getEditText().getText().toString().equalsIgnoreCase("")) && (amountTxt.getEditText().getText().toString().equalsIgnoreCase("0"))) {
            Toast.makeText(getApplicationContext(), "Please enter the transaction amount", Toast.LENGTH_LONG).show();
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

        return true;
    }

    private void showData()
    {
        List<TransactionTable> transactionTableModels =realm.where(TransactionTable.class).findAll();

        for(int i=0;i<transactionTableModels.size();i++)
        {
            Toast.makeText(this, "You are added:" + transactionTableModels.get(i).getTransactionType(), Toast.LENGTH_SHORT);
        }
    }

    private void showTransactionsListActivity() {
        Intent mainActivity = new Intent(this, TransactionsListActivity.class);
        startActivity(mainActivity);
    }

}


