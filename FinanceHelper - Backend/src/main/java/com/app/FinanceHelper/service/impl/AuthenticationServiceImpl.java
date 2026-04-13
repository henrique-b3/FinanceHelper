package com.app.FinanceHelper.service.impl;

import com.app.FinanceHelper.enums.UserRole;
import com.app.FinanceHelper.exceptions.APIexception;
import com.app.FinanceHelper.model.UserProfile;
import com.app.FinanceHelper.payload.dto.UserProfileDTO;
import com.app.FinanceHelper.payload.response.UserProfileResponse;
import com.app.FinanceHelper.repository.UserProfileRepository;
import com.app.FinanceHelper.service.AuthenticationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    UserProfileRepository userProfileRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public UserProfileResponse createUser(UserProfileDTO userProfileDTO) {
        if (userProfileRepository.findByEmail(userProfileDTO.getEmail()) != null) {
            throw new APIexception("User already exists with that email: " + userProfileDTO.getEmail());
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(userProfileDTO.getPassword());

        UserProfile newUser = modelMapper.map(userProfileDTO, UserProfile.class);
        newUser.setPassword(encryptedPassword);
        newUser.setRole(UserRole.USER);

        UserProfile savedUser = userProfileRepository.save(newUser);

        return modelMapper.map(savedUser, UserProfileResponse.class);
    }

}
