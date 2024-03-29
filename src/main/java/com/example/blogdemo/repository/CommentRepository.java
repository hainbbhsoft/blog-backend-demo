package com.example.blogdemo.repository;

import com.example.blogdemo.model.Comment;
import com.example.blogdemo.model.Post;
import com.example.blogdemo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);

    List<Comment> findAllByUser(User user);
}
