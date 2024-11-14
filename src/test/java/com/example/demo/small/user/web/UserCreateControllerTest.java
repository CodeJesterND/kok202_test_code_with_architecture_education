package com.example.demo.small.user.web;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.example.demo.small.mock.TestContainer;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.web.request.UserCreate;
import com.example.demo.user.web.response.UserResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

class UserCreateControllerTest {

    @Test
    void 사용자는_회원_가입을_할_수있고_회원가입된_사용자는_PENDING_상태이다() throws Exception {
        // given
        TestContainer testContainer = TestContainer.builder()
                .uuidHolder(() -> "aaaaaaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .build();

        UserCreate userCreate = UserCreate.builder()
                .email("dev.hyoseung@gmail.com")
                .nickname("hyoseung")
                .address("Suwon")
                .build();

        // when
        ResponseEntity<UserResponse> result = testContainer.userCreateController.createUser(userCreate);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getEmail()).isEqualTo("dev.hyoseung@gmail.com");
        assertThat(result.getBody().getNickname()).isEqualTo("hyoseung");
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(result.getBody().getLastLoginAt()).isNull();
        assertThat(testContainer.userRepository.getById(1).getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaaaaaaaaaa");
    }

}