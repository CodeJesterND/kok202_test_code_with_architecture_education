package com.example.demo.user.web.port;

public interface AuthenticationService {

    void login(long id);

    void verifyEmail(long id, String certificationCode);
}
