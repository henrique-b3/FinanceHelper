package com.app.FinanceHelper.service;

import com.app.FinanceHelper.payload.response.UserProfileResponse;

import java.util.Set;
import java.util.UUID;


public interface UserProfileService {

    UserProfileResponse getUser(UUID userID);

    UserProfileResponse getUserByName(String name);

    Set<UserProfileResponse> getAllUsers();

    UserProfileResponse deleteUserById(UUID userID);

    UserProfileResponse updateName(UUID userID, String newName);

}
