package com.squadio.accountvalidationservice.remoteservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

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


    RestTemplate restTemplate;
    HttpHeaders httpHeaders;

    public ResponseEntity<String> FindUserByUsername(String username){

        restTemplate = new RestTemplate();

        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<String> entity = new HttpEntity<>("parameters", httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(userInformationUrl, HttpMethod.GET, entity, String.class);

        log.info("RESPONSE: "+ response);

        return  response;

    }




}
