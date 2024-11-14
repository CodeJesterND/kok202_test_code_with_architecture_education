package com.example.demo.small.user.application.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.small.mock.FakeMailSender;
import com.example.demo.small.mock.TestClockHolder;
import com.example.demo.small.mock.TestUuidHolder;
import com.example.demo.user.application.service.CertificationService;
import com.example.demo.user.application.service.UserServiceImpl;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.small.mock.FakeUserRepository;
import com.example.demo.user.web.request.UserCreate;
import com.example.demo.user.web.request.UserUpdate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserServiceImplTest {

    private UserServiceImpl userServiceImpl;

    @BeforeEach
    void init() {
        FakeMailSender fakeMailSender = new FakeMailSender();
        FakeUserRepository fakeUserRepository = new FakeUserRepository();

        userServiceImpl = UserServiceImpl.builder()
                .uuidHolder(new TestUuidHolder("aaaaaaaa-aaaa-aaaa-aaaaaaaaaaaa"))
                .clockHolder(new TestClockHolder(1678350673958L))
                .userRepository(fakeUserRepository)
                .certificationService(new CertificationService(fakeMailSender))
                .build();

        fakeUserRepository.save(User.builder()
                .id(1L)
                .email("dev.hyoseung@gmail.com")
                .nickname("hyoseung")
                .address("Suwon")
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(0L)
                .build()
        );

        fakeUserRepository.save(User.builder()
                .id(2L)
                .email("jaeyeon@gmail.com")
                .nickname("jaeyeon")
                .address("Suwon")
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .status(UserStatus.PENDING)
                .lastLoginAt(0L)
                .build()
        );
    }

    @Test
    void getByEmail은_ACTIVE_상태인_유저를_찾아올_수_있다() {
        // given
        String email = "dev.hyoseung@gmail.com";

        // when
        User result = userServiceImpl.getByEmail(email);

        // then
        assertThat(result.getNickname()).isEqualTo("hyoseung");
    }

    @Test
    void getByEmail은_PENDING_상태인_유저를_찾아올_수_없다() {
        // given
        String email = "jaeyeon@gmail.com";

        // when
        // then
        assertThatThrownBy(() -> {
            User result = userServiceImpl.getByEmail(email);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getById는_ACTIVE_상태인_유저를_찾아올_수_있다() {
        // given
        // when
        User result = userServiceImpl.getById(1);

        // then
        assertThat(result.getNickname()).isEqualTo("hyoseung");
    }

    @Test
    void getById은_PENDING_상태인_유저를_찾아올_수_없다() {
        // given
        // when
        // then
        assertThatThrownBy(() -> {
            User result = userServiceImpl.getById(2);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void userCreateDto_를_이용하여_유저를_생성할_수_있다() {
        // given
        UserCreate userCreate = UserCreate.builder()
                .email("ubin@naver.com")
                .address("Suwon")
                .nickname("ubin")
                .build();

        // when
        User result = userServiceImpl.create(userCreate);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(result.getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaaaaaaaaaa");
    }

    @Test
    void userUpdateDto_를_이용하여_유저를_수정할_수_있다() {
        // given
        UserUpdate userUpdate = UserUpdate.builder()
                .address("Anyang")
                .nickname("hyoseung-n")
                .build();

        // when
        userServiceImpl.update(1L, userUpdate);

        // then
        User User = userServiceImpl.getById(1);
        assertThat(User.getId()).isNotNull();
        assertThat(User.getAddress()).isEqualTo("Anyang");
        assertThat(User.getNickname()).isEqualTo("hyoseung-n");
    }

    @Test
    void user를_로그인_시키면_마지막_로그인_시간이_변경된다() {
        // given
        // when
        userServiceImpl.login(1);

        // then
        User result = userServiceImpl.getById(1);
        assertThat(result.getLastLoginAt()).isEqualTo(1678350673958L);
    }

    @Test
    void PENDING_상태의_사용자는_인증_코드로_ACTIVE_시킬_수_있다() {
        // given
        // when
        userServiceImpl.verifyEmail(2, "aaaaaaaa-aaaa-aaaa-aaaaaaaaaaaa");

        // then
        User result = userServiceImpl.getById(2);
        assertThat(result.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void PENDING_상태의_사용자는_잘못된_인증_코드를_받으면_에러를_던진다() {
        // given
        // when
        // then
        assertThatThrownBy(() -> {
            userServiceImpl.verifyEmail(2, "aaaaaaaa-aaaa-aaaa-aaaaaaaaaaac");
        }).isInstanceOf(CertificationCodeNotMatchedException.class);
    }

}
