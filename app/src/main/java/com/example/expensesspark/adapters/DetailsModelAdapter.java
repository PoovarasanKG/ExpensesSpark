package com.example.expensesspark.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.expensesspark.R;
import com.example.expensesspark.model.DetailsModel;

import java.util.List;

public class DetailsModelAdapter extends RecyclerView.Adapter<DetailsModelAdapter.AccountListItem> {
    private List<DetailsModel> data;

    public DetailsModelAdapter(List<DetailsModel> data){
        this.data = data;
    }

    @Override
    public DetailsModelAdapter.AccountListItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.details_list_item, parent, false);
        return new AccountListItem(rowItem);
    }

    @Override
    public void onBindViewHolder(DetailsModelAdapter.AccountListItem holder, int position) {
        holder.nameLbl.setText((String) this.data.get(position).getName());
        holder.valueLbl.setText((String) this.data.get(position).getValue());
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public static class AccountListItem extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView nameLbl, valueLbl;

        public AccountListItem(View view) {
            super(view);
            view.setOnClickListener(this);
            this.nameLbl = view.findViewById(R.id.nameTv);
            this.valueLbl = view.findViewById(R.id.valueTv);

        }

        @Override
        public void onClick(View view) {
           // Toast.makeText(view.getContext(), "position : " + getLayoutPosition() + " text : " + this.valueLbl.getText(), Toast.LENGTH_SHORT).show();
        }
    }
}