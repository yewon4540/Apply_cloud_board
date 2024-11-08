package com.chunjae.awscloudrequestboard.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "temp_variable")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class TempVariable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tvid;

    @Column(name = "template_name", nullable = false)
    private String templateName;

    @Column(name = "variable_name", nullable = false)
    private String variableName;

    @Column(name = "variable_type", nullable = false)
    private String variableType;

    @Column(name = "default_value")
    private String defaultValue;

    private String description;

    @LastModifiedDate
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    //tid
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tid")
    Template template;

}
