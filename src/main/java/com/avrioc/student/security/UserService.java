package com.avrioc.student.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    private Map<String, User> data;

    @Value("${user.password}")
    private String userPwd;

    @Value("${admin.password}")
    private String adminPwd;


    @PostConstruct
    public void init() {
        data = new HashMap<>();
        data.put("user", new User("user", userPwd, true, List.of(Role.ROLE_USER)));
        data.put("admin", new User("admin", adminPwd, true, List.of(Role.ROLE_ADMIN)));
    }

    public Mono<User> findByUsername(String username) {
        return Mono.justOrEmpty(data.get(username));
    }
}
