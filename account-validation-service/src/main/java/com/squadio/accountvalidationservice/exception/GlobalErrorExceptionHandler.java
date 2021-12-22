package com.squadio.accountvalidationservice.exception;




import com.squadio.accountvalidationservice.exception.model.ErrorReponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.swing.text.html.Option;
import java.util.Date;
import java.util.Optional;

/**
 * @author jamesoladimeji
 * @created 29/08/2021 - 4:19 PM
 * @project IntelliJ IDEA
 */
@ControllerAdvice
@Slf4j
public class GlobalErrorExceptionHandler {


    //get global exception
    //log error that for bad request
   @ExceptionHandler(Exception.class)
    public ResponseEntity handleGlobalException(Exception exception, WebRequest webRequest) {
       exception.printStackTrace();
        log.error(exception.getMessage());
        ErrorReponse response = new ErrorReponse(new Date().toString(), "99", "BAD REQUEST, PLEASE REVIEW AND TRY AGAIN");
        log.error(response.toString());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //get APi runtime exception
    //log error that is internal server error.
    @ExceptionHandler(ApiException.class)
    public ResponseEntity handleGlobalException(ApiException exception, WebRequest webRequest) {
        ErrorReponse response = new ErrorReponse(new Date().toString(), "99", "INTERNAL SERVER ERROR");
        //log the error in log file
        log.error(response.toString());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }



    //handle errors for resource not found
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity handleResourceNotFound(NoHandlerFoundException exception, WebRequest webRequest) {
        ErrorReponse response = new ErrorReponse(new Date().toString(), "99", "RESOURCES NOT FOUND");
        //log the error in log file
        log.error(response.toString());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

   //handle custom error validations
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity customErrorValidation(MethodArgumentNotValidException methodArgumentNotValidException){
        log.info("MethodArgumentNotValidException set in");
        try {
            String fieldError = methodArgumentNotValidException.getBindingResult().getFieldError().getDefaultMessage();
            if(fieldError != null) {
                ErrorReponse response = new ErrorReponse(new Date().toString(),
                        "99", fieldError);
                log.error(response.toString());
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        }catch (Exception ex){
            ex.printStackTrace();
            log.error("error occured while validating response fields: {}", ex.getMessage());
        }
        return null;

    }
}
