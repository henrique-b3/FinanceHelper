package com.app.FinanceHelper.controller;

import com.app.FinanceHelper.payload.dto.UserProfileDTO;
import com.app.FinanceHelper.payload.response.UserProfileResponse;
import com.app.FinanceHelper.service.UserProfileService;
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
            @RequestBody UserProfileDTO userProfileDTO
            ){
        UserProfileResponse createdUser = userProfileService.createUser(userProfileDTO);
        return new ResponseEntity<UserProfileResponse>(createdUser, HttpStatus.CREATED);
    }

    @GetMapping("/users/{userID}")
    public ResponseEntity<UserProfileResponse> getUser(
            @PathVariable UUID userID
    ){
        UserProfileResponse foundUser = userProfileService.getUserById(userID);
        return new ResponseEntity<UserProfileResponse>(foundUser, HttpStatus.OK);
    }

    @GetMapping("/userByName/{name}")
    public ResponseEntity<UserProfileResponse> getUserByName(
            @PathVariable String name
    ){
        UserProfileResponse foundUser = userProfileService.getUserByName(name);
        return new ResponseEntity<UserProfileResponse>(foundUser, HttpStatus.OK);
    }

    @GetMapping("/allUsers")
    public ResponseEntity<List<UserProfileResponse>> getAllUsers(
            @PathVariable UUID userID
    ){
        List<UserProfileResponse> allUsers = userProfileService.getAllUsers();
        return new ResponseEntity<List<UserProfileResponse>>(allUsers, HttpStatus.OK);
    }

    @PatchMapping("/users/{userID}/name")
    public ResponseEntity<UserProfileResponse> updateName(
            @PathVariable UUID userID,
            @RequestBody String newName
    ) {
        UserProfileResponse updatedUser = userProfileService.updateName(userID, newName);
        return ResponseEntity.ok(updatedUser);
    }


    @DeleteMapping("/users/{userID}")
    public ResponseEntity<UserProfileResponse> deleteUser(
            @PathVariable UUID userID
    ){
        UserProfileResponse deletedUser = userProfileService.deleteUserById(userID);
        return new ResponseEntity<UserProfileResponse>(deletedUser, HttpStatus.OK);
    }
}
