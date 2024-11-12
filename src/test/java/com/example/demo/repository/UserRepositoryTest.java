package com.example.demo.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.example.demo.model.UserStatus;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlGroup;

@DataJpaTest
@TestPropertySource("classpath:test-application.properties")
@SqlGroup({
        @Sql(value = "/sql/user-repository-test-data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD),
})
class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @Test
    void findByIdAndStatus_로_유저_데이터를_찾아올_수_있다() {

        // given
        // when
        Optional<UserEntity> result = repository.findByIdAndStatus(1, UserStatus.ACTIVE);

        // then
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    void findByIdAndStatus_는_데이터가_없으면_Optional_empty_를_내려준다() {

        // given
        // when
        Optional<UserEntity> result = repository.findByIdAndStatus(1, UserStatus.PENDING);

        // then
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    void findByEmailAndStatus_로_유저_데이터를_찾아올_수_있다() {

        // given
        // when
        Optional<UserEntity> result = repository.findByEmailAndStatus("dev.hyoseung@gmail.com", UserStatus.ACTIVE);

        // then
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    void findByEmailAndStatus_는_데이터가_없으면_Optional_empty_를_내려준다() {

        // given
        // when
        Optional<UserEntity> result = repository.findByEmailAndStatus("dev.hyoseung@gmail.com", UserStatus.PENDING);

        // then
        assertThat(result.isEmpty()).isTrue();
    }

}