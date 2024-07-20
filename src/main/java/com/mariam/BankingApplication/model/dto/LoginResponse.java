package com.mariam.BankingApplication.model.dto;

import com.mariam.BankingApplication.model.AccountUser;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private AccountUser user;
    private String token;
}
