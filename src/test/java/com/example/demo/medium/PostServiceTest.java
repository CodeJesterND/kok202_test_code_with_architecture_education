package com.example.demo.medium;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.demo.post.application.service.PostService;
import com.example.demo.post.domain.Post;
import com.example.demo.post.web.request.PostCreate;
import com.example.demo.post.web.request.PostUpdate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlGroup;

@SpringBootTest
@TestPropertySource("classpath:test-application.properties")
@SqlGroup({
        @Sql(value = "/sql/post/post-service-test-data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/common/delete-all-data.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD),
})
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Test
    void getById는_존재하는_게시물을_내려준다() {
        // given
        // when
        Post result = postService.getById(1);

        // then
        assertThat(result.getContent()).isEqualTo("hello world");
    }

    @Test
    void postCreateDto_를_이용하여_게시물을_생성할_수_있다() {
        // given
        PostCreate postCreate = PostCreate.builder()
                .writerId(1)
                .content("foobar")
                .build();

        // when
        Post result = postService.create(postCreate);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getContent()).isEqualTo("foobar");
        assertThat(result.getCreatedAt()).isGreaterThan(0);
    }

    @Test
    void postUpdateDto_를_이용하여_게시물을_수정할_수_있다() {
        // given
        PostUpdate postUpdate = PostUpdate.builder()
                .content("hello world :)")
                .build();

        // when
        postService.update(1, postUpdate);

        // then
        Post Post = postService.getById(1);
        assertThat(Post.getContent()).isEqualTo("hello world :)");
        assertThat(Post.getModifiedAt()).isGreaterThan(0);
    }

}