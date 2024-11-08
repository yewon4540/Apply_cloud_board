package com.chunjae.awscloudrequestboard.repository;

import com.chunjae.awscloudrequestboard.domain.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateRepository extends JpaRepository<Template, Long> {
}
