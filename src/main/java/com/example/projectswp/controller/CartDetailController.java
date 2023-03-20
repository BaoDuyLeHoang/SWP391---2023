package com.example.projectswp.controller;

import com.example.projectswp.data_view_model.cartdetail.CartDetailConfirmVM;
import com.example.projectswp.data_view_model.cartdetail.CartDetailGetVM;
import com.example.projectswp.model.Blog;
import com.example.projectswp.model.CartDetails;
import com.example.projectswp.model.Items;
import com.example.projectswp.repositories.CartDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class CartDetailController {
    @Autowired
    CartDetailsRepository cartDetailsRepository;


    @GetMapping("/cartdetail")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<CartDetails>> getCartDetails(@RequestBody CartDetailGetVM cartDetailGetVM) {
        List<CartDetails> cartDetails = cartDetailsRepository.getCartDetails(cartDetailGetVM);
        return cartDetails != null ? ResponseEntity.ok(cartDetails) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    @PostMapping("/cartdetail/create")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CartDetails> createCartDetail(@RequestBody CartDetails addCartDetails) {
        boolean result = cartDetailsRepository.addCartDetails(addCartDetails);
        return result ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    @PutMapping("/cartdetail")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Object> updateCartDetail(@RequestBody CartDetails cartDetails) {
        try {
            boolean result = false;
            if (cartDetailsRepository.getCartDetail(cartDetails.getCartDetailId()) != null) {
                result = cartDetailsRepository.updateCartDetail(cartDetails);
            }
            return result ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @DeleteMapping("/cartdetail")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Object> deleteItem(@RequestBody CartDetails cartDetailsDelete){
        try {
            boolean result = cartDetailsRepository.deleteCartDetail(cartDetailsDelete);
            return result ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/cartdetail/confirm")
    public ResponseEntity<?> confirmCartDetail(@RequestBody CartDetailConfirmVM cartDetailConfirmVM) {
        return null;
    }


}
