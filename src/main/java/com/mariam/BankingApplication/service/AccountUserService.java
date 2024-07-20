package com.mariam.BankingApplication.service;

import com.mariam.BankingApplication.model.AccountUser;
import com.mariam.BankingApplication.model.ResetCode;
import com.mariam.BankingApplication.model.ResetRequest;
import com.mariam.BankingApplication.model.Role;
import com.mariam.BankingApplication.model.dto.LoginRequest;
import com.mariam.BankingApplication.model.dto.LoginResponse;
import com.mariam.BankingApplication.repository.AccountUserRepository;
import com.mariam.BankingApplication.repository.ResetCodeRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Random;

@Service
public class AccountUserService {
    private final AccountUserRepository accountUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private ResetCodeRepository resetCodeRepository;


    public AccountUserService(AccountUserRepository accountUserRepository) {
        this.accountUserRepository = accountUserRepository;
    }

    public ResponseEntity<Iterable<AccountUser>> getAllUser(){
        return new ResponseEntity<>(accountUserRepository.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<AccountUser> getUserById(int id){
        return new ResponseEntity<>(accountUserRepository.findById(id).get(), HttpStatus.OK);
    }

    public ResponseEntity<AccountUser> getByUsername(String name){
        return new ResponseEntity<>(accountUserRepository.findByUsername(name), HttpStatus.OK);
    }

/*
    public ResponseEntity<AccountUser> addNewUser(AccountUser accountUser) throws MessagingException {
        accountUser.setPassword(passwordEncoder.encode(accountUser.getPassword()));
        accountUser.setRole(accountUser.getRole());
        messageService.registrationNotification(accountUser.getUsername(), accountUser.getFirstName());
        return new ResponseEntity<>(accountUserRepository.save(accountUser), HttpStatus.CREATED);
    }
*/

    public ResponseEntity<AccountUser> updateUser(int id, AccountUser accountUser) {
        AccountUser user = accountUserRepository.findById(id).get();
        user.setFirstName(accountUser.getFirstName());
        user.setLastName(accountUser.getLastName());
        user.setMiddleName(accountUser.getMiddleName());
        user.setUsername(accountUser.getUsername());
        user.setPassword(accountUser.getPassword());
        user.setPhoneNumber(accountUser.getPhoneNumber());
        return new ResponseEntity<>(accountUserRepository.save(user), HttpStatus.ACCEPTED);
    }

    public ResponseEntity<AccountUser> deleteUser(int id){
        return new ResponseEntity<>(accountUserRepository.findById(id).get(), HttpStatus.OK);
    }

    //register
    public ResponseEntity<AccountUser> postAccountUser(@RequestBody AccountUser user) throws MessagingException {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(user.getRole());
        messageService.registrationNotification(user.getUsername(), user.getFirstName());
        AccountUser savedUser = accountUserRepository.save(user);
        bankAccountService.createBankAccount(user, 100000.00);
        return new ResponseEntity<>(accountUserRepository.save(user), HttpStatus.CREATED);
    }

    public ResponseEntity<LoginResponse> login(LoginRequest request) throws MessagingException {
        Authentication auth = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        if( auth != null ){
            AccountUser user = accountUserRepository.findByUsername(request.getUsername());
            String token = jwtService.createToken(user);
            messageService.loginNotification(user.getUsername(), "Dear "+ user.getFirstName() +"\n" +
                    "There has been a successful login into your Banking Account. Please if you did not log in" +
                    " call us on the following number: 01-2245845, 08004455454\n"
                    + "Thank you for Banking with Us.");
            return new ResponseEntity<>(LoginResponse.builder().user(user).token(token).build(), HttpStatus.OK);
        }

        return null;
    }

    public String resetUserPassword(String username) throws MessagingException {
        AccountUser user = getByUsername(username).getBody();

        StringBuilder randomCode = new StringBuilder();
        int count = 1;
        while(count <= 6 ){
            String x = String.valueOf(new Random().nextInt(10));
            randomCode.append(x);
            count++;
        }

        assert user != null;
        messageService.sendResetCode(user.getUsername(), randomCode.toString());

        ResetCode resetCode = new ResetCode();
        resetCode.setCode(randomCode.toString());
        resetCode.setAccountUser(user);
        resetCodeRepository.save(resetCode);
        return "Reset Code Sent Successfully";
    }

    public String changePassword(ResetRequest request){

        AccountUser accountUser = getByUsername(request.getUsername()).getBody();
        String resetCode = resetCodeRepository.findByAccountUser(accountUser).getCode();

        System.out.println(resetCode);
        if(!request.getResetCode().equals(resetCode)){
            return "Invalid Reset Code";
        }
        if(!request.getPassword().equals(request.getConfirmPassword())){
            return "Password Mismatch";
        }

        assert accountUser != null;
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        accountUser.setPassword(encodedPassword);
        accountUserRepository.save(accountUser);
        return "Password changed Successfully";
    }

}