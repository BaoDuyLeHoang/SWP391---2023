package com.example.projectswp.repositories;

import com.example.projectswp.model.Blog;
import com.example.projectswp.repositories.rowMapper.BlogRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Repository
public class BlogRepository {
    private static final BlogRowMapper BLOG_ROW_MAPPER = new BlogRowMapper();
    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<Blog> getBlogs() {
        String sql = "Select * from Blogs";
        List<Blog> blogs = jdbcTemplate.query(sql,BLOG_ROW_MAPPER);
        return blogs.size() != 0? blogs: null;
    }
    public List<Blog> getBlogs(int userID) {
        String sql = "select * from Blogs where UserID = ?";
        List<Blog> blogs = jdbcTemplate.query(sql,BLOG_ROW_MAPPER, userID);
        return blogs.size() != 0? blogs: null;
    }

    public Blog getBlog(int blogID){
        String sql = "select * from Blogs where BlogID = ?";
        List<Blog> blogs = jdbcTemplate.query(sql,BLOG_ROW_MAPPER, blogID);
        return blogs.size() != 0? blogs.get(0): null;
    }
    public boolean insertBlog(Blog blog) {
        String sql = "INSERT INTO dbo.Blogs(Blog_CategoryID, UserID, Blog_Title, Blog_Description, Blog_Content, Blog_Date_Create, Blog_Date_Update, Blog_Status) values (?, ?, ?, ?, ?, ?, ?, ?)";
        Date currentDate = getCurrentDate();
        int check = jdbcTemplate.update(sql, blog.getCategoryID(), blog.getUserID(), blog.getTitle(), blog.getDescription(), blog.getContent(), currentDate, currentDate, 1);
        return check > 0;
    }
    public Date getCurrentDate(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        return new Date(dtf.format(now));
    }

    public int getLastBlogId(){
        List<Blog> blogs = getBlogs();
        return (blogs != null) ? blogs.get(blogs.size() - 1).getID() : -1;
    }

    public boolean updateBlog(int blogId, Blog blog){
        Blog newBlog = updateBlogData(blogId, blog);
        String sql = "UPDATE dbo.Blogs set Blog_CategoryID = ?, UserID = ?, Blog_Title = ?, Blog_Description = ?, Blog_Content = ?, Blog_Date_Update = ? WHERE BlogID = ?";
        int rowAffected = jdbcTemplate.update(sql, newBlog.getCategoryID(), newBlog.getUserID(), newBlog.getTitle(), newBlog.getDescription(), newBlog.getContent(), getCurrentDate(), blogId);
        return rowAffected > 0;
    }
    public Blog updateBlogData(int id, Blog newBlog){
        Blog oldbBlog = getBlog(id);
        if(oldbBlog == null)
            return newBlog;

        if(newBlog.getCategoryID() == 0)
            newBlog.setCategoryID(oldbBlog.getCategoryID());

        if(newBlog.getUserID() == 0)
            newBlog.setUserID(oldbBlog.getUserID());

        if(newBlog.getTitle() == null || newBlog.getTitle().trim().length() == 0)
            newBlog.setTitle(oldbBlog.getTitle());

        if(newBlog.getDescription() == null || newBlog.getDescription().trim().length() == 0)
            newBlog.setDescription(oldbBlog.getDescription());

        if(newBlog.getContent() == null || newBlog.getContent().trim().length() == 0)
            newBlog.setContent(oldbBlog.getContent());

        return newBlog;
    }
    public boolean deleteBlog(int id){
        String sql = "DELETE dbo.Blogs WHERE BlogID = ?";
        int rowAffected = jdbcTemplate.update(sql, id);
        return rowAffected > 0;
    }
}
