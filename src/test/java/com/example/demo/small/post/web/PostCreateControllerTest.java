package com.example.demo.small.post.web;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.example.demo.post.web.request.PostCreate;
import com.example.demo.post.web.response.PostResponse;
import com.example.demo.small.mock.TestContainer;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

class PostCreateControllerTest {

    @Test
    void 사용자는_게시물을_작성할_수_있다() throws Exception {
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
                .lastLoginAt(1678350673958L)
                .build()
        );

        PostCreate postCreate = PostCreate.builder()
                .writerId(1)
                .content("hello world")
                .build();

        // when
        ResponseEntity<PostResponse> result = testContainer.postCreateController.createPost(postCreate);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getContent()).isEqualTo("hello world");
        assertThat(result.getBody().getCreatedAt()).isEqualTo(1678350673958L);
        assertThat(result.getBody().getModifiedAt()).isNull();
        assertThat(result.getBody().getWriter().getId()).isEqualTo(1);
    }

}
