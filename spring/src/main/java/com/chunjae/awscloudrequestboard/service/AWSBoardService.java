package com.chunjae.awscloudrequestboard.service;

import com.chunjae.awscloudrequestboard.dto.AWSBoardDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface AWSBoardService {
    Page<AWSBoardDTO> findPosts(Pageable pageable);

    void savePost(AWSBoardDTO dto);

    AWSBoardDTO getPostDetail(Long bid);

    void savePostWithFile(AWSBoardDTO dto, MultipartFile file);

    void updatePost(Long bid, AWSBoardDTO dto);

    // 게시물 삭제 메서드 추가
    void deletePost(Long bid);
}
