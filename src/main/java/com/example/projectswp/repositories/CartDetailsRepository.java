package com.example.projectswp.repositories;

import com.example.projectswp.data_view_model.cartdetail.CartDetailGetVM;
import com.example.projectswp.model.CartDetails;
import com.example.projectswp.model.Carts;
import com.example.projectswp.model.Items;
import com.example.projectswp.repositories.rowMapper.CartDetailsRowMapper;
import com.example.projectswp.repositories.ultil.Ultil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Repository
public class CartDetailsRepository {
    private static final CartDetailsRowMapper  CART_DETAILS_ROW_MAPPER  = new CartDetailsRowMapper();
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    RequestRepository requestRepository;
    @Autowired
    CartRepository cartRepository;
    public Date getCurrentDate(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        return new Date(dtf.format(now));
    }
    public List<CartDetails> getCartDetails(CartDetailGetVM cartDetailGetVM) {
        List<CartDetails> cartDetails = new ArrayList<>();
        for (int cartId : cartDetailGetVM.getListCartDetailID()) {
            CartDetails cartDetail = getCartDetail(cartId);
            if (cartDetail != null) {
                cartDetails.add(cartDetail);
            }
        }
        return cartDetails;
    }
    public CartDetails getCartDetail(int cartDetailsID) {
        String sql = "select * from CartDetails where CartDetailID = ?";
        List<CartDetails> cartDetails = jdbcTemplate.query(sql,CART_DETAILS_ROW_MAPPER, cartDetailsID);
        return cartDetails.size() != 0? cartDetails.get(0): null;
    }
    public List<CartDetails> getCartDetails() {
        String sql = "Select * from CartDetails where CartID = ?";
        List<CartDetails> cartDetails = jdbcTemplate.query(sql,CART_DETAILS_ROW_MAPPER, cartRepository.getCarts().get(0).getCartID());
        return cartDetails.size() != 0? cartDetails: null;
    }

    public boolean addCartDetails(CartDetails cartDetails) {
        String sql = "insert into dbo.CartDetails ([CartID], [ItemID], [ItemQuantity])\n" +
                "values (?, ?, ?)";

        int check = jdbcTemplate.update(sql, cartRepository.getCarts().get(0).getCartID(), cartDetails.getItemId(), 1);
        return check != 0;
    }
    public boolean updateCartDetail(CartDetails cartDetails) {
        String sql = "update dbo.CartDetails\n" +
                "set ItemQuantity = ?\n" +
                "where CartDetailID = ? AND CartID = ?";
        int check = jdbcTemplate.update(sql,cartDetails.getItemQuantity(), cartDetails.getCartDetailId(), cartRepository.getCarts().get(0).getCartID());
        return check != 0;
    }

    public boolean deleteCartDetail(int id){
        String sql = "DELETE dbo.CartDetails WHERE CartDetailID = ?";
        int check = jdbcTemplate.update(sql, id);
        return check > 0;
    }

    public boolean cartDetailtoRequest(String note, int cartDetailId, int userID) {
        CartDetails cartDetails = this.getCartDetail(cartDetailId);
        boolean check = requestRepository.addRequest(userID, cartDetails.getItemQuantity(),cartDetails.getItemId(),
                cartRepository.getAddress(userID), note, Ultil.getCurrentDate(),0, null);
        return check  ;
    }
}