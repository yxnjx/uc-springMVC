package ac.sw.chap03.dto;

import ac.sw.chap03.entity.Article;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

//DTO : 식판
@AllArgsConstructor //모든 변수가 다 들어 있는 생성자를 자동으로 만들어줌
@NoArgsConstructor //() 안에 매개 변수가 없는 생성자(기본 생성자)
@Setter @Getter
@ToString //안에 들어 있는 내용을 제목은 뭔지 내용은 뭔지 자세하게 알려줌
public class ArticleForm {
    private String title; //html에서 사용한 변수의 이름과 같아야 됨.
    private String content;
    public Article toEntity() {
        return new Article(null, title, content);
    }
}
