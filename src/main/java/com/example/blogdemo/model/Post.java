package com.example.blogdemo.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Data
@Builder //Auto add Builder Pattern
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    private String postName;

    private String url;

    @Lob //Annotate a field that hold the large size of data(Long text, Image,...)
    private  String description;

    private Integer voteCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;

    private Instant createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", referencedColumnName = "id") //Reference entity
    private Subreddit subreddit;
}
