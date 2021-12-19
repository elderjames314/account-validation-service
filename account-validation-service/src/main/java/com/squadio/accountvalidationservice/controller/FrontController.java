package com.squadio.accountvalidationservice.controller;

import com.squadio.accountvalidationmodule.remoteservice.RestClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    private RestClientService restClientService;


    @GetMapping("/user/{username}")
    public ResponseEntity<?> findUserByUsername(@PathVariable String username) {

        return restClientService.FindUserByUsername(username);

    }



}
