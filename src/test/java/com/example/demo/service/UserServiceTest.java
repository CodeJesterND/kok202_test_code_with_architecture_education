package com.example.demo.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

import com.example.demo.exception.CertificationCodeNotMatchedException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.UserStatus;
import com.example.demo.model.dto.UserCreateDto;
import com.example.demo.model.dto.UserUpdateDto;
import com.example.demo.repository.UserEntity;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlGroup;

@SpringBootTest
@TestPropertySource("classpath:test-application.properties")
@SqlGroup({
        @Sql(value = "/sql/user-service-test-data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD),
})
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private JavaMailSender mailSender;

    @Test
    void getByEmail은_ACTIVE_상태인_유저를_찾아올_수_잇다() {

        // given
        String email = "dev.hyoseung@gmail.com";

        // when
        UserEntity userEntity = userService.getByEmail(email);

        // then
        assertThat(userEntity.getNickname()).isEqualTo("hyoseung");
    }

    @Test
    void getByEmail은_PENDING_상태인_유저를_찾아올_수_없다() {

        // given
        String email = "jaeyeon@gmail.com";

        // when
        // then
        assertThatThrownBy(() -> {
            UserEntity userEntity = userService.getByEmail(email);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getById는_ACTIVE_상태인_유저를_찾아올_수_잇다() {

        // given
        // when
        UserEntity userEntity = userService.getById(1);

        // then
        assertThat(userEntity.getNickname()).isEqualTo("hyoseung");
    }

    @Test
    void getById은_PENDING_상태인_유저를_찾아올_수_없다() {

        // given
        // when
        // then
        assertThatThrownBy(() -> {
            UserEntity userEntity = userService.getById(2);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void userCreateDto_를_이용하여_유저를_생성할_수_있다() {

        // given
        UserCreateDto userCreateDto = UserCreateDto.builder()
                .email("ubin@naver.com")
                .address("Suwon")
                .nickname("ubin")
                .build();
        BDDMockito.doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        // when
        UserEntity userEntity = userService.create(userCreateDto);

        // then
        assertThat(userEntity.getId()).isNotNull();
        assertThat(userEntity.getStatus()).isEqualTo(UserStatus.PENDING);
        // assertThat(userEntity.getCertificationCode()).isEqualTo("T.T");
    }

    @Test
    void userUpdateDto_를_이용하여_유저를_수정할_수_있다() {

        // given
        UserUpdateDto userUpdateDto = UserUpdateDto.builder()
                .address("Anyang")
                .nickname("hyoseung-n")
                .build();

        // when
        userService.update(1L, userUpdateDto);

        // then
        UserEntity userEntity = userService.getById(1);
        assertThat(userEntity.getId()).isNotNull();
        assertThat(userEntity.getAddress()).isEqualTo("Anyang");
        assertThat(userEntity.getNickname()).isEqualTo("hyoseung-n");
    }

    @Test
    void user를_로그인_시키면_마지막_로그인_시간이_변경된다() {

        // given
        // when
        userService.login(1);

        // then
        UserEntity userEntity = userService.getById(1);
        assertThat(userEntity.getLastLoginAt()).isGreaterThan(0L);
        // assertThat(userEntity.getLastLoginAt()).isEqualTo("T.T");
    }

    @Test
    void PENDING_상태의_사용자는_인증_코드로_ACTIVE_시킬_수_있다() {

        // given
        // when
        userService.verifyEmail(2, "aaaaaaaa-aaaa-aaaa-aaaaaaaaaaab");

        // then
        UserEntity userEntity = userService.getById(2);
        assertThat(userEntity.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void PENDING_상태의_사용자는_잘못된_인증_코드를_받으면_에러를_던진다() {

        // given
        // when
        // then
        assertThatThrownBy(() -> {
            userService.verifyEmail(2, "aaaaaaaa-aaaa-aaaa-aaaaaaaaaaac");
        }).isInstanceOf(CertificationCodeNotMatchedException.class);
    }

}
