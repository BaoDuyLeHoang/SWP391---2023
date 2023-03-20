package com.example.projectswp.repositories.rowMapper;

import com.example.projectswp.model.CartDetails;
import com.example.projectswp.model.Carts;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CartDetailsRowMapper implements RowMapper<CartDetails> {
    @Override
    public CartDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
        CartDetails cartDetails = new CartDetails();
        cartDetails.setCartDetailID(rs.getInt("Cart_DetailID"));
        cartDetails.setCartId(rs.getInt("CartID"));
        cartDetails.setItemId(rs.getInt("ItemID"));
        cartDetails.setItemQuantity(rs.getInt("Cart_Detail_Item_Quantity"));
        return cartDetails;
    }
}
