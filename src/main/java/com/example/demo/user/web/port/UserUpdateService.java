package com.example.demo.user.web.port;

import com.example.demo.user.domain.User;
import com.example.demo.user.web.request.UserUpdate;

public interface UserUpdateService {

    User update(long id, UserUpdate userUpdate);
}
