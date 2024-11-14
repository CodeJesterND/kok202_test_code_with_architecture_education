package com.example.demo.post.web.port;

import com.example.demo.post.domain.Post;
import com.example.demo.post.web.request.PostCreate;
import com.example.demo.post.web.request.PostUpdate;

public interface PostService {

    Post getById(long id);

    Post create(PostCreate postCreate);

    Post update(long id, PostUpdate postUpdate);
}
