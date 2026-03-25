package com.app.FinanceHelper.service;

import com.app.FinanceHelper.payload.dto.UserProfileDTO;
import com.app.FinanceHelper.payload.response.UserProfileResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface UserProfileService {
    UserProfileResponse createUser(UserProfileDTO userProfileDTO);

    UserProfileResponse getUserById(UUID userID);

    UserProfileResponse getUserByName(String name);
}
