package com.chunjae.awscloudrequestboard.service;

import com.chunjae.awscloudrequestboard.dto.CommentDTO;

import java.util.List;

public interface CommentService {
    List<CommentDTO> getCommentsByPostId(Long bid);

    CommentDTO addComment(Long postId, CommentDTO commentDTO, String clientIp, String username);

    void deleteComment(Long commentId);

    CommentDTO editComment(CommentDTO commentDTO);
}
