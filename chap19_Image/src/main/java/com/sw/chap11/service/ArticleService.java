package com.sw.chap11.service;

import com.sw.chap11.dto.ArticleForm;
import com.sw.chap11.entity.Article;
import com.sw.chap11.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service //전체 구조에서 서비스 역할을 하는 객체를 뜻함.
//@Component (기능은 같음, 일반적인 객체로 만듦)
public class ArticleService {
    //서비스를 통해 요청하기 때문에 repository를 가지고 있어야 함.
    @Autowired
    private ArticleRepository articleRepository;

    public List<Article> getAll() {
        //List<Article> lists = articleRepository.findAll(); //전체 가져오는 명령

        List<Article> lists = (List<Article>)articleRepository.findAll();

        return lists;
    }

    //json 형태가 아니기 때문에 @RequestBody 있으면 안 됨.
    public Article create(ArticleForm dto) {
        if(dto.getId() != null) { //dto 에러 : id 값이 있으면 안 됨.
            return null;
        }

        //DTO(ArticleForm 식판) => 엔티티(Article)
        Article entity = dto.toEntity();

        Article savedArticle = articleRepository.save(entity);

        return savedArticle;
    }

    public Article getOne(Long id) {
        Article article = articleRepository.findById(id).orElse(null);

        return article;
    }

    public Article update(Long id, ArticleForm dto) {
        // 1. DTO -> Entity
        Article entity = dto.toEntity();

        // 2. 타깃 조회하기 -> 수정할 내용이 있는지 검사(ex. 5번 내용이 있는지 검사)
        Article target = articleRepository.findById(id).orElse(null);

        // 3. 잘못된 요청 처리하기 -> 타깃 조회했는데 없을 시 없다고 응답
        if(target == null) { //수정하려는 id가 없을 때
            return null;
        }

        // 4. 업데이트 및 정상적인 응답
        // 진짜 수정 내용은 entity에 들어 있음. but 내가 수정하려는 내용의 id 번호가 없음.
        entity.setId(id); //저장하기 전에 id를 먼저 찾아줘야 수정이 가능함.

        // 수정하지 않는 부분을 entity에 복사
        if(entity.getTitle() == null) entity.setTitle(target.getTitle()); //content만 수정할 경우 title은 가져옴.
        if(entity.getContent() == null) entity.setContent(target.getContent()); //title만 수정할 경우 content는 가져옴.

        Article updated = articleRepository.save(entity);

        return updated;
    }

    public Article deleteOne(Long id) {
        // 삭제하려는 내용의 id 찾기
        Article target = articleRepository.findById(id).orElse(null);

        if(target == null) { //삭제하려는 내용이 없을 때
            return null;
        } else { //삭제하려는 내용이 있을 때
            articleRepository.deleteById(id);
            return target;
        }
    }

    @Transactional
    public List<Article> createArticles(List<ArticleForm> dtos) {
        List<Article> savedList = new ArrayList<Article>(); //3건을 저장할 변수

        // 1. dto 3건을 entity로 변환
        for(int i = 0; i < dtos.size(); i++) {
            Article aa = dtos.get(i).toEntity();

            //강제적으로 에러 발생시키기
            if(i == 2) {
                // -1L(Long) 아이디를 찾는데 없으면 orElseThrow를 통해 에러 발생 시킴 -> 프로그램 중지
                // 에러 발생 이유를 직접 작성할 수 있음.
                articleRepository.findById(-1L).orElseThrow(() -> new IllegalArgumentException("강제 발생"));
            }

            // 2. DB 저장
            Article saved =  articleRepository.save(aa);
            savedList.add(saved);
        }

        // 3. 결과(저장된 3건) 리턴
        return savedList;
    }
}
