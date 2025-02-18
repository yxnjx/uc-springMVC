package com.sw.finalProject.entity;

import com.sw.finalProject.dto.CommentDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@ToString
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne //다대일 관계
    @JoinColumn(name = "sale_id") //name 속성으로 매핑할 외래키 이름 지정(외래키 매핑)
    private Sale sale;

    @Column
    private String nickname; //닉네임

    @Column
    private String body; //댓글 내용

    public void patch(CommentDto dto) { //dto에 수정 내용 들어 있음.
        // 예외 발생
        if (this.id != dto.getId()) throw new IllegalArgumentException("댓글 수정 실패! 잘못된 id가 입력됐습니다.");

        // 객체 갱신
        if (dto.getNickname() != null) // 수정할 닉네임 데이터가 있다면
            this.nickname = dto.getNickname(); // 내용 반영(dto에 있는 내용을 복사함.)

        if (dto.getBody() != null) // 수정할 본문 데이터가 있다면
            this.body = dto.getBody(); // 내용 반영
    }
}
