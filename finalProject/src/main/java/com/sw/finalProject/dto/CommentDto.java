package com.sw.finalProject.dto;

import com.sw.finalProject.entity.Comment;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@ToString
public class CommentDto {
    private Long id;
    private Long saleId;
    private String nickname;
    private String body;

    public static CommentDto createCommentDto(Comment comment) {
        CommentDto dto = new CommentDto(
                comment.getId(),
                comment.getSale().getId(), //sale_id
                comment.getNickname(),
                comment.getBody()
        );
        return dto;
    }
}
