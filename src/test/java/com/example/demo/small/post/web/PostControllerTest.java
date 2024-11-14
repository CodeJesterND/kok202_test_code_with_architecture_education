package com.example.demo.small.post.web;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.example.demo.post.domain.Post;
import com.example.demo.post.web.request.PostUpdate;
import com.example.demo.post.web.response.PostResponse;
import com.example.demo.small.mock.TestContainer;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

class PostControllerTest {

    @Test
    void 사용자는_게시물을_단건_조회_할_수_있다() throws Exception {
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();

        User user = User.builder()
                .id(1L)
                .email("dev.hyoseung@gmail.com")
                .nickname("hyoseung")
                .address("Suwon")
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(0L)
                .build();

        testContainer.userRepository.save(user);

        Post post = Post.builder()
                .id(1L)
                .content("Hello World :)")
                .createdAt(1678350673958L)
                .modifiedAt(0L)
                .writer(user)
                .build();

        testContainer.postRepository.save(post);

        // when
        ResponseEntity<PostResponse> result = testContainer.postController.getPostById(1);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getContent()).isEqualTo("Hello World :)");
        assertThat(result.getBody().getCreatedAt()).isEqualTo(1678350673958L);
        assertThat(result.getBody().getWriter().getNickname()).isEqualTo("hyoseung");
    }

    @Test
    void 사용자는_게시물을_수정할_수_있다() throws Exception {
        // given
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(() -> 200L)
                .build();

        User user = User.builder()
                .id(1L)
                .email("dev.hyoseung@gmail.com")
                .nickname("hyoseung")
                .address("Suwon")
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(0L)
                .build();

        testContainer.userRepository.save(user);

        Post post = Post.builder()
                .id(1L)
                .content("Hello World :)")
                .createdAt(100L)
                .modifiedAt(0L)
                .writer(user)
                .build();

        testContainer.postRepository.save(post);

        // when
        ResponseEntity<PostResponse> result = testContainer.postController.updatePost(
                1,
                PostUpdate.builder()
                        .content("foobar")
                        .build()
        );

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getContent()).isEqualTo("foobar");
        assertThat(result.getBody().getCreatedAt()).isEqualTo(100L);
        assertThat(result.getBody().getModifiedAt()).isEqualTo(200L);
        assertThat(result.getBody().getWriter().getNickname()).isEqualTo("hyoseung");
    }

}
