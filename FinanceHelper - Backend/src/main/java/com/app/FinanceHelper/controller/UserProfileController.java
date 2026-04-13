package com.app.FinanceHelper.controller;

import com.app.FinanceHelper.model.UserProfile;
import com.app.FinanceHelper.payload.response.UserProfileResponse;
import com.app.FinanceHelper.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserProfileController {

    @Autowired
    UserProfileService userProfileService;


    @GetMapping()
    public ResponseEntity<UserProfileResponse> getUser(
            @AuthenticationPrincipal UserProfile user
    ){
        UserProfileResponse userProfileResponse = userProfileService.getUser(user.getId());
        return new ResponseEntity<>(userProfileResponse, HttpStatus.OK);
    }

    @GetMapping("/userByName/{name}")
    public ResponseEntity<UserProfileResponse> getUserByName(
            @PathVariable String name
    ){
        UserProfileResponse userProfileResponse = userProfileService.getUserByName(name);
        return new ResponseEntity<>(userProfileResponse, HttpStatus.OK);
    }

    @GetMapping("/allUsers")
    public ResponseEntity<Set<UserProfileResponse>> getAllUsers(){
        Set<UserProfileResponse> userProfileResponse = userProfileService.getAllUsers();
        return new ResponseEntity<>(userProfileResponse, HttpStatus.OK);
    }

    @PutMapping("/name")
    public ResponseEntity<UserProfileResponse> updateName(
            @AuthenticationPrincipal UserProfile user,
            @RequestParam String newName
    ) {
        UserProfileResponse userProfileResponse = userProfileService.updateName(user.getId(), newName);
        return new ResponseEntity<>(userProfileResponse, HttpStatus.OK);
    }


    @DeleteMapping()
    public ResponseEntity<UserProfileResponse> deleteUser(
            @PathVariable UUID userID
    ){
        UserProfileResponse userProfileResponse = userProfileService.deleteUserById(userID);
        return new ResponseEntity<>(userProfileResponse, HttpStatus.OK);
    }
}
