package com.example.blogdemo.repository;

import com.example.blogdemo.model.Post;
import com.example.blogdemo.model.Subreddit;
import com.example.blogdemo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllBySubreddit(Subreddit subreddit);

    List<Post> findByUser(User user);
}
