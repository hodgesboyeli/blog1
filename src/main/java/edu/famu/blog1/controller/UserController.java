package edu.famu.blog1.controller;

import com.google.api.client.util.Value;
import edu.famu.blog1.model.User;
import edu.famu.blog1.service.UserService;
import edu.famu.blog1.util.ErrorMessage;
import edu.famu.blog1.util.ResponseWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private UserService userService;
    @Value("${response.status}")
    private int statusCode;
    @Value("${response.name}")
    private String name;
    private Object payload;
    private ResponseWrapper response;
    private static final String CLASS_NAME = "UserService";
    public UserController(UserService userService) {
        this.userService = userService;
        payload = null;
    }

    @GetMapping("/")
    public ResponseEntity<Map<String,Object>> getAllUsers(){
        try {
            payload = userService.getUsers();
            statusCode = 200;
            name = "users";
        } catch (ExecutionException | InterruptedException e) {
            payload = new ErrorMessage("Cannot fetch users from database", CLASS_NAME,
                    e.getStackTrace().toString());
        }

        response = new ResponseWrapper(statusCode, name, payload);
        return response.getResponse();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Map<String,Object>> getUser(@PathVariable(name = "userId") String id){
        try {
            payload = userService.getUserById(id);
            statusCode = 200;
            name = "user";
        } catch (ExecutionException | InterruptedException e) {
            payload = new ErrorMessage("Cannot fetch user with id" + id + " from database", CLASS_NAME,
                    e.getStackTrace().toString());
        }

        response = new ResponseWrapper(statusCode, name, payload);
        return response.getResponse();
    }

    @PostMapping("/")
    public ResponseEntity<Map<String,Object>> addUser(@RequestBody User user){
        try {
            payload = userService.createUser(user);
            statusCode = 200;
            name = "userId";
        } catch (ExecutionException | InterruptedException e) {
            payload = new ErrorMessage("Cannot create user in database", CLASS_NAME,
                    e.getStackTrace().toString());
        }

        response = new ResponseWrapper(statusCode, name, payload);
        return response.getResponse();
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Map<String,Object>> updateUser
            (@PathVariable(name = "userId") String id, @RequestBody Map<String,String> updateValues){
        try {
            userService.updateUser(id, updateValues);
            payload = "User successfully updated with id " + id;
            statusCode = 201;
            name = "message";
        } catch (Exception e) {
            payload = new ErrorMessage("Cannot update user with id " + id + " in database", CLASS_NAME,
                    e.getStackTrace().toString());
        }

        response = new ResponseWrapper(statusCode, name, payload);
        return response.getResponse();
    }


}
