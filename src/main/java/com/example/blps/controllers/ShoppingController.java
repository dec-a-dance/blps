package com.example.blps.controllers;

import com.example.blps.dto.*;
import com.example.blps.entities.AuthToken;
import com.example.blps.entities.Category;
import com.example.blps.entities.Product;
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

    //типа показать ассортимент
    @GetMapping("/getProducts")
    public ResponseEntity<List<ProductDTO>> getProducts(){
        List<ProductDTO> products = shoppingService.getProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    //добавить в корзину в бд
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

    //по факту нужно послать только username
    @GetMapping("/getMyOrders")
    public ResponseEntity<List<OrderDTO>> getOrders(@RequestBody AuthToken token){
        List<OrderDTO> orders = shoppingService.getUserOrders(token.getUsername());
        if(orders!=null) {
            return new ResponseEntity<>(orders, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }


    //оформить корзину в бд в заказ
    //по факту нужно послать только username
    @PostMapping("/confirmOrder")
    public ResponseEntity<String> confirmOrder(@RequestBody AuthToken token){
        boolean isConfirmed = shoppingService.confirmOrder(token.getUsername());
        if(isConfirmed){
            return new ResponseEntity<>("Successfully confirmed your order", HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Your input is invalid :( (maybe cart is empty)", HttpStatus.BAD_REQUEST);
        }
    }

    //удалить из корзины в бд
    @DeleteMapping("/deleteFromCart")
    public ResponseEntity<String> deleteFromCart(@RequestBody AddToCartDTO dto){
        int isDeleted = shoppingService.deleteFromCart(dto.getUsername(), dto.getProductId());
        if(isDeleted>0){
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

    //посмотреть что сейчас в корзине
    //по факту нужно послать только username
    @GetMapping("/getMyCart")
    public ResponseEntity<List<OrderPositionDTO>> getCart(@RequestBody AuthToken token){
        List<OrderPositionDTO> cart = shoppingService.getCart(token.getUsername());
        if(cart!=null) {
            return new ResponseEntity<>(cart, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getCategories")
    public ResponseEntity<List<Category>> getCategories(){
        List<Category> categories = shoppingService.getCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/getByCategory")
    public ResponseEntity<List<Product>> getByCategory(@RequestBody CategoryDTO dto){
        List<Product> products = shoppingService.getAllByCategory(dto.getId());
        if(products!=null) {
            return new ResponseEntity<>(products, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
