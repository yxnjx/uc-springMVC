package ac.sw.chap03.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor //모든 변수가 다 들어 있는 생성자를 자동으로 만들어줌
@NoArgsConstructor //() 안에 매개 변수가 없는 생성자(기본 생성자)
@Setter
@Getter
@Entity //부팅 시 DB에 Article이라는 table 자동 생성 // 테이블 만드는 역할(필드들이 다 들어 있음)
//@Table(name = "board") //테이블 이름 변경
public class Article {
    //primary 키 역할(반드시 있어야 됨.)
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String title;

    @Column
    private String content;
}
