package com.app.FinanceHelper.controller;

import com.app.FinanceHelper.payload.dto.UserProfileDTO;
import com.app.FinanceHelper.payload.response.UserProfileResponse;
import com.app.FinanceHelper.service.UserProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/public/social")
public class UserProfileController {

    @Autowired
    UserProfileService userProfileService;

    @PostMapping("/user")
    public ResponseEntity<UserProfileResponse> createUser(
            @Valid @RequestBody UserProfileDTO userProfileDTO
    ){
        UserProfileResponse userProfileResponse = userProfileService.createUser(userProfileDTO);
        return new ResponseEntity<>(userProfileResponse, HttpStatus.CREATED);
    }

    @GetMapping("/users/{userID}")
    public ResponseEntity<UserProfileResponse> getUser(
            @PathVariable UUID userID
    ){
        UserProfileResponse userProfileResponse = userProfileService.getUser(userID);
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
    public ResponseEntity<List<UserProfileResponse>> getAllUsers(
            @PathVariable UUID userID
    ){
        List<UserProfileResponse> userProfileResponse = userProfileService.getAllUsers();
        return new ResponseEntity<>(userProfileResponse, HttpStatus.OK);
    }

    @PatchMapping("/users/{userID}/name")
    public ResponseEntity<UserProfileResponse> updateName(
            @PathVariable UUID userID,
            @RequestParam String newName
    ) {
        UserProfileResponse userProfileResponse = userProfileService.updateName(userID, newName);
        return new ResponseEntity<>(userProfileResponse, HttpStatus.OK);
    }


    @DeleteMapping("/users/{userID}")
    public ResponseEntity<UserProfileResponse> deleteUser(
            @PathVariable UUID userID
    ){
        UserProfileResponse userProfileResponse = userProfileService.deleteUserById(userID);
        return new ResponseEntity<>(userProfileResponse, HttpStatus.OK);
    }
}
