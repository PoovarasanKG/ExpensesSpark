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
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;

public class NewAccountActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener

{

    Button submitButton;
    TextInputLayout accountName;
    TextInputLayout bankAccountNumber, currntBalance;
    Spinner currency_spinner;
    Spinner accountTypeSpinner;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);

        realm = Realm.getDefaultInstance();

        getSupportActionBar().setTitle("NewAccountActivity");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        submitButton = findViewById(R.id.submitButton);
        accountName = findViewById(R.id.accountname);
        bankAccountNumber = findViewById(R.id.accountnumber);
        currency_spinner = findViewById(R.id.CurrencyTypeSpinner);
        accountTypeSpinner = findViewById(R.id.AccountTypeSpinner);
        currntBalance = findViewById(R.id.currntBalance);


        submitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (validationSuccess()) {
                    Toast.makeText(getApplicationContext(), "New Account Added", Toast.LENGTH_LONG).show();

                    String currentDate = new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault()).format(new Date());

                    final AccountTable AccountTableModelObj = new AccountTable();

                    Number current_id = realm.where(AccountTable.class).max("accountId");
                    long nextId;

                    if (current_id == null) {
                        nextId = 1;
                    } else {
                        nextId = current_id.intValue() + 1;
                    }

                    AccountTableModelObj.setaccountId(nextId);
                    AccountTableModelObj.setAccountType(accountTypeSpinner.getSelectedItem().toString());
                    AccountTableModelObj.setCurrencyType(currency_spinner.getSelectedItem().toString());
                    AccountTableModelObj.setAccountName(accountName.getEditText().getText().toString());
                    AccountTableModelObj.setAccountNumber(bankAccountNumber.getEditText().getText().toString());
                    AccountTableModelObj.setBalance(Double.parseDouble(currntBalance.getEditText().getText().toString()));
                    AccountTableModelObj.setDateTime(currentDate);


                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.copyToRealm(AccountTableModelObj);
                            //showData();
                            showTransactionsListActivity();                        }
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
        if (accountName.getEditText().toString().equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(), "Please enter account name", Toast.LENGTH_LONG).show();
            return false;
        }

        if (bankAccountNumber.getEditText().toString().equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(), "Please enter account number", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (currntBalance.getEditText().toString().equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(), "Please enter currentbalence", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (accountTypeSpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter account type", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (currency_spinner.getSelectedItemPosition() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter currency ", Toast.LENGTH_SHORT).show();
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
        Intent accountListActivity = new Intent(this, AccountListActivity.class);
        startActivity(accountListActivity);
    }
}
