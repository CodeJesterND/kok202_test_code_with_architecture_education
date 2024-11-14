package com.example.demo.small.mock;

import com.example.demo.common.application.port.ClockHolder;
import com.example.demo.common.application.port.UuidHolder;
import com.example.demo.post.application.port.PostRepository;
import com.example.demo.post.application.service.PostServiceImpl;
import com.example.demo.post.web.PostController;
import com.example.demo.post.web.PostCreateController;
import com.example.demo.post.web.port.PostService;
import com.example.demo.user.application.port.mail.MailSender;
import com.example.demo.user.application.port.repository.UserRepository;
import com.example.demo.user.application.service.CertificationService;
import com.example.demo.user.application.service.UserServiceImpl;
import com.example.demo.user.web.UserController;
import com.example.demo.user.web.UserCreateController;
import com.example.demo.user.web.port.AuthenticationService;
import com.example.demo.user.web.port.UserCreateService;
import com.example.demo.user.web.port.UserReadSerivce;
import com.example.demo.user.web.port.UserUpdateService;
import lombok.Builder;

public class TestContainer {

    public final MailSender mailSender;
    public final UserRepository userRepository;
    public final PostRepository postRepository;
    public final PostService postService;
    public final UserReadSerivce userReadSerivce;
    public final UserCreateService userCreateService;
    public final UserUpdateService userUpdateService;
    public final AuthenticationService authenticationService;
    public final CertificationService certificationService;
    public final UserController userController;
    public final UserCreateController userCreateController;
    public final PostController postController;
    public final PostCreateController postCreateController;

    @Builder
    public TestContainer(ClockHolder clockHolder, UuidHolder uuidHolder) {
        this.mailSender = new FakeMailSender();
        this.userRepository = new FakeUserRepository();
        this.postRepository = new FakePostRepository();
        this.postService = PostServiceImpl.builder()
                .postRepository(postRepository)
                .userRepository(userRepository)
                .clockHolder(clockHolder)
                .build();
        this.certificationService = new CertificationService(this.mailSender);
        UserServiceImpl userService = UserServiceImpl.builder()
                .uuidHolder(uuidHolder)
                .clockHolder(clockHolder)
                .userRepository(userRepository)
                .certificationService(certificationService)
                .build();
        this.userReadSerivce = userService;
        this.userCreateService = userService;
        this.userUpdateService = userService;
        this.authenticationService = userService;
        this.userController = UserController.builder()
                .userReadSerivce(userReadSerivce)
                .userUpdateService(userUpdateService)
                .authenticationService(authenticationService)
                .build();
        this.userCreateController = UserCreateController.builder()
                .userCreateService(userCreateService)
                .build();
        this.postController = PostController.builder()
                .postService(postService)
                .build();
        this.postCreateController = PostCreateController.builder()
                .postService(postService)
                .build();
    }

}
