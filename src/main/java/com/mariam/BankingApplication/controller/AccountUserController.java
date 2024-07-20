package com.mariam.BankingApplication.controller;

import com.mariam.BankingApplication.model.AccountUser;
import com.mariam.BankingApplication.model.AccountUserResource;
import com.mariam.BankingApplication.model.ResetRequest;
import com.mariam.BankingApplication.model.dto.LoginRequest;
import com.mariam.BankingApplication.model.dto.LoginResponse;
import com.mariam.BankingApplication.service.AccountUserService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class AccountUserController {

    @Autowired
    private final AccountUserService accountUserService;

    public AccountUserController(AccountUserService accountUserService){this.accountUserService = accountUserService;}

    @GetMapping("/allUser")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Iterable<AccountUser>> getAllUser() {return accountUserService.getAllUser();}

    @GetMapping("/user/{id}")
    public ResponseEntity<AccountUser> getUserById(@PathVariable int id) {return accountUserService.getUserById(id);}

    @GetMapping("/user")
    public ResponseEntity<AccountUser> getByUsername(@RequestParam String name) {return accountUserService.getByUsername(name);}

    /*@PostMapping("/user")
    public ResponseEntity<AccountUser> addNewUser(@RequestBody AccountUser accountUser) throws MessagingException {return accountUserService.addNewUser(accountUser);}
*/
    @PutMapping("/user/{id}")
    public ResponseEntity<AccountUser> updateUser(@PathVariable int id, @RequestBody AccountUser accountUser) {return accountUserService.updateUser(id, accountUser);}

    @DeleteMapping("/user/{id}")
    public ResponseEntity<AccountUser> deleteUser(@PathVariable int id) {return accountUserService.deleteUser(id);}

    @PostMapping("/register")
    public ResponseEntity<AccountUser> postAccountUser(@RequestBody AccountUser user) throws MessagingException {
        return accountUserService.postAccountUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginRequest loginRequest) throws MessagingException {
        return accountUserService.login(loginRequest);
    }

    @GetMapping("/resource/{id}")
    public ResponseEntity<AccountUserResource> getMusicResource(@PathVariable int id){
        AccountUser account = accountUserService.getUserById(id).getBody();
        AccountUserResource accountUserResource = new AccountUserResource();
        accountUserResource.setAccountUser(account);
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AccountUserController.class).getUserById(id)).withSelfRel();
        Link delete = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AccountUserController.class).deleteUser(id)).withRel("delete");
        Link update = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AccountUserController.class).updateUser(id,account)).withRel("update");
        accountUserResource.add(selfLink, delete,update);
        return new ResponseEntity<>(accountUserResource, HttpStatus.OK);

    }

    @PostMapping("/resetPassword")
    public ResponseEntity<String> getResetCode(@RequestParam String username) throws MessagingException{
        return new ResponseEntity<>(accountUserService.resetUserPassword(username), HttpStatus.ACCEPTED);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody ResetRequest request){
        return new ResponseEntity<>(accountUserService.changePassword(request), HttpStatus.ACCEPTED);
    }
}

