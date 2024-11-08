package com.chunjae.awscloudrequestboard.dto;

import com.chunjae.awscloudrequestboard.domain.Template;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TempVariableDTO {
    private Long tvid;
    private String templateName;
    private String variableName;
    private String variableType;
    private String defaultValue;
    private String description;
    private LocalDateTime lastUpdated;
    private Template template; //tid
}
