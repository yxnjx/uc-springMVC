package com.sw.chap11.repository;

import com.sw.chap11.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    //레포지토리는 기본적인 CRUD 명령은 제공됨.
    //만약 내가 원하는 기능이 없으면(findByArticleId) 내가 만들어야됨.
    @Query(value = "Select * from comment where article_id = :articleId", nativeQuery = true) //sql문 실행해서 데이터 요청
    List<Comment> findByArticleId(Long articleId);

    @Query(value = "Select * from comment where nickname = :name", nativeQuery = true)
    List<Comment> findByNickname(String name);
}
