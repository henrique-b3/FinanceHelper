package com.app.FinanceHelper.controller;

import com.app.FinanceHelper.model.UserProfile;
import com.app.FinanceHelper.payload.dto.AuthenticationDTO;
import com.app.FinanceHelper.payload.dto.UserProfileDTO;
import com.app.FinanceHelper.payload.response.LoginResponse;
import com.app.FinanceHelper.payload.response.UserProfileResponse;
import com.app.FinanceHelper.security.TokenService;
import com.app.FinanceHelper.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    AuthenticationService authService;

    @Autowired
    TokenService tokenService;

    @PostMapping("/register")
    public ResponseEntity<UserProfileResponse> register(
            @RequestBody @Valid UserProfileDTO userProfileDTO //TODO validar entre UserProfileDTO ou RegisterDTO
    ){
        UserProfileResponse userProfileResponse = authService.createUser(userProfileDTO);

        return new ResponseEntity<>(userProfileResponse, HttpStatus.CREATED);
    }


    @PostMapping("/login")
    public ResponseEntity login(
            @RequestBody @Valid AuthenticationDTO data
    ){
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var auth = authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((UserProfile) auth.getPrincipal());

        return new ResponseEntity<>(new LoginResponse(token), HttpStatus.OK);
    }
}
