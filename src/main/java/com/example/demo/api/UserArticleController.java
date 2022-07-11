package com.example.demo.api;

import com.example.demo.db.ArticleRepository;
import com.example.demo.kafka.Article;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/userarticles")
public class UserArticleController {

    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping
    public List<Article> findArticles(){
        return articleRepository.findAll();
    }

    @SneakyThrows
    @GetMapping("/{id}")
    public Article findArticle(@PathVariable String id){
        Article article = articleRepository.findById(id).orElseThrow(() -> new ExpressionException("Can not found the Article"));
        return article;
    }
}
