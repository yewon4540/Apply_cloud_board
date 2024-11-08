package com.chunjae.awscloudrequestboard.repository;

import com.chunjae.awscloudrequestboard.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByAwsBoard_Aid(Long aid);
}
