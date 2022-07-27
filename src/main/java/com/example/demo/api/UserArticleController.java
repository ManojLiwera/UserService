package com.example.demo.api;

import com.amazonaws.services.cognitoidp.model.AuthenticationResultType;
import com.amazonaws.services.cognitoidp.model.ConfirmSignUpResult;
import com.amazonaws.services.cognitoidp.model.SignUpResult;
import com.example.demo.db.ArticleRepository;
import com.example.demo.db.DBConnection;
import com.example.demo.kafka.Article;
import com.example.demo.user.UserAuth;
import com.example.demo.user.UserSignupPayload;
import com.example.demo.user.WriterProfile;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping(path = "/signup")
    public ResponseEntity<Object> userSignup(@RequestBody UserSignupPayload userSignupPayload) {

        SignUpResult results;
        try {
            UserAuth userAuth = new UserAuth();
            results = userAuth.signUp(userSignupPayload.getUserName(), userSignupPayload.getEmail(), userSignupPayload.getPassword());

        }catch (Exception e){
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
        }

        DBConnection dbconn = new DBConnection();
        userSignupPayload.setUserRole("Reader");
        userSignupPayload.setSubscribedCategories("ML|MICRO");
        if(dbconn.AddUserToDB(userSignupPayload))
        {
            log.info("User Added");
        }
        if(results.isUserConfirmed()){
            return new ResponseEntity<>(userSignupPayload, HttpStatus.OK);
        }
        return new ResponseEntity<>("user UNCONFIRMED - please confirm user", HttpStatus.ACCEPTED);
    }

    @PostMapping(path = "/confirmsignup")
    public ResponseEntity<Object> userConfirmSignUp(@RequestBody UserSignupPayload userSignupPayload) {
        try {
            UserAuth userAuth = new UserAuth();
            userAuth.confirmSignUp(userSignupPayload.getEmail(), userSignupPayload.getSignConfirmationCode());

        }catch (Exception e){
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("user CONFIRMED", HttpStatus.ACCEPTED);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<Object> userLogin(@RequestBody UserSignupPayload userSignupPayload) {
        AuthenticationResultType results;
        try {
            UserAuth userAuth = new UserAuth();
            results = userAuth.login(userSignupPayload.getEmail(), userSignupPayload.getPassword());

        }catch (Exception e){
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Successfully login \nidToken : " + results.getIdToken()+ "\naccessToken : " + results.getAccessToken()+"\nrefreshToken : "+results.getRefreshToken(), HttpStatus.ACCEPTED);
    }

    @PostMapping(path = "/joinasawriter")
    public ResponseEntity<Object> writerProfilevalidation(@RequestBody WriterProfile writerProfile) {

        DBConnection dbconn = new DBConnection();
        if(dbconn.AddWriterToDB(writerProfile))
        {
            return new ResponseEntity<>("Writer Profile validation completed", HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>("Profile validation failed", HttpStatus.OK);

    }


}
