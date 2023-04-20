package edu.famu.blog1.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import edu.famu.blog1.model.User;
import edu.famu.blog1.security.FirebaseUserDetails;
import edu.famu.blog1.service.UserService;
import edu.famu.blog1.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api")
public class AuthenticationController {

    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final AuthenticationManager authenticationManager;

    @Value("${response.status}")
    private int statusCode;
    @Value("${response.name}")
    private String name;
    private Object payload;
    private ResponseWrapper response;
    private static final String CLASS_NAME = "Authentication";


    private final Log logger = LogFactory.getLog(this.getClass());

    public AuthenticationController(AuthenticationManager authenticationManager) {

        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public String register(@RequestBody RegistrationRequest registrationRequest) throws FirebaseAuthException {
        UserRecord.CreateRequest createRequest = new UserRecord.CreateRequest()
                .setEmail(registrationRequest.getEmail())
                .setPassword(registrationRequest.getPassword())
                .setDisplayName(registrationRequest.getDisplayName());
        UserRecord userRecord = firebaseAuth.createUser(createRequest);
        return "User created with UID: " + userRecord.getUid();
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequest loginRequest) throws FirebaseAuthException {
        String token = "";
        HttpHeaders headers = new HttpHeaders();

        try {
            UserRecord userRecord = firebaseAuth.getUserByEmail(loginRequest.getEmail());
            //logger.info(userRecord.getUid());

            UserDetails userDetails = new FirebaseUserDetails(userRecord);
            //logger.info(userDetails);
            token = JwtUtil.generateToken(userDetails);
            //logger.info(token);
            UserService service = new UserService();
            User user = service.getUserByUid(userRecord.getUid());
            payload = user;
            service.updateLastLogin(user.getUserId() );

            statusCode = 200;
            name = "user";
            headers.add("X-Auth-Token", token);
            Instant now = Instant.now();
            Instant expiryDate = now.plus(1, ChronoUnit.HOURS);
            headers.add("Expires", String.valueOf(expiryDate.toEpochMilli()));

        } catch (ExecutionException | InterruptedException e) {

            payload = new ErrorMessage("Error signing in", CLASS_NAME, e.toString());
        }

        response = new ResponseWrapper(statusCode, name, payload, headers);

        return response.getResponse();
    }


    @GetMapping("/logout")
    public String logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
        return "Logged out successfully";
    }
}
