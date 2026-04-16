package com.app.FinanceHelper.service;

import com.app.FinanceHelper.payload.dto.UserProfileDTO;
import com.app.FinanceHelper.payload.response.UserProfileResponse;

public interface AuthenticationService {
    UserProfileResponse createUser(UserProfileDTO userProfileDTO);
}
