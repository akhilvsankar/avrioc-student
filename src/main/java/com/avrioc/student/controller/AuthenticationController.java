package com.avrioc.student.controller;

import com.avrioc.student.security.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Controller class for login functionality with spring security.
 * generates jwt token for a user
 */
@AllArgsConstructor
@RestController
public class AuthenticationController {

    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private CustomPasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;

    /**
     * validates password for the username received
     * If validation failed, return 401 unauthorized,else generates jwt token
     *
     * @param ar
     * @return Mono publisher of AuthResponse Entity
     */
    @PostMapping("/login")
    public Mono<ResponseEntity<AuthResponse>> login(@RequestBody AuthRequest ar) {
        return userService.findByUsername(ar.getUsername())
                .filter(userDetails -> passwordEncoder.encode(ar.getPassword()).equals(userDetails.getPassword()))
                .map(userDetails -> ResponseEntity.ok(new AuthResponse(jwtUtil.generateToken(userDetails))))
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()));
    }

}
