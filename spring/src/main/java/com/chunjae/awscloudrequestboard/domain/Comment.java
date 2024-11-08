package com.chunjae.awscloudrequestboard.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cid;

    private String cate;

    @Column(nullable = false)
    private String comment;

    private String author;

    @LastModifiedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    private String ip;

    //aid
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aid")
    AWSBoard awsBoard;
}
