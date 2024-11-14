package com.example.demo.post.application.port;

import com.example.demo.post.domain.Post;
import java.util.Optional;

public interface PostRepository {

    Optional<Post> findById(long id);

    Post save(Post post);
}
