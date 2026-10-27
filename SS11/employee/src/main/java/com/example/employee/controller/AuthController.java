package com.example.employee.controller;

import com.example.employee.dto.ActiveRequest;
import com.example.employee.dto.JwtResponse;
import com.example.employee.dto.LoginRequest;
import com.example.employee.dto.RegisterRequest;
import com.example.employee.model.User;
import com.example.employee.repository.UserRepository;
import com.example.employee.security.JwtProvider;
import com.example.employee.security.UserPrincipal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username đã tồn tại!");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setRole("USER");
        user.setEnabled(true);

        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtProvider.generateToken(authentication);
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

            return ResponseEntity.ok(new JwtResponse(jwt, "Bearer", userPrincipal.getUsername()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("username or password incorrect");
        }
    }

    @PostMapping("/active-user")
    public ResponseEntity<?> activeUser(@RequestBody ActiveRequest request) {
        User user = userRepository.findByUsername(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"));

        if (user.getOtpCode() == null || !user.getOtpCode().equals(request.getOtp())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mã OTP không chính xác");
        }

        if (user.getOtpExpiration().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mã OTP đã hết hạn");
        }

        user.setEnabled(true);
        user.setOtpCode(null);
        user.setOtpExpiration(null);
        userRepository.save(user);

        return ResponseEntity.ok("Kích hoạt tài khoản thành công!");
    }
}
