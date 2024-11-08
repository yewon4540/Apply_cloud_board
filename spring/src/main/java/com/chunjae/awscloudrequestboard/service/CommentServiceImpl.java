package com.chunjae.awscloudrequestboard.service;

import com.chunjae.awscloudrequestboard.domain.AWSBoard;
import com.chunjae.awscloudrequestboard.domain.Comment;
import com.chunjae.awscloudrequestboard.dto.CommentDTO;
import com.chunjae.awscloudrequestboard.repository.AWSBoardRepository;
import com.chunjae.awscloudrequestboard.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;
    private final AWSBoardRepository awsBoardRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<CommentDTO> getCommentsByPostId(Long bid) {
        List<Comment> result = commentRepository.findByAwsBoard_Aid(bid);
        List<CommentDTO> comments = result.stream().map(item ->
                modelMapper.map(item, CommentDTO.class)
                ).collect(Collectors.toList());

        log.info("comments_size......{} postId_about_comments......{}", comments.size(), bid); // 댓글이 잘 불러와지는지 확인하는 로그

        return comments;
    }


    @Override
    public CommentDTO addComment(Long postId, CommentDTO commentDTO, String clientIp, String username) {
        if (commentDTO.getComment() == null || commentDTO.getComment().trim().isEmpty()) {
            throw new IllegalArgumentException("댓글 내용은 필수입니다.");
        }

        AWSBoard awsBoard = awsBoardRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        Comment comment = Comment.builder()
                .cate(commentDTO.getCate())
                .comment(commentDTO.getComment())
                .author(username) // 예시로 작성, 실제로는 로그인된 사용자 정보로 변경
                .awsBoard(awsBoard)
                .createdAt(LocalDateTime.now()) // 작성 시간을 현재 시간으로 설정
                .ip(clientIp)
                .build();

        Comment savedComment = commentRepository.save(comment);

        return CommentDTO.builder()
                .cid(savedComment.getCid())
                .cate(savedComment.getCate())
                .comment(savedComment.getComment())
                .author(savedComment.getAuthor())
                .createdAt(savedComment.getCreatedAt())
                .build();
    }



    @Override
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));
        commentRepository.delete(comment);
    }

    @Override
    public CommentDTO editComment(CommentDTO commentDTO) {
        Comment comment = commentRepository.findById(commentDTO.getCid())
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));

        comment.setComment(commentDTO.getComment()); // 댓글 내용 수정
        commentRepository.save(comment); // 수정된 댓글 저장

        return CommentDTO.builder()
                .cid(comment.getCid())
                .cate(comment.getCate())
                .comment(comment.getComment())
                .author(comment.getAuthor())
                .createdAt(comment.getCreatedAt())
                .build();
    }


}
