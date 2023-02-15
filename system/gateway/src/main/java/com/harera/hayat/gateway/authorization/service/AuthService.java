package com.harera.hayat.gateway.authorization.service;

import java.util.Objects;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.harera.hayat.common.service.auth.JwtService;
import com.harera.hayat.common.service.auth.UserUtils;
import com.harera.hayat.gateway.authorization.model.auth.FirebaseOauthToken;
import com.harera.hayat.gateway.authorization.model.auth.LoginRequest;
import com.harera.hayat.gateway.authorization.model.auth.LoginResponse;
import com.harera.hayat.gateway.authorization.model.auth.LogoutRequest;
import com.harera.hayat.gateway.authorization.model.auth.OAuthLoginRequest;
import com.harera.hayat.gateway.authorization.model.auth.SignupRequest;
import com.harera.hayat.gateway.authorization.model.auth.SignupResponse;
import com.harera.hayat.gateway.authorization.model.user.AppFirebaseToken;
import com.harera.hayat.gateway.authorization.model.user.AppFirebaseUser;
import com.harera.hayat.gateway.authorization.model.user.User;
import com.harera.hayat.gateway.authorization.repository.user.TokenRepository;
import com.harera.hayat.gateway.authorization.repository.user.UserRepository;
import com.harera.hayat.gateway.authorization.service.auth.JwtUtils;

import io.jsonwebtoken.JwtException;

@Service
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthValidation authValidation;
    private final TokenRepository tokenRepository;
    private final FirebaseService firebaseService;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;
    private final JwtUtils jwtUtils;
    private final UserUtils userUtils;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                    JwtUtils jwtUtils, AuthValidation authValidation,
                    TokenRepository tokenRepository, FirebaseService firebaseService,
                    ModelMapper modelMapper, JwtService jwtService, UserUtils userUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authValidation = authValidation;
        this.tokenRepository = tokenRepository;
        this.firebaseService = firebaseService;
        this.modelMapper = modelMapper;
        this.jwtService = jwtService;
        this.jwtUtils = jwtUtils;
        this.userUtils = userUtils;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        authValidation.validateLogin(loginRequest);
        User user = userUtils.getUser(loginRequest.getSubject());

        if (!Objects.equals(user.getDeviceToken(), loginRequest.getDeviceToken())) {
            user.setDeviceToken(loginRequest.getDeviceToken());
            userRepository.save(user);
        }

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtService.generateToken(user));
        loginResponse.setRefreshToken(jwtService.generateRefreshToken(user));

        return loginResponse;
    }

    public LoginResponse login(OAuthLoginRequest oAuthLoginRequest) {
        authValidation.validate(oAuthLoginRequest);
        AppFirebaseToken firebaseToken =
                        firebaseService.getToken(oAuthLoginRequest.getFirebaseToken());
        AppFirebaseUser userRecord = firebaseService.getUser(firebaseToken.getUid());
        User user = userRepository.findByUid(userRecord.getUid()).orElseThrow(
                        () -> new UsernameNotFoundException("User not found"));
        return new LoginResponse(jwtService.generateToken(user),
                        jwtService.generateRefreshToken(user));
    }

    public SignupResponse signup(SignupRequest signupRequest) {
        authValidation.validate(signupRequest);
        AppFirebaseToken firebaseToken =
                        firebaseService.getToken(signupRequest.getFirebaseToken());
        AppFirebaseUser firebaseUser = firebaseService.getUser(firebaseToken.getUid());

        authValidation.validate(firebaseUser, signupRequest);
        User user = modelMapper.map(signupRequest, User.class);
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setUid(firebaseUser.getUid());
        user.setUsername(firebaseUser.getUid());
        userRepository.save(user);

        return modelMapper.map(user, SignupResponse.class);
    }

    public LoginResponse refresh(String refreshToken) {
        String usernameOrMobile = jwtUtils.extractUserSubject(refreshToken);
        final User user = (User) loadUserByUsername(usernameOrMobile);
        jwtUtils.validateRefreshToken(user, refreshToken);
        LoginResponse authResponse = new LoginResponse();
        authResponse.setToken(jwtService.generateToken(user));
        authResponse.setRefreshToken(jwtService.generateRefreshToken(user));
        return authResponse;
    }

    public FirebaseOauthToken generateFirebaseToken(LoginRequest loginRequest) {
        authValidation.validateLogin(loginRequest);
        User user = userUtils.getUser(loginRequest.getSubject());
        String s = firebaseService.generateToken(user.getUid());
        return new FirebaseOauthToken(s);
    }

    public void logout(LogoutRequest logoutRequest) {
        String usernameOrMobile = jwtUtils.extractUserSubject(logoutRequest.getToken());
        final User user = (User) loadUserByUsername(usernameOrMobile);
        if (StringUtils.isNotEmpty(user.getDeviceToken())) {
            user.setDeviceToken(null);
            userRepository.save(user);
        }
        jwtUtils.validateToken(user, logoutRequest.getToken());
        tokenRepository.removeUserToken(logoutRequest.getToken());
        if (StringUtils.isNotEmpty(logoutRequest.getRefreshToken())) {
            jwtUtils.validateRefreshToken(user, logoutRequest.getRefreshToken());
            tokenRepository.removeUserRefreshToken(logoutRequest.getRefreshToken());
        }
    }

    public User getRequestUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication()
                        .getPrincipal();
        if (principal instanceof User user)
            return user;
        throw new JwtException("Invalid token");
    }

    @Override
    public UserDetails loadUserByUsername(String username)
                    throws UsernameNotFoundException {
        try {
            long userId = Integer.parseInt(username);
            return userRepository.findById(userId).orElse(null);
        } catch (Exception e) {
            throw new UsernameNotFoundException("User not found");
        }
    }
}