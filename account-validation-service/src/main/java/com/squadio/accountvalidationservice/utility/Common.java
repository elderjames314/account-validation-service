package com.squadio.accountvalidationservice.utility;

import com.squadio.accountvalidationservice.authentication.util.JwtUtil;
import com.squadio.accountvalidationservice.model.AppResponse;
import com.squadio.accountvalidationservice.model.NormalResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * @author jamesoladimeji
 * @created 19/12/2021 - 12:32 PM
 * @project IntelliJ IDEA
 */
@Service
@Slf4j
public class Common {

   public String hashedString(String txt) {
       String hashed = null;
       try {
           MessageDigest digest = MessageDigest.getInstance("SHA-256");
           byte[] hash = digest.digest(txt.getBytes(StandardCharsets.UTF_8));
           hashed = Base64.getEncoder().encodeToString(hash);
       }catch (Exception ex) {
           ex.printStackTrace();
           log.info("Error occured while trying to hash "+ txt);
       }
       return hashed;
   }

    public String getLoggedInUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = null;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        return username;
    }

    public boolean isAdmin() {
        return "Admin".equals(getLoggedInUser());
    }

    public ResponseEntity<AppResponse> checkLoggedInUser(String username) {
        String loggedInUser = getLoggedInUser();
        if(!loggedInUser.equals(username)) {
            NormalResponse response = new NormalResponse();
            response.setResponseMessage("You do not have required privilege to perform this operation");
            AppResponse appResponse = new AppResponse();
            appResponse.setResponse(response);
            return new ResponseEntity(response, HttpStatus.UNAUTHORIZED);
        }
        return null;
    }




    public  String getClientIp(HttpServletRequest request) {
        String remoteAddr = "";
        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }
        return remoteAddr;
    }


}
