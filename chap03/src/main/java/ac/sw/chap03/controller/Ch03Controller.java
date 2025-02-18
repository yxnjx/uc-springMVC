package ac.sw.chap03.controller;

import ac.sw.chap03.dto.ArticleForm;
import ac.sw.chap03.entity.Article;
import ac.sw.chap03.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Slf4j //log 사용하려면 있어야 됨.
@Controller
public class Ch03Controller {
    @Autowired
    ArticleRepository articleRepository;

    @GetMapping("/") //localhost:8088 or localhost:8088/
    public String hi(Model model) {
        //model.addAttribute("username", "홍길동");
        return "index"; //index.mustache
    }

    //게시판 새 글 등록 폼
    @GetMapping("/articles/new") //localhost:8088 + /articles/new
    public String newForm() {
        return "article/newForm"; //newForm.mustache
    }

    @PostMapping("/articles/create") //식판(DTO)을 쓰는 방법
    public String create2(ArticleForm dto, Model model) { //form : 식판 이름
        System.out.println("========== DTO ==========");
        System.out.println("제목: " + dto.getTitle());
        System.out.println("내용: " + dto.getContent());
        log.info("========== DTO ==========");
        log.info(dto.toString());

        //DTO(ArticleForm 식판) => 엔티티(Article)
        Article entity = dto.toEntity(); //dto 안에 toEntity라는 내용이 있음.

        //dto에 있는 내용을 꺼내서 Article 생성(dto에는 id가 없기 때문에 null을 넣음)
        //dto에 toEntity를 안 만드는 경우 -> 45번줄 대신 49번줄 작성
        //Article entity = new Article(null, dto.getTitle(), dto.getContent());

        //Article saved = articleRepository.save(entity); //ctrl -> save라는 메서드가 저장되어 있는 곳으로 감.

        //h2(수행할 때 생겼다가 종료하면 데이터 없어짐) 데이터베이스에 저장
        articleRepository.save(entity);
        model.addAttribute("username", "홍길동");

        return "index";
    }

    //article 전체 리스트 가져오기
    @GetMapping("/articles")
    public String getAllArticles(Model model) {
        //DB에 있는 모든 article 가져오기
        List<Article> allEntityList = articleRepository.findAll();

        //Model 에 담기
        model.addAttribute("list", allEntityList); //allEntityList라는 이름으로 model에 담김

        return "article/articleList";
    }
}