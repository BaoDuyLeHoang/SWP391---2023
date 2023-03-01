package com.example.projectswp.controller;

import com.example.projectswp.data_view_model.BlogGetVM;
import com.example.projectswp.data_view_model.BlogIdVM;
import com.example.projectswp.data_view_model.CreateBlogVM;
import com.example.projectswp.data_view_model.UpdateBlogVM;
import com.example.projectswp.model.Blog;
import com.example.projectswp.model.UserAccount;
import com.example.projectswp.repositories.BlogRepository;
import com.example.projectswp.repositories.ultil.Ultil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@EnableGlobalMethodSecurity(prePostEnabled = true)

public class BlogController {
    private static final int ACCEPT_STATUS = 1;
    private static final int DENY_STATUS = 2;
    private static final int DELETE_STATUS = 3;
    @Autowired
    BlogRepository blogRepository;

    @GetMapping("/blog/all")
    public ResponseEntity<List<Blog>> getBlogs() {
        List<Blog> blogs = blogRepository.getBlogs();
        return blogs != null? ResponseEntity.ok(blogs) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/useraccount/blog")
    public ResponseEntity<List<Blog>> getBlog() {
        int userID = Ultil.getUserId();
        List<Blog> blogs = blogRepository.getBlogByUserId(userID);
        return blogs != null ? ResponseEntity.ok(blogs) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("")
    public ResponseEntity<Object> getBlog(@RequestParam BlogGetVM blogGetVM) {
        List<Blog> blogs;
        if(blogGetVM.getCategoryId() != 0){
            blogs = blogRepository.getBlogByCategoryId(blogGetVM.getCategoryId());
            return blogs != null ? ResponseEntity.ok(blogs) : ResponseEntity.notFound().build();
        } else if(blogGetVM.getUserId() != 0){
            blogs = blogRepository.getBlogByUserId(blogGetVM.getUserId()) ;
            return blogs != null ? ResponseEntity.ok(blogs) : ResponseEntity.notFound().build();
        } else if(blogGetVM.getBlogId() != 0){
            Blog blog = blogRepository.getBlog(blogGetVM.getBlogId());
            return blog != null ? ResponseEntity.ok(blog) : ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("there no field so no blog");
    }

    @PutMapping("")
    public ResponseEntity<Blog> updateBlog(@RequestBody UpdateBlogVM blogVM){
        int uid = Ultil.getUserId();
        boolean isUpdated = blogRepository.updateBlog(uid, blogVM);
        return isUpdated ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("/blog/create")
    public ResponseEntity<Blog> createBlog(@RequestBody CreateBlogVM blogVM){
        int uid = Ultil.getUserId();
        boolean result = blogRepository.insertBlog(uid, blogVM);
        URI uri = URI.create("localhost:8080/api/blogs/" + blogRepository.getLastBlogId() );
        return result ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();//Todo: add return message
    }

    @PatchMapping("/blog/accept")
    public ResponseEntity<Blog> blogAccept(@RequestBody BlogIdVM blogIdVM){
        boolean isUpdated = blogRepository.updateStatus(blogIdVM.getBlogId(), ACCEPT_STATUS);
        return isUpdated ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    @PatchMapping("/blog/deny")
    public ResponseEntity<Blog> blogDeny(@RequestBody BlogIdVM blogIdVM){
        boolean isDeleted = blogRepository.updateStatus(blogIdVM.getBlogId(), DENY_STATUS);
        return isDeleted ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PatchMapping("/blog/delete")
    public ResponseEntity<Blog> deleteBlog(@RequestBody BlogIdVM blogIdVM){
        boolean isDeleted = blogRepository.updateStatus(blogIdVM.getBlogId(), DELETE_STATUS);
        return isDeleted ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    //TODO: test this function
    @PutMapping("/blog/test")
    public String test(@RequestBody BlogIdVM blogIdVM){

        return "" + blogIdVM.getBlogId();
    }


}
