package com.app.FinanceHelper.service.impl;

import com.app.FinanceHelper.exceptions.APIexception;
import com.app.FinanceHelper.exceptions.ResourceNotFoundException;
import com.app.FinanceHelper.model.UserProfile;
import com.app.FinanceHelper.payload.dto.UserProfileDTO;
import com.app.FinanceHelper.payload.response.UserProfileResponse;
import com.app.FinanceHelper.repository.UserProfileRepository;
import com.app.FinanceHelper.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    UserProfileRepository userProfileRepository;
    ModelMapper modelMapper;

    @Override
    public UserProfileResponse createUser(UserProfileDTO userProfileDTO) {

        if (userProfileDTO.getId() != null && userProfileRepository.existsById(userProfileDTO.getId())) {
            throw new APIexception("UserProfile already exists");
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

    @Override
    public List<UserProfileResponse> getAllUsers() {
        return userProfileRepository.findAll()
                .stream()
                .map(userProfile -> modelMapper.map(userProfile, UserProfileResponse.class))
                .toList();
    }

    @Override
    public UserProfileResponse updateName(UUID userID, String newName) {
        UserProfile userProfile = userProfileRepository.findById(userID)
                .orElseThrow(() -> new ResourceNotFoundException("UserProfile", "userID", userID));

        userProfile.setName(newName);
        UserProfile updatedUser = userProfileRepository.save(userProfile);

        return modelMapper.map(updatedUser, UserProfileResponse.class);
    }


    @Override
    public UserProfileResponse deleteUserById(UUID userID) {

        UserProfile userProfile = userProfileRepository.findById(userID).orElseThrow(()
                -> new ResourceNotFoundException("UserProfile","userID", userID));

        userProfileRepository.deleteById(userID);

        return modelMapper.map(userProfile, UserProfileResponse.class);
    }


}
