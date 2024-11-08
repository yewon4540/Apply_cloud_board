package com.chunjae.awscloudrequestboard.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TemplateDTO {
    private Long tid;
    private String templateInfo;
    private String templateName;
    private String templateBody;
    private LocalDateTime lastUpdated;
}
