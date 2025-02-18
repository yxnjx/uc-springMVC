package com.sw.chap11.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.sw.chap11.dto.ArticleForm;
import com.sw.chap11.entity.Article;
import com.sw.chap11.repository.ArticleRepository;
import com.sw.chap11.service.ArticleService;
import com.sw.chap11.service.UploadFileService;

@Controller
public class FileUploadController {

    @Autowired
    UploadFileService uploadFileService;
    
    @Autowired
    ArticleRepository articleRepository;

    @PostMapping("/articles/uploadImage")
    public String uploadImage(ArticleForm dto, @RequestParam("file") MultipartFile file) { // @RequestParam("file") => newImage의 이미지 name과 같아야 됨.
        // SAVE FILE
        String savedFileName = uploadFileService.upload(file); //저장된 파일 이름을 savedFileName에 넘겨줌
        
        if (savedFileName != null) { //정상적으로 저장이 됐을 경우
            Article article = dto.toEntity(); //dto -> Entity
            article.setFilename(savedFileName); //filename -> savedFileName 세팅
            articleRepository.save(article); //db 저장
        } else {
            System.out.println("파일저장실패");   
        }
        return "index";
    }
}
