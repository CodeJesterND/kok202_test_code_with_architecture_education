package com.example.demo.user.web.port;

import com.example.demo.user.domain.User;
import com.example.demo.user.web.request.UserCreate;

public interface UserCreateService {

    User create(UserCreate userCreate);
}
