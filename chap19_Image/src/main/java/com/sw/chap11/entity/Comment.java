package com.sw.chap11.entity;

import com.sw.chap11.dto.CommentDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor //생성자
@NoArgsConstructor //기본 생성자
@ToString //객체 내용 문자열로 출력
@Setter @Getter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //id값 자동 생성
    private Long id;

    @ManyToOne //다대일 관계
    @JoinColumn(name = "article_id") //name 속성으로 매핑할 외래키 이름 지정(외래키 매핑)
    private Article article; //Article 객체 지정(클래스, 객체를 가리킴)

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
