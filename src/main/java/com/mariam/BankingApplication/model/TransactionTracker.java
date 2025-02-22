package com.mariam.BankingApplication.model;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TransactionTracker {

    private List<String> transString;

    public TransactionTracker(List<String> transString){
        this.transString = transString;
    }

    public void addTxnId(String id){
        this.transString.add(id);
    }

    public String getTxnId(){
        return this.transString.get(this.transString.size() - 1);
    }

    public void clearList(){
        this.transString.clear();
    }

}
