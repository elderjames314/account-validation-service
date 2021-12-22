package com.squadio.accountvalidationservice.controller;


import com.squadio.accountvalidationservice.authentication.entity.AuthRequest;
import com.squadio.accountvalidationservice.authentication.entity.AuthResponse;
import com.squadio.accountvalidationservice.authentication.util.JwtUtil;
import com.squadio.accountvalidationservice.exception.model.ErrorReponse;
import com.squadio.accountvalidationservice.model.NormalResponse;
import com.squadio.accountvalidationservice.model.SearchModel;
import com.squadio.accountvalidationservice.model.StatementRequest;
import com.squadio.accountvalidationservice.remoteservice.model.User;
import com.squadio.accountvalidationservice.service.AccountValidationService;
import com.squadio.accountvalidationservice.model.AppResponse;
import com.squadio.accountvalidationservice.utility.Common;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author jamesoladimeji
 * @created 19/12/2021 - 8:36 AM
 * @project IntelliJ IDEA
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class FrontController {

    @Autowired
    private AccountValidationService accountValidationService;

    @Autowired
    private Common common;

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;
    private String generedToken = null;
    private String loggedIp = null;
    private String loggedInUsername =  null;

    @GetMapping("/")
    public String welcome() {
        return "Welcome to Squadio account validation service";
    }


    @GetMapping("/logout")
    public ResponseEntity logout(HttpServletRequest request) {
        log.info("LOGGING OUT");
        if(loggedInUsername == null) {
            return  ResponseEntity.ok("Please login first");
        }
        try {
            ResponseEntity<String> message = checkIfTokeHasExpired(generedToken);
            if (message != null) return message;
            ResponseEntity<String> UNAUTHORIZED = checkIfSameUserloggedOn(request);
            if (UNAUTHORIZED != null) return UNAUTHORIZED;
            loggedInUsername = null;
            loggedIp = null;
            generedToken = null;
            return ResponseEntity.ok("You have successfully logged out");
        }catch (Exception ex) {
            return ResponseEntity.ok(ex.getMessage());
        }

    }
    private ResponseEntity<String> checkIfSameUserloggedOn(HttpServletRequest request) {
        if (jwtUtil.validateToken(generedToken) && !loggedIp.equals(request.getRemoteAddr())) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
        return null;
    }
    @PostMapping("/authenticate")
    public ResponseEntity authenticate(@Valid @RequestBody AuthRequest authRequest, HttpServletRequest request) throws Exception {
        log.info("AUTHENTICATING USER");
        AuthResponse authResponse = new AuthResponse();
        ErrorReponse errorReponse = new ErrorReponse();
        log.info("getLoggedInUser: "+ common.getLoggedInUser());
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
        } catch (Exception ex) {
            ex.printStackTrace();
            log.info("ERROR OCCURED WHILE AUTHENTICATING USER {}, : {}", authRequest.getUsername(), ex.getMessage());
            errorReponse.setResponseMessage("Invalid username/password");
            errorReponse.setResponseCode("89");
            //throw new Exception("inavalid username/password");
            return ResponseEntity.ok(errorReponse);
        }
        AppResponse<User> response = accountValidationService.getSingleUser(authRequest.getUsername());
        User user = response.getData();
        if(user != null) {
            authResponse.setUsername(user.getName());
            authResponse.setId(user.getId());
            log.info("USERNAME: "+ authRequest.getUsername());
            String token = jwtUtil.generateToken(authRequest.getUsername(), request);
            authResponse.setToken(token);
            generedToken = token;
            loggedIp = request.getRemoteAddr();
            loggedInUsername = user.getName();
            log.info("my Logged Use: "+ common.getLoggedInUser());
            return ResponseEntity.ok(authResponse);
        }
        errorReponse.setResponseMessage("User detail not found in our database");
        errorReponse.setResponseCode("89");
        return ResponseEntity.ok(errorReponse);

    }

    //search request
    @PostMapping("/statements/search")
    public ResponseEntity search(@Valid @RequestBody SearchModel searchModel) {
        AppResponse response = accountValidationService.search(searchModel);
        return ResponseEntity.ok(response);
    }



    //get account statement by user account ID
    @PostMapping("/accounts/statements")
    public ResponseEntity<?> getUserAccountStatement(@Valid @RequestBody StatementRequest statementRequest) {
        log.info("FETCHING USER ACCOUNT STATEMENT");
        AppResponse response = accountValidationService.getUserStatement(statementRequest);
        return ResponseEntity.ok(response);
    }

    //get user accounts by user id
    @GetMapping("/accounts/{user_id}")
    public ResponseEntity getUserAccounts(@Valid @PathVariable String user_id) {
        log.info("FETCHING USER ACCOUNT DETAIL");
        AppResponse response = accountValidationService.getUserAccounts(user_id);
        if(response.getResponse().getResponseCode().equals("87")) {
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok(response);
    }


    //get user
    @GetMapping("/users/{username}")
    public ResponseEntity findUserByUsername(@Valid @PathVariable String username) {
        log.info("STARTED");
        ResponseEntity<AppResponse> appResponse = runValidationChecks(username);
        if (appResponse != null) return appResponse;
        AppResponse response =  accountValidationService.getSingleUser(username);
        return ResponseEntity.ok(response);
    }

    //get all users
    @GetMapping("/users")
    public ResponseEntity getAllLoggedInUsers() {
        if(!common.isAdmin())  {
            NormalResponse response = new NormalResponse();
            response.setResponseMessage("You do not have required privilege to perform this operation");
            AppResponse appResponse = new AppResponse();
            appResponse.setResponse(response);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        AppResponse response = accountValidationService.getAllAccountUsers();
        return ResponseEntity.ok(response);
    }



    private ResponseEntity runValidationChecks(String username) {
        //do this checked if and only if the logged in user is not admin
        if(!common.isAdmin()) {
            //checked if logged in is this user
            ResponseEntity  appResponse =  common.checkLoggedInUser(username);
            if (appResponse != null) return appResponse;
        }
        return null;
    }

    private ResponseEntity<String> checkIfTokeHasExpired(String generedToken) {
        //checked if token is expred
        if(loggedInUsername != null && jwtUtil.isTokenExpired(generedToken)) {
            String message = "Token has expired. please try and log in again!";
            return ResponseEntity.ok(message);
        }
        return null;
    }




}
