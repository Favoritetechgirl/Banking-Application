package com.mariam.BankingApplication.model;

import org.springframework.hateoas.RepresentationModel;

public class AccountUserResource extends RepresentationModel<AccountUserResource> {
    private AccountUser accountUser;

    public AccountUser getAccountUser() {
        return accountUser;
    }

    public void setAccountUser(AccountUser accountUser) {
        this.accountUser = accountUser;
    }
}
