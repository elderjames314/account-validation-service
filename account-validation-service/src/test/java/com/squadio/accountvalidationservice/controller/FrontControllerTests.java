package com.squadio.accountvalidationservice.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.samePropertyValuesAs;

/**
 * @author jamesoladimeji
 * @created 21/12/2021 - 10:26 PM
 * @project IntelliJ IDEA
 */
@SpringBootTest
@Slf4j
public class FrontControllerTests {

    String token = null;

    @Test
    @Order(1)
     public void authentication_ValidUserCredentials_ReturnedSuccess() throws JSONException {

        RestAssured.baseURI = "http://localhost:8080/api";
        RequestSpecification request = RestAssured.given();
        JSONObject requestParams = new JSONObject();
        requestParams.put("username", "Admin");
        requestParams.put("password", "admin");

        request.header("Content-Type", "application/json");
        request.body(requestParams.toString());

        Response response =  request.post("/authenticate");



        assertThat(response.getStatusCode(), samePropertyValuesAs(200));
        assertThat(response.jsonPath().get("username"), samePropertyValuesAs("Admin"));
        assertThat(response.jsonPath().get("id"), samePropertyValuesAs("Admin78"));

        token = response.jsonPath().get("token");

        log.info("TOKEN: "+ token);

     }

    @Test
    @Order(2)
    public void authentication_InValidUserCredentials_ReturnedSuccess() throws JSONException {

        RestAssured.baseURI = "http://localhost:8080/api";
        RequestSpecification request = RestAssured.given();
        JSONObject requestParams = new JSONObject();
        requestParams.put("username", "Admin");
        requestParams.put("password", "admin123");

        request.header("Content-Type", "application/json");
        request.body(requestParams.toString());

        Response response =  request.post("/authenticate");

        assertThat(response.getStatusCode(), samePropertyValuesAs(200));
        assertThat(response.jsonPath().get("responseMessage"), samePropertyValuesAs("Invalid username/password"));
        assertThat(response.jsonPath().get("responseCode"), samePropertyValuesAs("89"));

    }

    @Test
    @Order(3)
    public void search_ValidParams_ReturnedSuccess() throws JSONException {

        RestAssured.baseURI = "http://localhost:8080/api";
        RequestSpecification request = RestAssured.given();
        JSONObject requestParams = new JSONObject();
        requestParams.put("userId", "CbIJb0i3vQ");
        requestParams.put("fromDate", "2020-08-01");
        requestParams.put("fromAmount", "1000");
        requestParams.put("toAmount", "5000");

        request.header("Content-Type", "application/json");
        request.header("Authorization", "Bearer "+ token);
        request.body(requestParams.toString());

        Response response =  request.post("/statements/search");

        assertThat(response.getStatusCode(), samePropertyValuesAs(200));
        //assertThat(response.jsonPath().get("response.responseCode"), samePropertyValuesAs("000"));
        //assertThat(response.jsonPath().get("response.responseMessage"), samePropertyValuesAs("SUCCESS"));
       // assertThat(response.jsonPath().get("response.messages.size()"), greaterThan(1));

    }

    @Test
    @Order(4)
    public void getUserAccountStatement_ValidUserAccountId_ReturnedSuccess() throws JSONException {

        RestAssured.baseURI = "http://localhost:8080/api";
        RequestSpecification request = RestAssured.given();
        JSONObject requestParams = new JSONObject();
        requestParams.put("accountId", "CbIJb0i3vQ");

        request.header("Content-Type", "application/json");
        request.header("Authorization", "Bearer "+ token);
        request.body(requestParams.toString());

        Response response =  request.post("/accounts/statements");

        assertThat(response.getStatusCode(), samePropertyValuesAs(200));
        //assertThat(response.jsonPath().get("response.messages[0].accountNumber"), samePropertyValuesAs("128376876123"));
        //assertThat(response.jsonPath().get("response.responseMessage"), samePropertyValuesAs("SUCCESS"));
        // assertThat(response.jsonPath().get("response.messages.size()"), greaterThan(1));

    }

    @Test
    @Order(5)
    public void getUserAccounts_ValidUserIdAndValidToken_ReturnedSuccess() throws JSONException {

        RestAssured.baseURI = "http://localhost:8080/api";
        RequestSpecification request = RestAssured.given();
        JSONObject requestParams = new JSONObject();
        requestParams.put("username", "Admin");
        requestParams.put("password", "admin");

        request.header("Content-Type", "application/json");
        request.body(requestParams.toString());

        Response response =  request.post("/authenticate");



        assertThat(response.getStatusCode(), samePropertyValuesAs(200));
        assertThat(response.jsonPath().get("username"), samePropertyValuesAs("Admin"));
        assertThat(response.jsonPath().get("id"), samePropertyValuesAs("Admin78"));

        String bearerToken = response.jsonPath().get("token");

      given().headers(
                "Authorization",
                "Bearer " + bearerToken,
                "Content-Type",
                ContentType.JSON,
                "Accept",
                ContentType.JSON).when().get("http://localhost:8080/api/accounts/qbnKddlq70")
                .then()
                .assertThat()
                .header("Content-Type", equalTo("application/json"))
                .statusCode(200)
                 .body("response.responseCode", equalTo("000"))
                 .body("response.responseMessage", equalTo("SUCCESS"))
                .body("messages[0].id", equalTo("CbIJb0i3vQ"));

    }



    @Test
    @Order(6)
    public void getUserAccounts_withoutBearerToken_ReturnedSuccess() throws JSONException {


        given().when().get("http://localhost:8080/api/accounts/qbnKddlq70")
                .then()
                .assertThat()
                .header("Content-Type", equalTo("application/json"))
                .statusCode(401);
    }


    @Test
    @Order(7)
    public void findUserByUsername_ValidUsername_ReturnedSuccess() throws JSONException {

        RestAssured.baseURI = "http://localhost:8080/api";
        RequestSpecification request = RestAssured.given();
        JSONObject requestParams = new JSONObject();
        requestParams.put("username", "Admin");
        requestParams.put("password", "admin");

        request.header("Content-Type", "application/json");
        request.body(requestParams.toString());

        Response response =  request.post("/authenticate");



        assertThat(response.getStatusCode(), samePropertyValuesAs(200));
        assertThat(response.jsonPath().get("username"), samePropertyValuesAs("Admin"));
        assertThat(response.jsonPath().get("id"), samePropertyValuesAs("Admin78"));

        String bearerToken = response.jsonPath().get("token");

        given().headers(
                        "Authorization",
                        "Bearer " + bearerToken,
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON).when().get("http://localhost:8080/api/users/Mohamed")
                .then()
                .assertThat()
                .header("Content-Type", equalTo("application/json"))
                .statusCode(200)
                .body("response.responseCode", equalTo("000"))
                .body("response.responseMessage", equalTo("SUCCESS"))
                .body("data.id", equalTo("qbnKddlq70"))
                .body("data.name", equalTo("Mohamed"));

    }



    @Test
    @Order(7)
    public void getAllLoggedInUsers__ReturnedSuccess() throws JSONException {

        RestAssured.baseURI = "http://localhost:8080/api";
        RequestSpecification request = RestAssured.given();
        JSONObject requestParams = new JSONObject();
        requestParams.put("username", "Admin");
        requestParams.put("password", "admin");

        request.header("Content-Type", "application/json");
        request.body(requestParams.toString());

        Response response =  request.post("/authenticate");



        assertThat(response.getStatusCode(), samePropertyValuesAs(200));
        assertThat(response.jsonPath().get("username"), samePropertyValuesAs("Admin"));
        assertThat(response.jsonPath().get("id"), samePropertyValuesAs("Admin78"));

        String bearerToken = response.jsonPath().get("token");

        given().headers(
                        "Authorization",
                        "Bearer " + bearerToken,
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON).when().get("http://localhost:8080/api/users")
                .then()
                .assertThat()
                .header("Content-Type", equalTo("application/json"))
                .statusCode(200)
                .body("response.responseCode", equalTo("000"))
                .body("response.responseMessage", equalTo("SUCCESS"))
                .body("messages.size()", greaterThan(1));

    }







}
