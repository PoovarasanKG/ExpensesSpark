package com.example.expensesspark.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.expensesspark.R;
import com.example.expensesspark.model.AccountTable;
import com.example.expensesspark.model.TransactionTable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmQuery;

public class NewAccountActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button submitButton;
    TextInputLayout accountName;
    TextInputLayout bankAccountNumber, currntBalance;
    Spinner currency_spinner;
    Spinner accountTypeSpinner;
    Realm realm;
    long primarykey;
    String activity;
    //  Date selectedDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);

        realm = Realm.getDefaultInstance();

        getSupportActionBar().setTitle("New Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        submitButton = findViewById(R.id.submitButton);
        accountName = findViewById(R.id.accountname);
        bankAccountNumber = findViewById(R.id.accountnumber);
        currency_spinner = findViewById(R.id.CurrencyTypeSpinner);
        accountTypeSpinner = findViewById(R.id.AccountTypeSpinner);
        currntBalance = findViewById(R.id.currntBalance);

        activity = getIntent().getStringExtra("Activity");
        primarykey = getIntent().getLongExtra("accountId", 0);

        if ("Edit".equalsIgnoreCase(activity)) {
            currntBalance.getEditText().setEnabled(false);
            accountTypeSpinner.setEnabled(false);

            RealmQuery<AccountTable> realmQuery = realm.where(AccountTable.class)
                    .equalTo("accountId", primarykey);
            AccountTable accountTableObj = realmQuery.findAll().first();

            accountName.getEditText().setText(accountTableObj.getAccountName());
            bankAccountNumber.getEditText().setText(accountTableObj.getaccountNumber());
            currntBalance.getEditText().setText(String.valueOf(accountTableObj.getBalance()));

            //account spinner
            if (accountTableObj.getAccountType().equalsIgnoreCase("Cash")) {
                accountTypeSpinner.setSelection(1);
            } else if (accountTableObj.getAccountType().equalsIgnoreCase("Bank")) {
                accountTypeSpinner.setSelection(2);
            } else if (accountTableObj.getAccountType().equalsIgnoreCase("Credit")) {
                accountTypeSpinner.setSelection(3);
            }

            // currency spinner
            if (accountTableObj.getCurrencyType().equalsIgnoreCase("INR(₹)")) {
                currency_spinner.setSelection(1);
            }
            //  selectedDate = accountTableObj.getDateType();

        } else {
            currntBalance.getEditText().setEnabled(true);
            accountTypeSpinner.setEnabled(true);
        }


        submitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (validationSuccess()) {

                    if (activity.equalsIgnoreCase("Edit")) {
                        Toast.makeText(getApplicationContext(), "Account Details Updated Succesfully", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Account Details Added Succesfully", Toast.LENGTH_LONG).show();
                    }


                    final AccountTable AccountTableModelObj = new AccountTable();

                    long nextId;

                    if (activity.equalsIgnoreCase("Edit")) {
                        nextId = primarykey;
                    } else {
                        Number current_id = realm.where(AccountTable.class).max("accountId");
                        if (current_id == null) {
                            nextId = 1;
                        } else {
                            nextId = current_id.intValue() + 1;

                        }
                    }

                    AccountTableModelObj.setaccountId(nextId);
                    AccountTableModelObj.setAccountType(accountTypeSpinner.getSelectedItem().toString());
                    AccountTableModelObj.setCurrencyType(currency_spinner.getSelectedItem().toString());
                    AccountTableModelObj.setAccountName(accountName.getEditText().getText().toString());
                    AccountTableModelObj.setaccountNumber(bankAccountNumber.getEditText().getText().toString());
                    AccountTableModelObj.setBalance(Double.parseDouble(currntBalance.getEditText().getText().toString()));
                    //    AccountTableModelObj.setDateType(selectedDate);


                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.insertOrUpdate(AccountTableModelObj);
                            //showData();
                            showAccountListActivity();
                        }
                    });

                } else {
                    //AlertDialog();
                }
            }
        });
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
    }

    public void onNothingSelected(AdapterView<?> parent) {
    }

    private Boolean validationSuccess() {

        if (accountName.getEditText().getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(), "Please enter account name", Toast.LENGTH_LONG).show();
            return false;
        }

        if (bankAccountNumber.getEditText().getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(), "Please enter account number", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (currntBalance.getEditText().getText().toString().equalsIgnoreCase("") && (currntBalance.getEditText().getText().toString().equalsIgnoreCase("0"))) {
            Toast.makeText(getApplicationContext(), "Please enter current balence", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (accountTypeSpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter account type", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!activity.equalsIgnoreCase("Edit")) {
            RealmQuery<AccountTable> accountTableResults = realm.where(AccountTable.class)
                    .equalTo("accountType", accountTypeSpinner.getSelectedItem().toString());
            if (accountTableResults.count() > 0) {
                Toast.makeText(getApplicationContext(), "You've already added this account type", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if (currency_spinner.getSelectedItemPosition() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter currency ", Toast.LENGTH_SHORT).show();
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

    private void showAccountListActivity() {
        Intent accountListActivity = new Intent(this, AccountListActivity.class);
        accountListActivity.putExtra("ActivityName", "Accounts");
        startActivity(accountListActivity);
    }

    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
