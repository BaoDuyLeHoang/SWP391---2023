package com.example.projectswp.repositories.rowMapper;

import com.example.projectswp.model.Carts;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CartsRowMapper implements RowMapper<Carts> {
    @Override
    public Carts mapRow(ResultSet rs, int rowNum) throws SQLException {
        Carts carts = new Carts();
        carts.setCartID(rs.getInt("CartID"));
        carts.setUserID(rs.getInt("UserID"));
        carts.setAddress(rs.getString("Address"));
        return carts;
    }
}
