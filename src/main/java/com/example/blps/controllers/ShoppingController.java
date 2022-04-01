package com.example.blps.controllers;

import com.example.blps.dto.AddToCartDTO;
import com.example.blps.dto.OrderDTO;
import com.example.blps.dto.ProductDTO;
import com.example.blps.dto.UserOrderDTO;
import com.example.blps.entities.AuthToken;
import com.example.blps.services.ShoppingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/shop")
public class ShoppingController {
    private final ShoppingService shoppingService;

    @Autowired
    public ShoppingController(ShoppingService shoppingService){
        this.shoppingService=shoppingService;
    }

    @GetMapping("/getProducts")
    public ResponseEntity<List<ProductDTO>> getProducts(){
        List<ProductDTO> products = shoppingService.getProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PostMapping("/addToCart")
    public ResponseEntity<String> addToCart(@RequestBody AddToCartDTO dto){
        boolean isAdded = shoppingService.addToCart(dto.getProductId(), dto.getUsername(), dto.getCount());
        if(isAdded){
            return new ResponseEntity<>("Successfully added to your cart.", HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Your input is invalid :(", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/getMyOrders")
    public ResponseEntity<List<OrderDTO>> getOrders(@RequestBody AuthToken token){
        List<OrderDTO> orders = shoppingService.getUserOrders(token.getUsername());
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PostMapping("/confirmOrder")
    public ResponseEntity<String> confirmOrder(@RequestBody AuthToken token){
        boolean isConfirmed = shoppingService.confirmOrder(token.getUsername());
        if(isConfirmed){
            return new ResponseEntity<>("Successfully confirmed your order", HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Your input is invalid :(", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/deleteFromCart")
    public ResponseEntity<String> deleteFromCart(@RequestBody AddToCartDTO dto){
        boolean isDeleted = shoppingService.deleteFromCart(dto.getUsername(), dto.getProductId());
        if(isDeleted){
            return new ResponseEntity<>("Successfully deleted from your cart.", HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Your input is invalid :(", HttpStatus.BAD_REQUEST);
        }
    }

    //готовый запрос для корзины в сессии
    @PostMapping("/makeOrder")
    public ResponseEntity<String> makeOrder(@RequestBody UserOrderDTO dto){
        boolean isAdded = shoppingService.createOrderFromRequest(dto);
        if(isAdded){
            return new ResponseEntity<>("Successfully added your order.", HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Your input is invalid :(", HttpStatus.BAD_REQUEST);
        }
    }


}
