package com.chunjae.awscloudrequestboard.repository;

import com.chunjae.awscloudrequestboard.domain.AWSBoard;
import com.chunjae.awscloudrequestboard.dto.AWSBoardDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AWSBoardRepository extends JpaRepository<AWSBoard, Long> {
    Page<AWSBoard> findAllBy(Pageable sortedByIdDesc);
}
