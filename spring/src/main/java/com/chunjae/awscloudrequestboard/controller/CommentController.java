package com.chunjae.awscloudrequestboard.controller;

import com.chunjae.awscloudrequestboard.dto.CommentDTO;
import com.chunjae.awscloudrequestboard.service.CommentService;
import com.chunjae.awscloudrequestboard.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.chunjae.awscloudrequestboard.util.ClientIpUtil.getClientIp;

@Slf4j
@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;

    @PostMapping("/{aid}/comments")
    public ResponseEntity<CommentDTO> addComment(
            @PathVariable Long aid
            , @RequestBody CommentDTO commentDTO
            , HttpServletRequest request) {
        try {

            // 클라이언트의 IP 주소를 가져오기
            String clientIp = getClientIp(request);
            String username = userService.findUserByIp(clientIp).getName();

            // 댓글 저장 로직
            CommentDTO savedComment = commentService.addComment(aid, commentDTO, clientIp, username);
            return ResponseEntity.ok(savedComment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PostMapping("/{aid}/comments/edit")
    public ResponseEntity<CommentDTO> editComment(
            @PathVariable Long aid,
            @RequestBody CommentDTO commentDTO) {
        try {
            CommentDTO updatedComment = commentService.editComment(commentDTO);
            return ResponseEntity.ok(updatedComment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PostMapping("/{aid}/comments/delete")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long aid,
            @RequestBody Long commentId) {
        try {
            commentService.deleteComment(commentId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}