package com.example.expensesspark;

import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class NewAccountActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button submitBtn;
    EditText accountName;
    EditText bankAccountNumber;
    Spinner currency_spinner;
    Spinner accountTypeSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);

        Spinner accounttypespinner = (Spinner) findViewById(R.id.Accounttype_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Accounttype_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accounttypespinner.setAdapter(adapter);
        accounttypespinner.setOnItemSelectedListener(this);

        Spinner currencyspinner = (Spinner) findViewById(R.id.currency_spinner);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.cureency_array, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currencyspinner.setAdapter(adapter1);
        currencyspinner.setOnItemSelectedListener(this);

        submitBtn = findViewById(R.id.submitBtn);
        accountName = findViewById(R.id.AccountName);
        bankAccountNumber = findViewById(R.id.BankAccountNumber);
        currency_spinner = findViewById(R.id.currency_spinner);
        accountTypeSpinner = findViewById(R.id.Accounttype_spinner);

        submitBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (validationSuccess())
                {
                    Toast.makeText(getApplicationContext(), "New Account Added", Toast.LENGTH_LONG).show();
                }
                else
                    {
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
        if (accountName.getText().toString().equalsIgnoreCase(""))
        {
            Toast.makeText(getApplicationContext(), "Please enter account name", Toast.LENGTH_LONG).show();
            return false;
        }

        if (bankAccountNumber.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(), "Please enter account number", Toast.LENGTH_SHORT).show();
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
}
