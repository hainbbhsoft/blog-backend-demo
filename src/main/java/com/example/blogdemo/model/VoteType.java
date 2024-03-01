package com.example.blogdemo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum VoteType {
    UPVOTE(1), DOWNVOTE(-1);
    private int i;
}
