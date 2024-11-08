package com.chunjae.awscloudrequestboard.service;

import com.chunjae.awscloudrequestboard.domain.AWSBoard;
import com.chunjae.awscloudrequestboard.dto.AWSBoardDTO;
import com.chunjae.awscloudrequestboard.repository.AWSBoardRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AWSBoardServiceImpl implements AWSBoardService{
    private final AWSBoardRepository boardRepository;
    private final ModelMapper modelMapper;


    @Override
    public Page<AWSBoardDTO> findPosts(Pageable pageable) {
        // id를 기준으로 내림차순 정렬
        Pageable sortedByIdDesc = PageRequest.of(
                pageable.getPageNumber()
                , pageable.getPageSize()
                , Sort.by(Sort.Direction.DESC, "aid"));

        Page<AWSBoard> boardList = boardRepository.findAll(sortedByIdDesc);
        Page<AWSBoardDTO> bList = boardList.map(board -> modelMapper.map(board, AWSBoardDTO.class));

        return bList;
    }

    @Override
    public void savePost(AWSBoardDTO dto) {
        AWSBoard board = modelMapper.map(dto, AWSBoard.class);
        boardRepository.save(board);
    }

    @Override
    public AWSBoardDTO getPostDetail(Long bid) {
        AWSBoard board = boardRepository.findById(bid)
                .orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다."));
        AWSBoardDTO dto = modelMapper.map(board, AWSBoardDTO.class);
        return dto;
    }

    @Override
    public void savePostWithFile(AWSBoardDTO dto, MultipartFile file) {
        AWSBoard board = modelMapper.map(dto, AWSBoard.class);

        // 파일 저장 경로 설정
//        String uploadDir = "C:/uploads/";  // 실제로 저장할 디렉토리 경로 설정
        String uploadDir = "/home/admin/cloud_board/uploads/";  // 실제로 저장할 디렉토리 경로 설정
        String originalFilename = file.getOriginalFilename();

        try {
            // 파일 저장
            File dest = new File(uploadDir + originalFilename);
            file.transferTo(dest);  // 파일을 해당 경로에 저장

            // 필요하면 파일 이름을 DTO에 저장 (DB에 저장할 경우)
            board.setAttachmentPath(dest.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        }

        boardRepository.save(board);
    }

    @Override
    public void updatePost(Long bid, AWSBoardDTO dto) {
        // 게시물 찾기
        AWSBoard existingPost = boardRepository.findById(bid)
                .orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다."));


        log.info("startDate..............{}",dto.getStartDate());
        log.info("endDate..............{}",dto.getEndDate());

        // DTO의 값을 기존 게시물에 업데이트
        existingPost.setTitle(dto.getTitle());
        existingPost.setService(dto.getService());
        existingPost.setReason(dto.getReason());
        existingPost.setStartDate(dto.getStartDate());
        existingPost.setEndDate(dto.getEndDate());
        existingPost.setStatus(dto.getStatus());

        // 수정된 게시물 저장
        boardRepository.save(existingPost);
    }

    @Override
    public void deletePost(Long bid) {
        // 게시물 찾기
        AWSBoard existingPost = boardRepository.findById(bid)
                .orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다."));

        // 게시물 삭제
        boardRepository.delete(existingPost);
    }


}
