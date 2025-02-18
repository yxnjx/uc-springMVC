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
@Entity //부팅 시 DB에 Member이라는 table 자동 생성 // 테이블 만드는 역할(필드들이 다 들어 있음)
public class Member {
    @Id //primary 키 역할(반드시 있어야 됨.)
    @GeneratedValue //primary key 를 자동으로 생성?
    private Long id;

    @Column
    private String uid;

    @Column
    private String pwd;

    @Column
    private String tel;

    @Column
    private String age;
}
