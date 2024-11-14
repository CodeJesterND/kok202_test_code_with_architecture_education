package com.example.demo.small.user.web.response;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.web.response.MyProfileResponse;
import org.junit.jupiter.api.Test;

class MyProfileResponseTest {

    @Test
    public void User으로_프로필_응답을_생성할_수_있다() {
        // given
        User user = User.builder()
                .id(1L)
                .email("dev.hyoseung@gamil.com")
                .nickname("hyoseung")
                .address("Suwon")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(100L)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .build();

        // when
        MyProfileResponse myProfileResponse = MyProfileResponse.from(user);

        // then
        assertThat(myProfileResponse.getId()).isEqualTo(1L);
        assertThat(myProfileResponse.getEmail()).isEqualTo("dev.hyoseung@gamil.com");
        assertThat(myProfileResponse.getNickname()).isEqualTo("hyoseung");
        assertThat(myProfileResponse.getAddress()).isEqualTo("Suwon");
        assertThat(myProfileResponse.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(myProfileResponse.getLastLoginAt()).isEqualTo(100L);
    }

}