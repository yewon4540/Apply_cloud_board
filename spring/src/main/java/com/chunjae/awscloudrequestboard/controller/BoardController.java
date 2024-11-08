package com.chunjae.awscloudrequestboard.controller;

import com.chunjae.awscloudrequestboard.dto.AWSBoardDTO;
import com.chunjae.awscloudrequestboard.dto.CommentDTO;
import com.chunjae.awscloudrequestboard.service.AWSBoardServiceImpl;
import com.chunjae.awscloudrequestboard.service.CommentService;
import com.chunjae.awscloudrequestboard.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.chunjae.awscloudrequestboard.util.ClientIpUtil.getClientIp;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BoardController {
    private final AWSBoardServiceImpl boardService;
    private final UserService userService;
    private final CommentService commentService;

    @GetMapping("/posts")
    public String getPostsDefault(Model model) {
        return getPosts(1, model); // 기본 페이지를 1로 설정
    }

    @GetMapping("/posts/{page}")
    public String getPosts(@PathVariable int page, Model model) {
        int pageSize = 8; // 한 페이지에 표시할 글 개수
        Pageable pageable = PageRequest.of(page - 1, pageSize); // 1부터 시작하도록 조정
        Page<AWSBoardDTO> postsPage = boardService.findPosts(pageable);

        int totalPages = postsPage.getTotalPages();
        int currentPage = page; // 현재 페이지 (1부터 시작)

        int pageGroupSize = 5; // 페이지 그룹 크기
        int startPage = ((currentPage - 1) / pageGroupSize) * pageGroupSize + 1;
        int endPage = Math.min(startPage + pageGroupSize - 1, totalPages);

        // 이전 버튼이 있는지 여부
        boolean hasPrevious = startPage > 1;
        // 다음 버튼이 있는지 여부
        boolean hasNext = endPage < totalPages;

//        log.info("postPage..getContent.............{}", postsPage.getContent().get(0).getTitle());

        // 모델에 페이지네이션 관련 정보 추가
        model.addAttribute("posts", postsPage.getContent());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("hasPrevious", hasPrevious);
        model.addAttribute("hasNext", hasNext);

        //log.info("paging_hasPrevious ... {}", hasPrevious);
        //log.info("paging_hasNext ... {}", hasNext);

        model.addAttribute("view", "board/posts");
        return "index";
    }


    @GetMapping("/create_post")
    public String createPostForm(Model model) {
        model.addAttribute("board", new AWSBoardDTO());
        model.addAttribute("view", "board/create_post");
        return "index";
    }

    @PostMapping("/create_post")
    public String createPost(
            @ModelAttribute AWSBoardDTO dto
            , @RequestParam("attachment") MultipartFile file // 파일 받기
            , HttpServletRequest request
    ) throws UnknownHostException {

        // 클라이언트의 IP 주소를 가져오기
        String clientIp = getClientIp(request);
        log.info("crate_post...........{}", clientIp);

        String username = userService.findUserByIp(clientIp).getName();

        dto.setStatus("요청");         // 초기 상태 설정
        dto.setAuthor(username);     // 작성자 IP로 결정


        // 파일이 비어 있지 않으면 서비스에서 저장 처리
        if (!file.isEmpty()) {
            boardService.savePostWithFile(dto, file);
        } else {
            boardService.savePost(dto);  // 파일이 없으면 기존 저장 처리
        }

        return "redirect:/posts"; // 저장 후 목록으로 리다이렉트
    }

    @GetMapping("/posts/detail/{bid}")
    public String getPostDetail(@PathVariable Long bid
            , HttpServletRequest request
            , Model model) throws UnknownHostException {

        // 클라이언트의 IP 주소를 가져오기
        String clientIp = getClientIp(request);
        String username = userService.findUserByIp(clientIp).getName();

        AWSBoardDTO dto = boardService.getPostDetail(bid);
        List<CommentDTO> comments = commentService.getCommentsByPostId(bid);

        model.addAttribute("dto", dto);

//        log.info("dto..............{}", dto.getTitle());

        // 댓글이 잘 불러와졌는지 로그 확인
        log.info("Comments size: {}", comments.size());

        model.addAttribute("dto", dto);
        model.addAttribute("ip", clientIp);
        model.addAttribute("author", username);
        model.addAttribute("comments", comments);
        model.addAttribute("view", "board/post_detail");
        return "index";
    }

    @GetMapping("/posts/{bid}/download")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long bid) throws MalformedURLException {
        // 게시물에서 파일 경로 가져오기
        AWSBoardDTO dto = boardService.getPostDetail(bid);
        String filePath = dto.getAttachmentPath();

        if (filePath == null) {
            return ResponseEntity.notFound().build();  // 파일이 없을 경우 404 반환
        }

        // 디버깅을 위한 로그 추가
        log.info("파일 경로: {}", filePath);

        // 파일 경로를 Path 객체로 변환
        Path path = Paths.get(filePath);

        // 파일 존재 여부 확인
        if (!Files.exists(path)) {
            log.error("파일이 존재하지 않음: {}", filePath);
            return ResponseEntity.notFound().build();  // 파일이 없으면 404 반환
        }

        // UrlResource를 사용하여 Resource 생성
        Resource resource = new UrlResource(path.toUri());

        // 파일 다운로드를 위한 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + path.getFileName().toString() + "\"");
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(path.toFile().length())
                .body(resource);
    }

    @GetMapping("/edit_post/{bid}")
    public String editPostForm(
            @PathVariable Long bid
            , Model model) {
        AWSBoardDTO dto = boardService.getPostDetail(bid);
        model.addAttribute("dto", dto);
        model.addAttribute("view", "board/edit_post");

        return "index";
    }

    @PostMapping("/edit_post/{bid}")
    public String editPost(
            @PathVariable Long bid,
            @ModelAttribute AWSBoardDTO dto) {

        // 게시물 수정 서비스 호출
        boardService.updatePost(bid, dto);

        return "redirect:/posts/detail/{bid}"; // 수정 후 목록으로 리다이렉트
    }

    // 게시물 삭제 요청 처리
    @PostMapping("/delete_post/{bid}")
    public String deletePost(@PathVariable Long bid) {
        boardService.deletePost(bid);
        return "redirect:/posts"; // 삭제 후 게시물 목록으로 리다이렉트
    }


}
