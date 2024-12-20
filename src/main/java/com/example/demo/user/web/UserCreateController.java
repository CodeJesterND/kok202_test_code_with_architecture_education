package com.example.demo.user.web;

import com.example.demo.user.web.port.UserCreateService;
import com.example.demo.user.web.request.UserCreate;
import com.example.demo.user.web.response.UserResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "유저(users)")
@Builder
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserCreateController {

    private final UserCreateService userCreateService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody UserCreate userCreate) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(UserResponse.from(userCreateService.create(userCreate)));
    }

}