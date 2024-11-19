package com.projectmanagement.controllers;

import com.projectmanagement.configs.JwtProvider;
import com.projectmanagement.dto.UserDTO;
import com.projectmanagement.models.User;
import com.projectmanagement.repositories.UserRepository;
import com.projectmanagement.dto.requests.LoginRequest;
import com.projectmanagement.dto.responses.AuthResponse;
import com.projectmanagement.services.CustomUserDetailsImpl;
import com.projectmanagement.services.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserDetailsImpl customUserDetails;

    @Autowired
    private SubscriptionService subscriptionService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> createUserHandler(
            @Validated @RequestBody UserDTO userDTO, BindingResult result) throws Exception {

        // Kiểm tra nếu có lỗi validation
        if (result.hasErrors()) {
            String errorMessages = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .reduce((msg1, msg2) -> msg1 + ", " + msg2)
                    .orElse("Invalid data");
            return new ResponseEntity<>(new AuthResponse(errorMessages), HttpStatus.BAD_REQUEST);
        }

        // Kiểm tra xem email đã tồn tại hay chưa
        User isUserExist = userRepository.findByEmail(userDTO.getEmail());
        if (isUserExist != null) {
            throw new Exception("Email already exist with another account!");
        }

        // Tạo đối tượng User mới từ UserDTO
        User createUser = new User();
        createUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        createUser.setEmail(userDTO.getEmail());
        createUser.setFullName(userDTO.getFullName());

        // Lưu user vào cơ sở dữ liệu
        User savedUser = userRepository.save(createUser);

        // Tạo subscription cho user
        subscriptionService.createSubscription(savedUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = JwtProvider.generateToken(authentication);

        AuthResponse res = new AuthResponse();
        res.setMessage("Register success!");
        res.setJwt(jwt);

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> signin(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        Authentication authentication = authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = JwtProvider.generateToken(authentication);

        AuthResponse res = new AuthResponse();
        res.setMessage("Login success!");
        res.setJwt(jwt);

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = customUserDetails.loadUserByUsername(username);

        if(username==null) {
            throw new BadCredentialsException("Invalid username!");
        }
        if(!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid password!");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
