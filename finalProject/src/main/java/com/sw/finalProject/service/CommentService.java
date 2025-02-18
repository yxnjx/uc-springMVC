package com.sw.finalProject.service;

import com.sw.finalProject.dto.CommentDto;
import com.sw.finalProject.entity.Comment;
import com.sw.finalProject.entity.Sale;
import com.sw.finalProject.repository.CommentRepository;
import com.sw.finalProject.repository.SaleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private SaleRepository saleRepository;

    public List<CommentDto> comments(Long saleId) {
        List<Comment> lists = commentRepository.findBySaleId(saleId);
        List<CommentDto> listDto = new ArrayList<CommentDto>();

        for(int i = 0; i < lists.size(); i++) {
            CommentDto dto = CommentDto.createCommentDto(lists.get(i)); //입력은 comment -> dto 변경
            listDto.add(dto);
        }
        return listDto;
    }

    public Comment create(Long id, CommentDto dto) { //id - 게시글, dto - 답글
        // 1. 타겟 검사
        Sale target = saleRepository.findById(id).orElse(null);

        if(target == null) return null;

        // 2. dto -> Entity
        Comment entity = new Comment();
        entity.setNickname(dto.getNickname());
        entity.setBody(dto.getBody());
        //entity.setId(dto.getId()); //entity id는 저장하면서 자동으로 생김. 값을 세팅하는 게 아니기 때문에 없어도 됨.
        entity.setSale(target); //id 값이 있는지 35번 줄에서 검사했기 때문에 id 값이 target에 저장되어 있음.

        // 3. entity 저장
        Comment saved = commentRepository.save(entity);

        // 4. 리턴
        return saved;
    }

    @Transactional
    public CommentDto update(Long id, CommentDto dto) { //id - 수정하려는 번호
        // 1. 댓글 조회 및 예외 발생
        Comment target = commentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("댓글 수정 실패! 대상 댓글이 없습니다."));

        // 2. 댓글 수정
        /*
        if(dto.getNickname().equals(""))
            dto.setNickname(null);

        if(dto.getBody().equals(""))
            dto.setBody(null);
        */
        target.patch(dto);
        //닉네임과 댓글 내용 둘 중에 하나 없는 건 db에 있는 걸 복사해서 넣음(원래는 둘 중에 하나 없는 게 null 처리가 됨. 그걸 방지하기 위한 코드)

        // 3. DB로 갱신
        Comment updated = commentRepository.save(target);

        // 4. 댓글 엔티티를 DTO로 변환 및 반환
        return CommentDto.createCommentDto(updated);
    }

    @Transactional
    public CommentDto delete(Long id) {
        // 1. 댓글 조회 및 예외 발생
        Comment target = commentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("댓글 삭제 실패! 대상이 없습니다."));

        // 2. 댓글 삭제
        commentRepository.delete(target);

        // 3. 삭제 댓글을 DTO로 변환 및 반환
        return CommentDto.createCommentDto(target);
    }
}
