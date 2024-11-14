package com.example.demo.small.post.application.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.demo.post.application.service.PostServiceImpl;
import com.example.demo.post.domain.Post;
import com.example.demo.post.web.request.PostCreate;
import com.example.demo.post.web.request.PostUpdate;
import com.example.demo.small.mock.TestClockHolder;
import com.example.demo.small.mock.FakePostRepository;
import com.example.demo.small.mock.FakeUserRepository;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PostServiceImplTest {

    private PostServiceImpl postServiceImpl;

    @BeforeEach
    void init() {
        FakePostRepository fakePostRepository = new FakePostRepository();
        FakeUserRepository fakeUserRepository = new FakeUserRepository();


        postServiceImpl = PostServiceImpl.builder()
                .postRepository(fakePostRepository)
                .userRepository(fakeUserRepository)
                .clockHolder(new TestClockHolder(1679530673958L))
                .build();

        User user1 = User.builder()
                .id(1L)
                .email("dev.hyoseung@gmail.com")
                .nickname("hyoseung")
                .address("Suwon")
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(0L)
                .build();

        User user2 = User.builder()
                .id(2L)
                .email("jaeyeon@gmail.com")
                .nickname("jaeyeon")
                .address("Suwon")
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .status(UserStatus.PENDING)
                .lastLoginAt(0L)
                .build();


        Post post = Post.builder()
                .id(1L)
                .content("hello world")
                .createdAt(1678350673958L)
                .modifiedAt(0L)
                .writer(user1)
                .build();

        fakeUserRepository.save(user1);
        fakeUserRepository.save(user2);
        fakePostRepository.save(post);
    }

    @Test
    void getById는_존재하는_게시물을_내려준다() {
        // given
        // when
        Post result = postServiceImpl.getById(1);

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
        Post result = postServiceImpl.create(postCreate);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getContent()).isEqualTo("foobar");
        assertThat(result.getCreatedAt()).isEqualTo(1679530673958L);
    }

    @Test
    void postUpdateDto_를_이용하여_게시물을_수정할_수_있다() {
        // given
        PostUpdate postUpdate = PostUpdate.builder()
                .content("hello world :)")
                .build();

        // when
        postServiceImpl.update(1, postUpdate);

        // then
        Post Post = postServiceImpl.getById(1);
        assertThat(Post.getContent()).isEqualTo("hello world :)");
        assertThat(Post.getModifiedAt()).isEqualTo(1679530673958L);
    }

}