package com.sw.chap11.controller;

import com.sw.chap11.dto.ArticleForm;
import com.sw.chap11.dto.CommentDto;
import com.sw.chap11.entity.Article;
import com.sw.chap11.entity.Comment;
import com.sw.chap11.repository.ArticleRepository;
import com.sw.chap11.repository.CommentRepository;
import com.sw.chap11.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Slf4j
@RestController //데이터만 응답해주는 서버 만들 때 사용
public class ApiController {
    //@Autowired
    //ArticleRepository articleRepository;

    @Autowired
    ArticleService articleService;

    @Autowired
    CommentRepository commentRepository;

    /*
    //전체 목록
    @GetMapping("/api/articles")
    public List<Article> getAllArticles() {
        //DB에 있는 모든 article 가져오기
        //List<Article> allEntityList = articleRepository.findAll();
        List<Article> allEntityList = articleService.getAll();

        return allEntityList; //리턴 타입이 ResponseEntity가 아니기 때문에 상태코드 내가 선택 못함.
    }
    */

    //전체 목록(응답코드 조절)
    @GetMapping("/api/articles")
    //응답 코드 내 맘대로 조절하기 위해 ResponseEntity 사용
    public ResponseEntity<List<Article>> getAllArticles2() {
        //DB에 있는 모든 article 가져오기
        List<Article> allEntityList = articleService.getAll();

        return ResponseEntity.status(HttpStatus.OK).body(allEntityList);
        //상태코드는 OK
        //ResponseEntity의 body 안에 allEntityList가 들어감.
        //상태코드 내가 직접 선택할 수 있음.
    }

    /*
    //게시판 글 등록(save)
    @PostMapping("/api/articles") //식판(DTO)을 쓰는 방법
    public Article create(@RequestBody ArticleForm dto) { //form : 식판 이름
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

        //h2(수행할 때 생겼다가 종료하면 데이터 없어짐) 데이터베이스에 저장
        Article savedArticle = articleRepository.save(entity);

        return savedArticle;
    }
    */

    // 게시판 글 등록(응답코드 조절)
    @PostMapping("/api/articles") //식판(DTO)을 쓰는 방법
    public ResponseEntity<Article> create(@RequestBody ArticleForm dto) {
        Article savedArticle = articleService.create(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedArticle);
    }

    // id 값에 맞는 게시판
    @GetMapping("/api/articles/{id}")
    public ResponseEntity<Article> getOne(@PathVariable Long id) {
        Article aaa = articleService.getOne(id);

        if(aaa == null) { //id가 없는 것 요청한 경우
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(aaa);
        }
    }

    // 수정 요청(수정된 내용 다시 보내줌)
    @PatchMapping("/api/articles/{id}")
    public ResponseEntity<Article> update(@PathVariable Long id, @RequestBody ArticleForm dto) { // json 데이터로 받을 때는 @RequestBody 사용
        Article updatedArticle = articleService.update(id, dto);

        if(updatedArticle == null) { //id가 없는 것을 수정 요청했을 때
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(updatedArticle);
        }
    }

    // 삭제 요청
    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<Article> deleteOne(@PathVariable Long id) {
        Article deleted = articleService.deleteOne(id);

        if(deleted == null) { //id가 없는 것을 수정 요청했을 때
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(deleted);
        }
    }

    //트랜잭션 테스트
    @PostMapping("/api/tr-test")
    public ResponseEntity<List<Article>> trTest(@RequestBody List<ArticleForm> dtos) { //3건이 dtos에 저장
        //여러 데이터를 한 번에 받음(데이터 3건을 List 형태로 저장)
        List<Article> lists = articleService.createArticles(dtos);

        return ResponseEntity.status(HttpStatus.CREATED).body(lists);
    }

    /*
    //findByArticleId() 테스트
    @GetMapping("/api/articles/{article_id}/comments")
    public ResponseEntity<List<Comment>> getAllComments(@PathVariable Long article_id) {
        //commentRepository.findAllById(id); //comment id 값에 맞는 글 하나만 가지고 옴.(article_id 값에 맞는 글을 모두 가져오는 거 X)
        List<Comment> lists = commentRepository.findByArticleId(article_id);

        return ResponseEntity.status(HttpStatus.OK).body(lists);
    }

    @GetMapping("/api/articles/{article_id}/comments2")
    public ResponseEntity<List<CommentDto>> getAllComments2(@PathVariable Long article_id) {
        //commentRepository.findAllById(id); //comment id 값에 맞는 글 하나만 가지고 옴.(article_id 값에 맞는 글을 모두 가져오는 거 X)
        List<Comment> lists = commentRepository.findByArticleId(article_id);
        List<CommentDto> listDto = new ArrayList<CommentDto>();

        for (int i = 0; i < lists.size(); i++) {
            CommentDto dto = CommentDto.createCommentDto(lists.get(i)); //입력은 comment -> dto 변경
            listDto.add(dto);
        }

        return ResponseEntity.status(HttpStatus.OK).body(listDto);
    }

    //findByNickname() 테스트
    @GetMapping("/api/comments/nickname/{name}")
    public ResponseEntity<List<Comment>> getAllCommentsByNickname(@PathVariable String name) {
        List<Comment> lists = commentRepository.findByNickname(name);

        return ResponseEntity.status(HttpStatus.OK).body(lists);
    }
    */
}



























