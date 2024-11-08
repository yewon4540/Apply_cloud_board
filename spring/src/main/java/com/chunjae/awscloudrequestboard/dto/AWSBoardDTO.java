package com.chunjae.awscloudrequestboard.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AWSBoardDTO {
    private Long aid;
    private String title;
    private String service;
    private String reason;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private LocalDateTime createdAt;
    private String author;

    // 파일 이름 또는 경로를 저장할 필드 추가
    private String attachmentPath;
}
