package com.chunjae.awscloudrequestboard.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDTO {
    private Long cid;
    private String cate;
    private String comment;
    private String author;
    private LocalDateTime createdAt;
    private Long aid;
    private String ip;
}
