package com.example.expensesspark;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class AddTransaction extends AppCompatActivity implements View.OnClickListener {

    TextInputLayout selectDate,selectTime;
    private int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        selectDate=(TextInputLayout)findViewById(R.id.dateTxt);
        selectTime=(TextInputLayout)findViewById(R.id.timeTxt);

        selectTime.getEditText().setOnClickListener(this);
        selectDate.getEditText().setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_patient_home_screen, menu);
        menu.add(0, 1, 1, menuIconWithText(ContextCompat.getDrawable(getApplicationContext(),R.drawable.home), getResources().getString(R.string.Dashboard)));
        menu.add(0, 2, 2, menuIconWithText(ContextCompat.getDrawable(getApplicationContext(),R.drawable.add), getResources().getString(R.string.AddNewTransaction)));
        menu.add(0, 3, 3, menuIconWithText(ContextCompat.getDrawable(getApplicationContext(),R.drawable.details), getResources().getString(R.string.ViewTransaction)));
        menu.add(0, 4, 4, menuIconWithText(ContextCompat.getDrawable(getApplicationContext(),R.drawable.analytics), getResources().getString(R.string.Reports)));
        menu.add(0, 4, 4, menuIconWithText(ContextCompat.getDrawable(getApplicationContext(),R.drawable.settings), getResources().getString(R.string.AccountSettings)));
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
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

    private CharSequence menuIconWithText(Drawable r, String title)
    {
        r.setBounds(0, 0, r.getIntrinsicWidth(), r.getIntrinsicHeight());
        SpannableString sb = new SpannableString("    " + title);
        ImageSpan imageSpan = new ImageSpan(r, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return sb;
    }

    @Override
    public void onClick(View view) {

        if (view == selectDate.getEditText()) {
            Toast.makeText(this, "Date is Clicked", Toast.LENGTH_SHORT).show();
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
        else
        {
            Toast.makeText(this, "Date is not Clicked", Toast.LENGTH_SHORT).show();
        }
        if (view == selectTime.getEditText()) {
            Toast.makeText(this, "Time is Clicked", Toast.LENGTH_SHORT).show();

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            selectTime.getEditText().setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }
}


