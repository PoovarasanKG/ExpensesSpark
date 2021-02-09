package com.example.expensesspark.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.accounts.Account;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.expensesspark.R;

public class Dashboard extends AppCompatActivity {

    GridLayout menuGridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        menuGridLayout = (GridLayout) findViewById(R.id.menuGridLayout);

        int child_count = menuGridLayout.getChildCount();
        for(int i =0;i<child_count;i++){
            menuGridLayout.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int selectedPosition = menuGridLayout.indexOfChild(v);
                   navigate(selectedPosition);
                }
            });
        }
    }

    public void navigate(int position)
    {
        if (position == 0)
        {
            Intent i = new Intent(this, AccountListActivity.class);
            startActivity(i);
        }
        else if (position == 1)
        {
            Intent i = new Intent(this, TransactionsListActivity.class);
            startActivity(i);
        }
        else if (position == 2)
        {
            Intent i = new Intent(this, NewAccountActivity.class);
            startActivity(i);
        }
        else if (position == 3)
        {
            Intent i = new Intent(this, AddTransaction.class);
            startActivity(i);
        }
        else if (position == 4)
        {
            Intent i = new Intent(this, NewAccountActivity.class);
            startActivity(i);
        }
    }
}