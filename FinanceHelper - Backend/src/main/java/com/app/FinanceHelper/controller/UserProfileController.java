package com.app.FinanceHelper.controller;

import com.app.FinanceHelper.payload.dto.UserProfileDTO;
import com.app.FinanceHelper.payload.response.UserProfileResponse;
import com.app.FinanceHelper.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class UserProfileController {

    @Autowired
    UserProfileService userProfileService;

    @PostMapping("/api/public/social/users")
    public ResponseEntity<UserProfileResponse> createUser(
            @RequestBody UserProfileDTO userProfileDTO
            ){
        UserProfileResponse createdUser = userProfileService.createUser(userProfileDTO);
        return new ResponseEntity<UserProfileResponse>(createdUser, HttpStatus.CREATED);
    }

    @GetMapping("/api/public/social/getUser")
    public ResponseEntity<UserProfileResponse> getUser(
            @RequestHeader UUID userID
    ){
        UserProfileResponse foundUser = userProfileService.getUserById(userID);
        return new ResponseEntity<UserProfileResponse>(foundUser, HttpStatus.OK);
    }

    @GetMapping("/api/public/social/getUser")
    public ResponseEntity<UserProfileResponse> getUserByName(
            @RequestHeader String name
    ){
        UserProfileResponse foundUser = userProfileService.getUserByName(name);
        return new ResponseEntity<UserProfileResponse>(foundUser, HttpStatus.OK);
    }

    @GetMapping("/api/public/social/getAllUsers")
    public ResponseEntity<UserProfileResponse> getAllUsers(){
        return null;
    }
}
