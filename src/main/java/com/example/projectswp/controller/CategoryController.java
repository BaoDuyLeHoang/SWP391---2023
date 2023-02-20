package com.example.projectswp.controller;

import com.example.projectswp.model.CateAndSub;
import com.example.projectswp.model.Category;
import com.example.projectswp.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class CategoryController {
    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategory(@PathVariable int id) {
        Category category = categoryRepository.getCategory(id);
        return category != null ? ResponseEntity.ok(category) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("")
    public ResponseEntity<List<Category>> getCategories() {
        List<Category> list = categoryRepository.getCategories();
        return list != null ? ResponseEntity.ok(list) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Category> insertCategory(@RequestBody Category addCategory) {
        boolean result = categoryRepository.addCategory(addCategory);
        URI uri = URI.create("" + categoryRepository.getLastCategory().getId());
        return result ? ResponseEntity.created(uri).build() : ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Category> updateCategory(@PathVariable int id, @RequestBody Category updateCategory) {
        boolean result = false;
        if (categoryRepository.getCategory(id) != null) {
             result = categoryRepository.updateCategory(id, updateCategory);
        }
        return result ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

}
