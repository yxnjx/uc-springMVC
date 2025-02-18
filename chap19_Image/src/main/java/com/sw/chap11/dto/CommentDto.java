package com.sw.chap11.dto;

import com.sw.chap11.entity.Comment;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@ToString
public class CommentDto { //Comment의 값만 저장하기 위한 객체
    private Long id;
    private Long articleId;
    private String nickname;
    private String body;

    public static CommentDto createCommentDto(Comment comment) {
        CommentDto dto = new CommentDto(
                comment.getId(),
                comment.getArticle().getId(), //article_id
                comment.getNickname(),
                comment.getBody()
        );
        return dto;
    }
}
