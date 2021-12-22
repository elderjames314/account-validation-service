package com.squadio.accountvalidationservice.authentication.controller;



import com.squadio.accountvalidationservice.authentication.entity.AccountUser;
import com.squadio.accountvalidationservice.authentication.entity.AuthRequest;
import com.squadio.accountvalidationservice.authentication.entity.AuthResponse;
import com.squadio.accountvalidationservice.authentication.repository.UserRepository;
import com.squadio.accountvalidationservice.authentication.util.JwtUtil;
import com.squadio.accountvalidationservice.exception.model.ErrorReponse;
import com.squadio.accountvalidationservice.model.AppResponse;
import com.squadio.accountvalidationservice.remoteservice.model.User;
import com.squadio.accountvalidationservice.service.AccountValidationService;
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


@RestController
@RequestMapping("/api")
@Slf4j
public class AuthenticationController {

    /*@Autowired
    private AccountValidationService accountValidationService;

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private Common common;
    private String generedToken = null;



    @GetMapping("/")
    public String welcome() {
        return "Welcome to Squadio account validation service";
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        ResponseEntity<String> message = checkIfTokeHasExpired();
        if (message != null) return message;

        if (jwtUtil.validateToken(generedToken)) {
            final String ipAddress = jwtUtil.getClientIp(request);
            // not match return error
            if (!ipAddress.equals(jwtUtil.getClientIp(request))){
                //return 401
                return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
            }
        }
        return ResponseEntity.ok("You have successfully logged out");
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@Valid @RequestBody AuthRequest authRequest, HttpServletRequest request) throws Exception {
        log.info("AUTHENTICATING USER");
        AuthResponse authResponse = new AuthResponse();
        ErrorReponse errorReponse = new ErrorReponse();
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
            return ResponseEntity.ok(authResponse);
        }
        errorReponse.setResponseMessage("User detail not found in our database");
        errorReponse.setResponseCode("89");
        return ResponseEntity.ok(errorReponse);*/

   // }


}
