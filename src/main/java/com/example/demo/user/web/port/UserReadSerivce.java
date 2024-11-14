package com.example.demo.user.web.port;

import com.example.demo.user.domain.User;

public interface UserReadSerivce {

    User getById(long id);

    User getByEmail(String email);
}
