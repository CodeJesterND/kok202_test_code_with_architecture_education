package com.example.demo.small.user.web;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.small.mock.TestContainer;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.web.request.UserUpdate;
import com.example.demo.user.web.response.MyProfileResponse;
import com.example.demo.user.web.response.UserResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;

import org.springframework.http.ResponseEntity;

class UserControllerTest {

    @Test
    void 사용자는_특정_유저의_정보를_개인정보는_소거된채_전달_받을_수_있다() throws Exception {
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();

        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("dev.hyoseung@gmail.com")
                .nickname("hyoseung")
                .address("Suwon")
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(100L)
                .build()
        );

        // when
        ResponseEntity<UserResponse> result = testContainer.userController.getUserById(1);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getEmail()).isEqualTo("dev.hyoseung@gmail.com");
        assertThat(result.getBody().getNickname()).isEqualTo("hyoseung");
        assertThat(result.getBody().getLastLoginAt()).isEqualTo(100);
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void 사용자는_존재하지_않는_유저의_아이디로_api_호출할_경우_404_응답을_받는다() throws Exception {
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();

        // when
        // then
        assertThatThrownBy(() -> {
            testContainer.userController.getUserById(1);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void 사용자는_인증_코드로_계정을_활성화_시킬_수_있다() throws Exception {
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();

        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("dev.hyoseung@gmail.com")
                .nickname("hyoseung")
                .address("Suwon")
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .status(UserStatus.PENDING)
                .lastLoginAt(100L)
                .build()
        );

        // when
        ResponseEntity<Void> result = testContainer.userController.verifyEmail(1, "aaaaaaaa-aaaa-aaaa-aaaaaaaaaaaa");

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(302));
        assertThat(testContainer.userRepository.getById(1).getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void 사용자는_인증_코드가_일치하지_않을_경우_권한_없음_에러를_내려준다() throws Exception {
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();

        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("dev.hyoseung@gmail.com")
                .nickname("hyoseung")
                .address("Suwon")
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .status(UserStatus.PENDING)
                .lastLoginAt(100L)
                .build()
        );

        // when
        // then
        assertThatThrownBy(() -> {
            testContainer.userController.verifyEmail(1, "aaaaaaaa-aaaa-aaaa-aaaaaaaaaaab");
        }).isInstanceOf(CertificationCodeNotMatchedException.class);
    }

    @Test
    void 사용자는_내_정보를_불러올_때_개인정보인_주소도_갖고_올_수_있다() throws Exception {
        // given
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(() -> 1678350673958L)
                .build();

        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("dev.hyoseung@gmail.com")
                .nickname("hyoseung")
                .address("Suwon")
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(100L)
                .build()
        );

        // when
        ResponseEntity<MyProfileResponse> result = testContainer.userController.getMyInfo("dev.hyoseung@gmail.com");

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getEmail()).isEqualTo("dev.hyoseung@gmail.com");
        assertThat(result.getBody().getNickname()).isEqualTo("hyoseung");
        assertThat(result.getBody().getLastLoginAt()).isEqualTo(1678350673958L);
        assertThat(result.getBody().getAddress()).isEqualTo("Suwon");
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void 사용자는_내_정보를_수정할_수_있다() throws Exception {
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();

        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("dev.hyoseung@gmail.com")
                .nickname("hyoseung")
                .address("Suwon")
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(1678350673958L)
                .build()
        );

        // when
        ResponseEntity<MyProfileResponse> result = testContainer.userController.updateMyInfo(
                "dev.hyoseung@gmail.com",
                UserUpdate.builder()
                        .nickname("hyoseung-n")
                        .address("Pangyo")
                        .build()
        );

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getEmail()).isEqualTo("dev.hyoseung@gmail.com");
        assertThat(result.getBody().getNickname()).isEqualTo("hyoseung-n");
        assertThat(result.getBody().getLastLoginAt()).isEqualTo(1678350673958L);
        assertThat(result.getBody().getAddress()).isEqualTo("Pangyo");
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

}