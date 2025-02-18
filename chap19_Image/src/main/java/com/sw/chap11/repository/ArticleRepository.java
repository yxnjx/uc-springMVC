package com.sw.chap11.repository;

import com.sw.chap11.entity.Article;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface ArticleRepository extends CrudRepository<Article, Long> {
    //@Override
    //ArrayList<Article> findAll();
}
