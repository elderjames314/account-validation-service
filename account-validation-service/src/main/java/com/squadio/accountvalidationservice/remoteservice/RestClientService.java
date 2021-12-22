package com.squadio.accountvalidationservice.remoteservice;

import com.squadio.accountvalidationservice.model.Statement;
import com.squadio.accountvalidationservice.model.StatementRequest;
import com.squadio.accountvalidationservice.model.UserAccount;
import com.squadio.accountvalidationservice.remoteservice.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.swing.plaf.nimbus.State;
import java.util.*;

/**
 * @author jamesoladimeji
 * @created 19/12/2021 - 8:02 AM
 * @project IntelliJ IDEA
 */
@Service
@Slf4j
public class RestClientService {

    @Value("${GET_USER_INFORMATION_URL}")
    private String userInformationUrl;
    @Value("${GET_ALL_USERS_URL}")
    private String allUserUrl;
    @Value("${GET_ALL_USER_ACCOUNTS}")
    private String singleAccountUrl;
    @Value("${GET_ALL_ACCOUNT_STATEMENTS}")
    private String userAccountStatementUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    //get user statement
    public List<Statement> getUserStatement(StatementRequest statementRequest) {
        try {
            HttpEntity<StatementRequest> request = new HttpEntity<>(statementRequest);
            ResponseEntity<List<Statement>> statements = restTemplate.exchange(userAccountStatementUrl, HttpMethod.POST, request, new ParameterizedTypeReference<List<Statement>>() {});
            return  statements.getBody();
        }catch (Exception ex) {
            log.error("Error occured while fetching user account statement: "+ ex);
            ex.printStackTrace();
            return new ArrayList<>();
        }

    }


    //get user account
    public List<UserAccount> getUserAccount(String user_id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> httpEntity = new HttpEntity<>("parameters", headers);
        Map<String, String> params = new HashMap<>();
        log.info("user-id: "+ user_id);
        params.put("user-id", user_id);
        ResponseEntity<List<UserAccount>> accounts =
                restTemplate.exchange(singleAccountUrl, HttpMethod.GET, httpEntity, new ParameterizedTypeReference<List<UserAccount>>() {}, params);
        //ResponseEntity<List<UserAccount>> accounts = restTemplate.exchange(singleAccountUrl, new ParameterizedTypeReference<List<UserAccount>>() {}, params);
        return accounts.getBody();
    }

    //find user by username
    public User FindUserByUsername(String username){
        User user = null;
        //MIMIC THE ADMIN USER AS IS NOT FOUND IN THE DB
        if("Admin".equals(username)) {
            return new User("Admin78", "Admin");
        }
        try {
            log.info("userInformationUrl: "+ userInformationUrl);
            Map<String, String> params = new HashMap<>();
            params.put("username", username);
           // RestTemplate restTemplate = new RestTemplate();
            user = restTemplate.getForObject(userInformationUrl, User.class, params);
            log.info("RESPONSE: "+ user);
        }catch (Exception ex) {
            ex.printStackTrace();
            log.error("ERROR OCCURED: "+ ex.getMessage());
        }
        return user;

    }

    //get all users
    public List<User> getListOfUsers() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> httpEntity = new HttpEntity<>("parameters", headers);
       // RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<User>> accounts =
                restTemplate.exchange(allUserUrl, HttpMethod.GET, httpEntity, new ParameterizedTypeReference<List<User>>() {});

        log.info("USERS: "+ accounts);

        return accounts.getBody();
    }

    //get one singe account





}
