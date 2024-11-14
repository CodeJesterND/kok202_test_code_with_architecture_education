package com.example.demo.post.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.example.demo.post.web.request.PostCreate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class PostTest {

    @Test
    public void PostCreate으로_게시물을_만들_수_있다() {
        // given
        PostCreate postCreate = PostCreate.builder()
                .writerId(1)
                .content("helloworld")
                .build();

        User writer = User.builder()
                .id(1L)
                .email("dev.hyoseung@gamil.com")
                .nickname("hyoseung")
                .address("Suwon")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .build();

        // when
        Post post = Post.from(writer, postCreate);

        // then
        assertThat(post.getContent()).isEqualTo("helloworld");
        assertThat(post.getWriter().getEmail()).isEqualTo("dev.hyoseung@gamil.com");
        assertThat(post.getWriter().getNickname()).isEqualTo("hyoseung");
        assertThat(post.getWriter().getAddress()).isEqualTo("Suwon");
        assertThat(post.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(post.getWriter().getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaaaaaaaaaa");
    }

}
