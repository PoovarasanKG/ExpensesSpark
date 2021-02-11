package com.example.expensesspark.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.expensesspark.R;
import com.example.expensesspark.activity.CommonDetailsActivity;
import com.example.expensesspark.model.AccountTable;

import java.util.List;

public class AccountTableAdapter extends RecyclerView.Adapter<AccountTableAdapter.AccountListItem> {
    private List<AccountTable> data;

    public AccountTableAdapter(List<AccountTable> data){
        this.data = data;
    }

    @Override
    public AccountTableAdapter.AccountListItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_list_item, parent, false);
        return new AccountListItem(rowItem);
    }

    @Override
    public void onBindViewHolder(AccountTableAdapter.AccountListItem holder, int position) {
        holder.categoryLbl.setText(this.data.get(position).getAccountName());
        holder.dateLbl.setText(this.data.get(position).getDateTime());
        holder.primaryKey = this.data.get(position).getaccountId();

        if (this.data.get(position).getCurrencyType().equalsIgnoreCase("INR(₹)")) {
            holder.amountLbl.setText("₹" + String.valueOf(this.data.get(position).getBalance()));
        }
        else
        {
            holder.amountLbl.setText(String.valueOf(this.data.get(position).getBalance()));
        }
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public static class AccountListItem extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView categoryLbl, amountLbl, dateLbl;
        long primaryKey;
        public AccountListItem(View view) {
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
            intent.putExtra("ActivityName", "AccountListActivity");
            view.getContext().startActivity(intent);
        }
    }
}