package com.app.FinanceHelper.service;

import com.app.FinanceHelper.payload.dto.UserProfileDTO;
import com.app.FinanceHelper.payload.response.UserProfileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


public interface UserProfileService {
    UserProfileResponse createUser(UserProfileDTO userProfileDTO);

    UserProfileResponse getUserById(UUID userID);

    UserProfileResponse getUserByName(String name);

    List<UserProfileResponse> getAllUsers();

    UserProfileResponse deleteUserById(UUID userID);

    UserProfileResponse updateName(UUID userID, String newName);

}
