package com.example.expensesspark.realm;

import com.example.expensesspark.model.TransactionTable;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class TransactionTableHelper {

    Realm realm;

    public TransactionTableHelper(Realm realm) {
        this.realm = realm;
    }

    //WRITE
    public void save(final TransactionTable transactionTableObj)
    {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TransactionTable s=realm.copyToRealm(transactionTableObj);
            }
        });

    }

    //READ
    public RealmResults<TransactionTable> retrieveTransactionTableItems()
    {
        ArrayList<String> transactionTableArr=new ArrayList<>();
        RealmResults<TransactionTable> transactionTableList=realm.where(TransactionTable.class).findAll();

        for(TransactionTable transactionTableListObj:transactionTableList)
        {
            transactionTableArr.add(String.valueOf(transactionTableListObj.getTransactionId()));
        }

        return transactionTableList;
    }
}