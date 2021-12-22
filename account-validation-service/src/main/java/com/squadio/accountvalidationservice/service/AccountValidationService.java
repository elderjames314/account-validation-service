package com.squadio.accountvalidationservice.service;

import com.squadio.accountvalidationservice.model.*;
import com.squadio.accountvalidationservice.remoteservice.RestClientService;
import com.squadio.accountvalidationservice.remoteservice.model.User;
import com.squadio.accountvalidationservice.utility.Common;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author jamesoladimeji
 * @created 19/12/2021 - 12:40 PM
 * @project IntelliJ IDEA
 */
@Service
@Slf4j
public class AccountValidationService {

    @Autowired
    private RestClientService restClientService;

    private Map<String, String> userMap = new HashMap<>();

    @Autowired
    private Common common;

    private static boolean getThreeMonthAccountStatement(Statement statement) {
        boolean b = false;
        try {
            Date created = new SimpleDateFormat("yyyy-MM-dd").parse(statement.getDate());
            Date todayDate = new SimpleDateFormat("yyyy-MM-dd").parse(getTodayDate());
            log.info("today: "+ todayDate);
            Date threeMonthBack = Date.from(LocalDate.of(todayDate.getYear(), todayDate.getMonth(), todayDate.getDay()).minusMonths(3).atStartOfDay(ZoneId.systemDefault()).toInstant());
            log.info("threeMonthBack: "+ threeMonthBack);
            b = created.before(todayDate) && created.after(threeMonthBack);
        } catch (ParseException e) {
            e.printStackTrace();
            log.info("Error occured while search request with no params given");
        }
        return b;
    }

    private static String getTodayDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.now();
        return dtf.format(localDate);
    }



    //search
    public AppResponse search(SearchModel searchModel) {
        NormalResponse normalResponse = new NormalResponse();
        normalResponse.setResponseCode("000");
        normalResponse.setResponseMessage("SUCCESS");
        AppResponse response = new AppResponse();
        try {
            //if not paramets specify
            StatementRequest statementRequest = new StatementRequest(searchModel.getUserId());
            List<Statement> statements = restClientService.getUserStatement(statementRequest);
            if(statements.size() > 0) {
                //we have statements
                List<Statement> statementResults;
                //this means non of the parameters given, therefore return three months back statements
                if(searchModel.getToAmount() == 0.0 && searchModel.getFromAmount() == 0.0
                        && searchModel.getFromDate() == null && searchModel.getToDate() == null){
                    log.info("search statements when no params given");
                    statementResults = statements.stream().filter(AccountValidationService::getThreeMonthAccountStatement).collect(Collectors.toList());

                }
                else {
                    log.info("search based on the params given");
                    statementResults = statements.stream().filter(statement -> {
                        boolean b = false;
                        try {
                            Date created = new SimpleDateFormat("yyyy-MM-dd").parse(statement.getDate());
                            b = created.before(searchModel.getToDate()) && created.after(searchModel.getFromDate());
                        } catch (ParseException e) {
                            e.printStackTrace();
                            log.info("Error occured while search request with params given");
                        }
                        return b;
                    }).filter(statement -> Double.parseDouble(statement.getAmount()) >= searchModel.getFromAmount()
                            && Double.parseDouble(statement.getAmount()) <= searchModel.getToAmount() ).collect(Collectors.toList());
                }
                List<Statement> statementWithHashedAccount = new ArrayList<>();
                if(statementResults.size() > 0) {
                    for (Statement statement : statementResults) {
                        String hashedAccount = common.hashedString(statement.getAccountNumber());
                        Statement newStatement = new StatementBuilder()
                                .setAccountNumber(hashedAccount)
                                .setAmount(statement.getAmount())
                                .setDate(statement.getDate())
                                .setDescription(statement.getDescription())
                                .build();
                        statementWithHashedAccount.add(newStatement);
                    }
                    response.setMessages(statementWithHashedAccount);
                }else {
                    normalResponse.setResponseMessage("No statement found for the params given!");
                }

            }else {
                normalResponse.setResponseMessage("No statements for this account: "+ searchModel.getUserId()+ " yet !");
                response.setMessages(new ArrayList());
            }

        }catch (Exception ex) {
            ex.printStackTrace();
            normalResponse.setResponseCode("99");
            normalResponse.setResponseMessage("Error occured while perform searching operation");
            log.error("Error occured while perform searching operation: "+ ex.getMessage());
        }
        response.setResponse(normalResponse);
        return response;
    }

    //get user statments
    public AppResponse getUserStatement(StatementRequest statementRequest) {
        NormalResponse normalResponse = new NormalResponse();
        AppResponse response = new AppResponse();
        try{
            if(common.isAdmin()) {
                List<Statement> statements = restClientService.getUserStatement(statementRequest);
                normalResponse.setResponseMessage("SUCCESS");
                normalResponse.setResponseCode("000");
                if(statements.size() > 0 ) {
                    response.setMessages(statements);
                }else {
                    normalResponse.setResponseMessage("No Statements for provided account number: " + statementRequest.getAccountId());
                }

            }else{
                //check if this logged user owns the ID passed
                String loggedOnUser = common.getLoggedInUser();
                log.info("Logged on user: "+ loggedOnUser);
                String loggedOnUserId = userMap.get(loggedOnUser);
                log.info("logged ID: "+ loggedOnUserId);
                if(loggedOnUserId.equals(statementRequest.getAccountId())) {
                    List<UserAccount> accounts = restClientService.getUserAccount(statementRequest.getAccountId());
                    normalResponse.setResponseMessage("SUCCESS");
                    normalResponse.setResponseCode("000");
                    response.setMessages(accounts);
                }else {
                    //it means you the user_id passed is not yours
                    normalResponse.setResponseMessage("You do not have required priviled to perform this operation");
                    normalResponse.setResponseCode("87");
                    response.setMessages(new ArrayList());
                }
            }

        }catch (Exception ex) {
            ex.printStackTrace();
            log.info("Error occured while fetching user statement {}", ex.getMessage());
            normalResponse.setResponseMessage("Error occured while fetching user statement {}");
        }
        response.setResponse(normalResponse);
        return response;
    }

    //fet the list of user accounts service
    public AppResponse getUserAccounts(String user_id) {
        NormalResponse normalResponse = new NormalResponse();
        AppResponse response = new AppResponse();
        try{
            if(common.isAdmin()) {
                List<UserAccount> accounts = restClientService.getUserAccount(user_id);
                normalResponse.setResponseMessage("SUCCESS");
                normalResponse.setResponseCode("000");
                if(accounts.size() > 0) {
                    response.setMessages(accounts);
                }else {
                    normalResponse.setResponseMessage("No accounts found for the userId provided: "+ user_id);
                    response.setMessages(new ArrayList());
                }

            }else{
                //check if this logged user owns the ID passed
                String loggedOnUser = common.getLoggedInUser();
                log.info("Logged on user: "+ loggedOnUser);
                String loggedOnUserId = userMap.get(loggedOnUser);
                log.info("logged ID: "+ loggedOnUserId);
                if(loggedOnUserId.equals(user_id)) {
                    List<UserAccount> accounts = restClientService.getUserAccount(user_id);
                    normalResponse.setResponseMessage("SUCCESS");
                    normalResponse.setResponseCode("000");
                    response.setMessages(accounts);
                }else {
                    //it means you the user_id passed is not yours
                    normalResponse.setResponseMessage("You do not have required priviled to perform this operation");
                    normalResponse.setResponseCode("87");
                }
            }

        }catch (Exception ex) {
            ex.printStackTrace();
            log.info("Error occured while fetching single user {}", ex.getMessage());
            normalResponse.setResponseMessage("Error occured while fetching user accounts {}");
        }
        response.setResponse(normalResponse);
        return response;

    }

    public AppResponse getAllAccountUsers() {
        NormalResponse normalResponse = new NormalResponse();
        AppResponse response = new AppResponse();
        try {
            List<User> users =  restClientService.getListOfUsers();
            response.setMessages(users);
            normalResponse.setResponseCode("000");
            normalResponse.setResponseMessage("SUCCESS");

        }catch (Exception ex) {
            ex.printStackTrace();
            log.info("Error occured while trying to get all users: {}", ex.getMessage());
            normalResponse.setResponseMessage("Error occured while trying to get all users");
        }
        response.setResponse(normalResponse);
        return response;

    }

    public AppResponse getSingleUser(String username) {
        NormalResponse normalResponse = new NormalResponse();
        AppResponse response = new AppResponse();
        try {
            User user = restClientService.FindUserByUsername(username);
            normalResponse.setResponseCode("000");
            normalResponse.setResponseMessage("SUCCESS");
            response.setData(user);
            //clear map
            userMap.clear();
            //save user detail inside map
            userMap.put(user.getName(), user.getId());
            log.info("username: "+ user.getName());
            log.info("Id: "+ user.getId());
            log.info("MAP COUNT: "+ userMap.size());
        }catch (Exception ex) {
            ex.printStackTrace();
            log.info("Error occured while trying to get single user: {}", ex.getMessage());
            normalResponse.setResponseMessage("Error occured while trying to get all users");
        }
        response.setResponse(normalResponse);
        return response;

    }
}
