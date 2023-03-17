package com.example.projectswp.repositories;

import com.example.projectswp.model.Blog;
import com.example.projectswp.model.BlogCategory;
import com.example.projectswp.repositories.rowMapper.BlogCategoryRowMapper;
import com.google.errorprone.annotations.Var;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class BlogCategoryRepository {
    private static final BlogCategoryRowMapper BLOG_CATEGORY_ROW_MAPPER = new BlogCategoryRowMapper();
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    BlogRepository blogRepository;

    public List<BlogCategory> getBlogCategories() {
        String sql = "select * from BlogCategories";
        List<BlogCategory> categories = jdbcTemplate.query(sql, BLOG_CATEGORY_ROW_MAPPER);
        return categories.size() != 0? categories : null;
    }

    public BlogCategory getBlogCategory(int blogCategoryId) {
        String sql = "SELECT * from BlogCategories WHERE Blog_CategoryID = ?";
        List<BlogCategory> categories = jdbcTemplate.query(sql, BLOG_CATEGORY_ROW_MAPPER, blogCategoryId);
        return categories.size() != 0? categories.get(0) : null;
    }

    public boolean insertBlogCategory(String blogCateName) {
        if(blogCateName == null || blogCateName.trim().length() == 0)
            return false;

        String sql = "INSERT INTO dbo.BlogCategories(Blog_Category_Name) VALUES(?)";
        int rowAffected = jdbcTemplate.update(sql, blogCateName.trim());
        return rowAffected > 0;
    }
    public int getLastBlogCategoryID() {
        List<BlogCategory> blogCategories = getBlogCategories();
        return blogCategories.get( blogCategories.size()-1 ).getBlogCategoryId();
    }

    public boolean updateBlogCategory(int blogCategoryID, BlogCategory blogCategory) {
        String sql = "UPDATE dbo.BlogCategories SET Blog_Category_Name = ? WHERE Blog_CategoryID = ?";
        int rowAffected = jdbcTemplate.update(sql, blogCategory.getBlogCategoryName(), blogCategoryID);

        return rowAffected > 0;
    }

    public boolean deleteBlogCategory(int blogCategoryId) {
        String sql = "DELETE dbo.BlogCategories WHERE Blog_CategoryID = ?";
        int rowAffected = jdbcTemplate.update(sql ,blogCategoryId);
        return rowAffected > 0;
    }
    public boolean isExistName(String name){
        var list = getBlogCategories();
        if(list == null)
            return false;
        for(BlogCategory blogCategory : list){
            if(blogCategory.getBlogCategoryName().equals(name)) {
                return true;
            }
        }
        return false;
    }
    private void addBlogListToBlogCategory(List<BlogCategory> blogCategories) {
        if(blogCategories == null)
            return;
        for(BlogCategory blogCategory : blogCategories) {
        }
    }
}
