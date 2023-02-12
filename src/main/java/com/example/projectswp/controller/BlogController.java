package com.example.projectswp.controller;

import com.example.projectswp.model.Blog;
import com.example.projectswp.repositories.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/blogs")

public class BlogController {

    @Autowired
    BlogRepository blogRepository;

    @GetMapping("")
    public ResponseEntity<List<Blog>> getBlogs() {
        List<Blog> blogs = blogRepository.getBlogs();
        return blogs != null? ResponseEntity.ok(blogs) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/{blogID}")
    public ResponseEntity<Blog> getBlog(@PathVariable int blogID) {
        Blog blog = blogRepository.getBlog(blogID);
        return blog != null? ResponseEntity.ok(blog) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("")
    public ResponseEntity<Blog> createBlog(@RequestBody Blog blog){
        boolean result = blogRepository.insertBlog(blog);
        URI uri = URI.create("localhost:8080/api/blogs/" + blogRepository.getLastBlogId() );
        return result ? ResponseEntity.created(uri).build() : ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    @PutMapping("/{blogId}")
    public ResponseEntity<Blog> updateBlog(@PathVariable int blogId, @RequestBody Blog blog){
        boolean isUpdated = blogRepository.updateBlog(blogId, blog);
        return isUpdated ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{blogId}")
    public ResponseEntity<Blog> deleteBlog(@PathVariable int blogId){
        boolean isDeleted = blogRepository.deleteBlog(blogId);
        return isDeleted ? ResponseEntity.accepted().build() : ResponseEntity.notFound().build();
    }

}
