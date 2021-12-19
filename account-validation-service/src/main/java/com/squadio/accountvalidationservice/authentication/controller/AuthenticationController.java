package com.squadio.accountvalidationservice.authentication.controller;


import com.squadio.accountvalidationmodule.authentication.entity.AccountUser;
import com.squadio.accountvalidationmodule.authentication.entity.AuthRequest;
import com.squadio.accountvalidationmodule.authentication.entity.AuthResponse;
import com.squadio.accountvalidationmodule.authentication.repository.UserRepository;
import com.squadio.accountvalidationmodule.authentication.util.JwtUtil;
import com.squadio.accountvalidationmodule.exception.model.ErrorReponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
public class AuthenticationController {

    @Autowired
    private UserRepository repository;

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;


    @GetMapping("/")
    public String welcome() {
        return "Welcome to Squadio account validation service";
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@Valid @RequestBody AuthRequest authRequest) throws Exception {
        AuthResponse authResponse = new AuthResponse();
        ErrorReponse errorReponse = new ErrorReponse();
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
        } catch (Exception ex) {
            errorReponse.setResponseMessage("Invalid username/password");
            errorReponse.setResponseCode("89");
            //throw new Exception("inavalid username/password");
            return ResponseEntity.ok(errorReponse);
        }


        AccountUser user = repository.findByUserName(authRequest.getUsername());
        if(user != null) {
            authResponse.setUsername(user.getUsername());
          //  authResponse.setEmail(user.getEmail());
            authResponse.setToken(jwtUtil.generateToken(authRequest.getUsername()));
           // authResponse.setRoles(new String[]{"ADMIN", "USER"});

            //get team member details here
            //AppResponse<TeamMember2> appResponse = teamService.getMemberByUsername(user.getUsername());
           // authResponse.setTeamMember(appResponse.getMessage());

            return ResponseEntity.ok(authResponse);
        }
        errorReponse.setResponseMessage("User detail not found in our database");
        errorReponse.setResponseCode("89");
        return ResponseEntity.ok(errorReponse);

    }
}
