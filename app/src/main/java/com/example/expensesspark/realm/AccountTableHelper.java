package com.example.expensesspark.realm;

import com.example.expensesspark.model.AccountTable;
import com.example.expensesspark.model.TransactionTable;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class AccountTableHelper {

    Realm realm;

    public AccountTableHelper(Realm realm) {
        this.realm = realm;
    }

    //WRITE
    public void save(final AccountTable AccountTableObj)
    {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                AccountTable s=realm.copyToRealm(AccountTableObj);
            }
        });

    }

    //READ
    public RealmResults<AccountTable> retrieveAccountTableItems()
    {
        ArrayList<String> AccountTableArr=new ArrayList<>();
        RealmResults<AccountTable> AccountTableList=realm.where(AccountTable.class).findAll();

        for(AccountTable AccountTableListObj:AccountTableList)
        {
            AccountTableArr.add(String.valueOf(AccountTableListObj.getaccountId()));
        }

        return AccountTableList;
    }
}