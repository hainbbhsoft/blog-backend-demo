package com.example.blogdemo.repository;

import com.example.blogdemo.model.Post;
import com.example.blogdemo.model.User;
import com.example.blogdemo.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);
}
