package com.sw.chap11.service;

import com.sw.chap11.dto.ArticleForm;
import com.sw.chap11.entity.Article;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ArticleServiceTest {
    @Autowired
    ArticleService articleService;

    @Test //테스트를 위한 코드
    void getAll() {
        // 1. 예상 데이터
        Article a = new Article(1L, "가가가", "111");
        Article b = new Article(2L, "나나나", "222");
        Article c = new Article(3L, "다다다", "333");
        Article d = new Article(4L, "라라라", "444");

        // a, b, c를 리스트로 만들어서 expected에 저장(강제적으로 만듦)
        List<Article> expected = new ArrayList<Article>(Arrays.asList(a, b, c, d));

        // 2. 실제 데이터
        List<Article> articles = articleService.getAll();

        // 3. 1과 2 비교(같으면 성공, 다르면 실패)
        assertEquals(expected.toString(), articles.toString());
    }

    @Test
    void getOne_성공_존재하는_id_입력() { //ArticleService의 getOne 테스트
        // 1. 예상 데이터
        Article expected = new Article(2L, "나나나", "222");

        // 2. 실제 데이터
        Article article = articleService.getOne(2L);

        // 3. 1, 2 비교
        assertEquals(expected.toString(), article.toString());
    }

    @Test
    void getOne_성공_존재하지않는_id_입력() {
        // 1. 예상 데이터
        Article expected = null;

        // 2. 실제 데이터
        Article article = articleService.getOne(0L);

        // 3. 1, 2 비교
        assertEquals(expected, article); //값이 null이기 때문에 .toString을 넣으면 안 됨
    }

    @Test
    @Transactional //테스트하기 위해 넣었던 데이터를 다시 없앰(원래대로 복귀)
    void create_성공() { //dto에 id값이 없음(데이터 입력 시 id 값을 넣지 않아야 성공)
        // 1. 예상 데이터
        String title = "마마마";
        String content = "555";
        Article expected = new Article(8L, title, content);

        // 2. 실제 데이터
        ArticleForm dto = new ArticleForm(null, title, content);
        Article real = articleService.create(dto);

        // 3. 비교
        assertEquals(expected.toString(), real.toString());
    }

    @Test
    @Transactional
    void create_실패() { //값을 입력할 때 id 값이 들어가 있으면 에러가 남.(실패)
        // 1. 예상 데이터
        String title = "바바바";
        String content = "666";
        Article expected = null; //new Article(4L, title, content);

        // 2. 실제 데이터
        ArticleForm dto = new ArticleForm(4L, title, content);
        Article real = articleService.create(dto); // real = id 값이 들어 있기 때문에 null

        // 3. 비교
        assertEquals(expected, real);
    }

    @Test
    void deleteOne_id가있는것() {
        // 1. 예상 데이터
        Article expected = new Article(1L, "가가가", "111");

        // 2. 실제 데이터
        Article del = articleService.deleteOne(1L);

        // 3. 비교
        assertEquals(expected.toString(), del.toString());
    }

    @Test
    void deleteOne_id가없는것() {
        // 1. 예상 데이터
        Article expected = null;

        // 2. 실제 데이터
        Article del = articleService.deleteOne(0L);

        // 3. 비교
        assertEquals(expected, del);
    }
}


