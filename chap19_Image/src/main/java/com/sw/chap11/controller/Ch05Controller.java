package com.sw.chap11.controller;

import com.sw.chap11.dto.ArticleForm;
import com.sw.chap11.dto.CommentDto;
import com.sw.chap11.entity.Article;
import com.sw.chap11.entity.Comment;
import com.sw.chap11.repository.ArticleRepository;
import com.sw.chap11.service.CommentService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Slf4j //log 사용하려면 있어야 됨.
@Controller
public class Ch05Controller {
    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    CommentService commentService;

    @GetMapping("/") //localhost:8088 or localhost:8088/
    public String hi(Model model) {
        //model.addAttribute("username", "홍길동");
        return "index"; //index.mustache
    }

    //게시판 새 글 등록 폼
    @GetMapping("/articles/new") //localhost:8088 + /articles/new

    public String newForm(HttpSession session, Model model) {
        //로그인 성공 검사 및 header.mustache에 전달
        String email = (String)session.getAttribute("email"); //session 값을 읽어내서 email이 있는지 없는지 검사
        if(email != null) { //이미 로그인한 사람
            model.addAttribute("email", email);
        }

        return "article/newImage"; //newImage.mustache
    }

    @PostMapping("/articles/create") //식판(DTO)을 쓰는 방법
    public String create2(ArticleForm dto, Model model, HttpSession session) { //form : 식판 이름
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
        Article savedArticle = articleRepository.save(entity);
        //model.addAttribute("username", "홍길동");

        //로그인 성공 검사 및 header.mustache에 전달
        String email = (String)session.getAttribute("email"); //session 값을 읽어내서 email이 있는지 없는지 검사
        if(email != null) { //이미 로그인한 사람
            model.addAttribute("email", email);
        }

        //return "index";
        return "redirect:/articles/" + savedArticle.getId(); //클라이언트한테 내가 넘겨준 주소로 다시 요청(다시 실행된 결과가 화면에 나타남)
                //localhost:8089/articles/2(아이디는 예를 들었음)
    }

    //article 전체 리스트 가져오기
    @GetMapping("/articles")
    public String getAllArticles(HttpSession session, Model model) {
        //DB에 있는 모든 article 가져오기
        List<Article> allEntityList = (List<Article>)articleRepository.findAll();

        //Model 에 담기
        model.addAttribute("list", allEntityList); //allEntityList라는 이름으로 model에 담김

        //로그인 성공 검사 및 header.mustache에 전달
        String email = (String)session.getAttribute("email"); //session 값을 읽어내서 email이 있는지 없는지 검사
        if(email != null) { //이미 로그인한 사람
            model.addAttribute("email", email);
        }

        return "article/articleList";
    }

    @GetMapping("/articles/{id}") // {path} - 변수 //넘어온 id 값을 변수로 받을 때 {} 로 받음
    public String getOne(@PathVariable Long id, Model model) { //Path로 넘어오기 때문에 @PathVariable이 꼭 있어야 됨.
        //System.out.println("id => " + id); //id 값 확인

        //DB에서 id에 해당하는 article을 가져옴
        //Article article = articleRepository.findById(id)에서 id 값이 없는 걸 요청 할 수도 있음.
        //id 번호가 db에 없는 걸 요청했을 때 null이 넘어옴(.orElse(null))
        //article은 정상적인 값을 받을 수도 있고 없는 값을 받을 수도 있음.
        Article article = articleRepository.findById(id).orElse(null);

        //System.out.println(article.toString()); //toString은 객체가 가지고 있는 내용을 알아서 출력

        //model 개체에 article을 저장하여 보냄
        model.addAttribute("article", article);

        //관련댓글(articleId)에 대한 댓글
        List<CommentDto> listDto = commentService.comments(id); //모든 댓글 복사하는 명령
        model.addAttribute("commentDtos", listDto); //commentDtos로 값을 넘김

        return "article/showDetail";
    }

    //article 삭제(id)
    @GetMapping("/articles/delete/{id}") //매개변수로 넘어오는 값을 변수로 받고 싶을 때 {}를 사용
    public String deleteArticle(@PathVariable Long id) { //{}로 값을 넘기면 @PathVariable로 받아야 됨.
        //id인 article을 삭제 명령
        articleRepository.deleteById(id);

        //return "index"; //삭제 후 home으로 돌아감
        return "redirect:/articles"; //삭제한 뒤 클라이언트에 응답을 주는데 다시 call함. 그때 주소가 /articles 임.
    }

    //article 수정(수정할 수 있는 페이지 보여주기)
    @GetMapping("/articles/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        //1. DB에서 수정 대상을 불러온다.(불러온 뒤 article에 저장)
        Article article = articleRepository.findById(id).orElse(null); //값이 있을 수도 없을 수도 있음

        //2. article을 수정하도록 view를 통해서 넘겨준다.(model 사용)
        model.addAttribute("article", article); //attributeName은 달라도 상관 없음.

        return "article/edit";
    }

    //수정된 내용을 받아서 DB에 수정하기(realUpdate)
    @PostMapping("/articles/realUpdate")
    public String realUpdate(ArticleForm dto) { //수정된 내용이 dto로 들어옴.
        System.out.println(dto.toString()); //맞게 들어왔는지 출력해봄.

        //dto -> entity로 옮김
        Article entityArticle = dto.toEntity(); //hidden된 id가 들어 있음.
        articleRepository.save(entityArticle); //id가 같은 것에 대해 수정(저장이라는 명령어지만 실제로는 수정임.)

        return "redirect:/articles"; //수정 후 전체 리스트 보여줌
    }

    //댓글 저장(생성)
    @PostMapping("/articles/comment")
    public String saveComment(CommentDto dto) { //form으로 받아온 값을 dto에 담음
        Comment saved = commentService.create(dto.getArticleId(), dto); //commentService.create - 댓글 저장

        return "redirect:/articles/" + dto.getArticleId(); //댓글 저장(생성) 후 redirect해서 댓글 작성한 글을 리턴해줌
    }
}














