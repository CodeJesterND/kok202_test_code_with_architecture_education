package com.example.demo.user.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
import com.example.demo.user.web.request.UserCreate;
import com.example.demo.user.web.request.UserUpdate;
import org.junit.jupiter.api.Test;

public class UserTest {

    @Test
    public void UserCreate_객체로_생성할_수_있다() {
        // given
        UserCreate userCreate = UserCreate.builder()
                .email("dev.hyoseung@gmail.com")
                .nickname("hyoseung")
                .address("Suwon")
                .build();

        // when
        User user = User.from(userCreate, new TestUuidHolder("aaaaaaaa-aaaa-aaaa-aaaaaaaaaaaa"));

        // then
        assertThat(user.getId()).isNull();
        assertThat(user.getEmail()).isEqualTo("dev.hyoseung@gmail.com");
        assertThat(user.getNickname()).isEqualTo("hyoseung");
        assertThat(user.getAddress()).isEqualTo("Suwon");
        assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(user.getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaaaaaaaaaa");
    }

    @Test
    public void UserUpdate_객체로_데이터를_업데이트_할_수_있다() {
        // given
        User user = User.builder()
                .id(1L)
                .email("dev.hyoseung@gmail.com")
                .nickname("hyoseung")
                .address("Suwon")
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(100L)
                .build();

        UserUpdate userUpdate = UserUpdate.builder()
                .nickname("hyoseung-n")
                .address("Pangyo")
                .build();

        // when
        user = user.update(userUpdate);

        // then
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getEmail()).isEqualTo("dev.hyoseung@gmail.com");
        assertThat(user.getNickname()).isEqualTo("hyoseung-n");
        assertThat(user.getAddress()).isEqualTo("Pangyo");
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(user.getLastLoginAt()).isEqualTo(100L);
        assertThat(user.getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaaaaaaaaaa");
    }

    @Test
    public void 로그인을_할_수_있고_로그인시_마지막_로그인_시간이_변경된다() {
        // given
        User user = User.builder()
                .id(1L)
                .email("dev.hyoseung@gmail.com")
                .nickname("hyoseung")
                .address("Suwon")
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(100L)
                .build();

        // when
        user = user.login(new TestClockHolder(1678350673958L));

        // then
        assertThat(user.getLastLoginAt()).isEqualTo(1678350673958L);
    }

    @Test
    public void 유효한_인증_코드로_계정을_활성화_할_수_있다() {
        // given
        User user = User.builder()
                .id(1L)
                .email("dev.hyoseung@gmail.com")
                .nickname("hyoseung")
                .address("Suwon")
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .status(UserStatus.PENDING)
                .lastLoginAt(100L)
                .build();

        // when
        user = user.certificate("aaaaaaaa-aaaa-aaaa-aaaaaaaaaaaa");

        // then
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    public void 잘못된_인증_코드로_게정을_활성화_하려하면_에러를_던진다() {
        // given
        User user = User.builder()
                .id(1L)
                .email("dev.hyoseung@gmail.com")
                .nickname("hyoseung")
                .address("Suwon")
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .status(UserStatus.PENDING)
                .lastLoginAt(100L)
                .build();

        // when
        // then
        assertThatThrownBy(() -> {
            user.certificate("aaaaaaaa-aaaa-aaaa-aaaaaaaaaaab");
        }).isInstanceOf(CertificationCodeNotMatchedException.class);
    }

}
