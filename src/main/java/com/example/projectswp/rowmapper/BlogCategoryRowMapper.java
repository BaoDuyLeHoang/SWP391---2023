package com.example.projectswp.rowmapper;

import com.example.projectswp.model.BlogCategory;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BlogCategoryRowMapper implements RowMapper<BlogCategory> {

    @Override
    public BlogCategory mapRow(ResultSet rs, int rowNum) throws SQLException {
        BlogCategory blogCategory = new BlogCategory();

        blogCategory.setId(rs.getInt("Blog_CategoryID"));
        blogCategory.setName(rs.getString("Blog_Category_Name"));

        return blogCategory;
    }
}
