package com.app.FinanceHelper.service.impl;

import com.app.FinanceHelper.model.UserProfile;
import com.app.FinanceHelper.payload.dto.UserProfileDTO;
import com.app.FinanceHelper.payload.response.UserProfileResponse;
import com.app.FinanceHelper.repository.UserProfileRepository;
import com.app.FinanceHelper.service.UserProfileService;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    @Autowired
    UserProfileRepository userProfileRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public UserProfileResponse createUser(UserProfileDTO userProfileDTO) {

        if (userProfileDTO.getId() != null && userProfileRepository.existsById(userProfileDTO.getId())) {
            throw new RuntimeException("UserProfile com este ID ja existe");
        }

        UserProfile userToSave = modelMapper.map(userProfileDTO, UserProfile.class);

        UserProfile savedUser = userProfileRepository.save(userToSave);

        return modelMapper.map(savedUser, UserProfileResponse.class);
    }

    @Override
    public UserProfileResponse getUserById(UUID userID) {

        UserProfile userProfile = userProfileRepository.findById(userID).orElseThrow(()
                -> new ResourceNotFoundException("UserProfile","userID", userID));


        return modelMapper.map(userProfile, UserProfileResponse.class);
    }

    @Override
    public UserProfileResponse getUserByName(String name) {

        UserProfile userProfile = userProfileRepository.findByName(name).orElseThrow(()
                -> new ResourceNotFoundException("UserProfile","userID", name));

        return modelMapper.map(userProfile, UserProfileResponse.class);
    }
}
