package com.sw.finalProject.controller;

import com.sw.finalProject.dto.CommentDto;
import com.sw.finalProject.entity.Comment;
import com.sw.finalProject.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ApiCommentController {
    @Autowired
    private CommentService commentService;

    // 1. 댓글 조회
    @GetMapping("/api/sales/{saleId}/comments")
    public ResponseEntity<List<CommentDto>> comments(@PathVariable Long saleId) {
        List<CommentDto> listDto = commentService.comments(saleId); //모든 댓글 복사하는 명령

        return ResponseEntity.status(HttpStatus.OK).body(listDto);
    }

    // 2. 댓글 생성
    @PostMapping("/api/sales/{saleId}/comments")
    public ResponseEntity<Comment> create(@PathVariable Long saleId, @RequestBody CommentDto dto) {
        Comment saved = commentService.create(saleId, dto); //saleId의 게시물이 dto에 저장됨

        if(saved == null) ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // 3. 댓글 수정
    @PatchMapping("/api/comments/{id}")
    public ResponseEntity<CommentDto> update(@PathVariable Long id, @RequestBody CommentDto dto) {
        // 서비스에게 위임
        CommentDto updatedDto = commentService.update(id, dto);

        // 결과 응답
        return ResponseEntity.status(HttpStatus.OK).body(updatedDto);
    }

    // 4. 댓글 삭제
    @DeleteMapping("/api/comments/{id}")
    public ResponseEntity<CommentDto> delete(@PathVariable Long id) {
        // 서비스에게 위임
        CommentDto deletedDto = commentService.delete(id);

        // 결과 응답
        return ResponseEntity.status(HttpStatus.OK).body(deletedDto);
    }
}
