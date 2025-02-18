package com.sw.chap11.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor //모든 변수가 다 들어 있는 생성자를 자동으로 만들어줌
@NoArgsConstructor //() 안에 매개 변수가 없는 생성자(기본 생성자)
@Setter
@Getter
@ToString //객체 내용을 하나하나 출력해 주는 기능 //toString을 오버라이드 안 해도 자동으로 만들어줌
@Entity //부팅 시 DB에 Article이라는 table 자동 생성 // 테이블 만드는 역할(필드들이 다 들어 있음)
//@Table(name = "board") //테이블 이름 변경
public class Article {
    //primary 키 역할(반드시 있어야 됨.)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //id 값을 자동으로 생성
    private Long id;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private String filename; //이미지 파일 이름

    public Article(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }
}
