package com.example.projectswp.repositories;

import com.example.projectswp.model.CartDetails;
import com.example.projectswp.model.Carts;
import com.example.projectswp.model.Items;
import com.example.projectswp.repositories.rowMapper.CartDetailsRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
@Repository
public class CartDetailsRepository {
    private static final CartDetailsRowMapper  CART_DETAILS_ROW_MAPPER  = new CartDetailsRowMapper();
    @Autowired
    JdbcTemplate jdbcTemplate;
    public Date getCurrentDate(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        return new Date(dtf.format(now));
    }
    public CartDetails getLastCartDetails() {
        List<CartDetails> list = getCartDetails();
        return list.size() != 0 ? list.get(list.size()-1) : null;
    }
    public CartDetails getCartDetail(int cartDetailsID) {
        String sql = "select * from CartDetails where Cart_DetailID = ?";
        List<CartDetails> cartDetails = jdbcTemplate.query(sql,CART_DETAILS_ROW_MAPPER, cartDetailsID);
        return cartDetails.size() != 0? cartDetails.get(0): null;
    }
    public List<CartDetails> getCartDetails() {
        String sql = "Select * from CartDetails";
        List<CartDetails> cartDetails = jdbcTemplate.query(sql,CART_DETAILS_ROW_MAPPER);
        return cartDetails.size() != 0? cartDetails: null;
    }

    public boolean addCartDetails(CartDetails cartDetails) {
        String sql = "insert into dbo.CartDetails ([Cart_DetailID], [CartID], [ItemID], [Cart_Detail_Date_Create], [Cart_Detail_Date_Update], [Cart_Detail_Item_Quantity])\n" +
                "values (?, ?, ?, ?, ?, ?)";

        int check = jdbcTemplate.update(sql, cartDetails.getCartDetailsID(), cartDetails.getCartID(), cartDetails.getItemID(), getCurrentDate(), null, cartDetails.getCartDetailItemQuantity());
        return check != 0;
    }
    public boolean updateCartDetail(CartDetails cartDetails) {
        String sql = "update dbo.Items\n" +
                "set Cart_Detail_Item_Quantity = ?,\n" +
                "    Cart_Detail_Date_Update = ?,\n" +
                "where Cart_DetailID = ?";
        int check = jdbcTemplate.update(sql,cartDetails.getCartDetailItemQuantity(), getCurrentDate(), cartDetails.getCartDetailsID() );
        return check != 0;
    }
    public boolean deleteCartDetail(CartDetails cartDetails){
        String sql = "DELETE dbo.CartDetails WHERE Cart_DetailID = ?";
        int check = jdbcTemplate.update(sql, cartDetails.getCartDetailsID());
        return check > 0;
    }
}