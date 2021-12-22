package com.squadio.accountvalidationservice.remoteservice;

import com.google.gson.Gson;
import com.squadio.accountvalidationservice.model.Statement;
import com.squadio.accountvalidationservice.model.StatementRequest;
import com.squadio.accountvalidationservice.model.UserAccount;
import com.squadio.accountvalidationservice.remoteservice.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.common.input.LineSeparatorDetector;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * @author jamesoladimeji
 * @created 22/12/2021 - 5:12 PM
 * @project IntelliJ IDEA
 */
@Slf4j
class RestClientServiceTest {

    @Test
    @DisplayName("Should fetch the list of statement when account number is provided.")
    @Order(1)
    public void shouldGetUserStatement() {
        try {
            StatementRequest statementRequest = new StatementRequest("CbIJb0i3vQ");
            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<StatementRequest> request = new HttpEntity<>(statementRequest);
            ResponseEntity<List<Statement>> statements = restTemplate.exchange("https://purple-fire-5350.getsandbox.com:443/accounts/statements", HttpMethod.POST, request, new ParameterizedTypeReference<List<Statement>>() {});
            Assertions.assertFalse(statements.getBody().isEmpty());
            Assertions.assertNotEquals(0, statements.getBody().size());
            Assertions.assertEquals("128376876123", statements.getBody().get(0).getAccountNumber());
        }catch (Exception ex) {
            log.error("Error occured while fetching user account statement: "+ ex);
            ex.printStackTrace();
        }

    }

    @Test
    @DisplayName("Should failed and throw warning message when account number is not provided")
    @Order(2)
    public void shouldFailToGetStatement() {
        try{
            StatementRequest statementRequest = new StatementRequest("");
            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<StatementRequest> request = new HttpEntity<>(statementRequest);
            ResponseEntity<List<Statement>> statements = restTemplate.exchange("https://purple-fire-5350.getsandbox.com:443/accounts/statements", HttpMethod.POST, request, new ParameterizedTypeReference<List<Statement>>() {});
            Assertions.assertEquals(BAD_REQUEST, statements.getStatusCode());
        }catch (Exception ex) {

        }

    }

    @Test
    @DisplayName("Should get user account upon provided user ID")
    @Order(3)
    public void shouldGetUserAccount() {
        String userId = "qbnKddlq70";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> httpEntity = new HttpEntity<>("parameters", headers);
        Map<String, String> params = new HashMap<>();
        params.put("user-id", userId);
        ResponseEntity<List<UserAccount>> accounts =
                restTemplate.exchange("https://purple-fire-5350.getsandbox.com:443/accounts/{user-id}", HttpMethod.GET, httpEntity, new ParameterizedTypeReference<List<UserAccount>>() {}, params);
        Assertions.assertNotNull(accounts.getBody());
        Assertions.assertEquals("CbIJb0i3vQ", accounts.getBody().get(0).getId());
    }


    @Test
    @DisplayName("Should get user detail upon provided valid username")
    @Order(4)
    public void shouldGetUser() {
        String username = "Mohamed";
        RestTemplate restTemplate = new RestTemplate();;
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        User user = restTemplate.getForObject("https://purple-fire-5350.getsandbox.com:443/users/{username}", User.class, params);
        Assertions.assertEquals("qbnKddlq70", user.getId());
        Assertions.assertEquals("Mohamed", user.getName());
    }


}