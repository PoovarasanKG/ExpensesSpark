package com.example.expensesspark.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.expensesspark.R;
import com.example.expensesspark.activity.CommonDetailsActivity;
import com.example.expensesspark.model.TransactionTable;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;

public class TransactionTableAdapter extends RecyclerView.Adapter<TransactionTableAdapter.TransactionListItem> {
    private List<TransactionTable> data;

    public TransactionTableAdapter (List<TransactionTable> data){
        this.data = data;
    }

    @Override
    public TransactionTableAdapter.TransactionListItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.transactions_list_item, parent, false);
        return new TransactionListItem(rowItem);
    }

    @Override
    public void onBindViewHolder(TransactionTableAdapter.TransactionListItem holder, int position) {
        holder.categoryLbl.setText(this.data.get(position).getCategory());
        holder.amountLbl.setText(String.valueOf(this.data.get(position).getAmount()));
        holder.dateLbl.setText(this.data.get(position).getDate() + " " + this.data.get(position).getTime());
        holder.primaryKey = this.data.get(position).getTransactionId();
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public static class TransactionListItem extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView categoryLbl, amountLbl, dateLbl;
        long primaryKey;
        public TransactionListItem(View view) {
            super(view);
            view.setOnClickListener(this);
            this.categoryLbl = view.findViewById(R.id.categoryLbl);
            this.amountLbl = view.findViewById(R.id.amountLbl);
            this.dateLbl = view.findViewById(R.id.dateLbl);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(view.getContext(), "position : " + getLayoutPosition() + " text : " + this.categoryLbl.getText(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(view.getContext(), CommonDetailsActivity.class);
            intent.putExtra("PrimaryKey", primaryKey);
            intent.putExtra("ActivityName", "TransactionListActivity");
            view.getContext().startActivity(intent);
        }
    }
}