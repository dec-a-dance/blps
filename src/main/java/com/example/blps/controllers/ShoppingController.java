package com.example.blps.controllers;

import com.example.blps.dto.*;
import com.example.blps.entities.AuthToken;
import com.example.blps.entities.Category;
import com.example.blps.entities.Product;
import com.example.blps.security.JwtUtil;
import com.example.blps.services.ShoppingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/shop")
@Tag(name="shopping", description = "all shopping operations")
public class ShoppingController {
    private final ShoppingService shoppingService;
    private final JwtUtil jwt;

    @Autowired
    public ShoppingController(ShoppingService shoppingService, JwtUtil jwt){
        this.shoppingService=shoppingService;
        this.jwt = jwt;
    }

    //типа показать ассортимент
    @GetMapping("/products")
    @Operation(description = "get the list of all products")
    public ResponseEntity<List<ProductDTO>> getProducts(){
        List<ProductDTO> products = shoppingService.getProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    //добавить в корзину в бд
    @PostMapping("/cart")
    @Operation(description = "add product to the cart")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<String> addToCart(@RequestHeader("Authorization") String auth, @RequestBody AddToCartDTO dto){
        boolean isAdded = shoppingService.addToCart(dto.getProductId(), jwt.subjectFromToken(auth.substring(7)), dto.getCount());
        if(isAdded){
            return new ResponseEntity<>("Successfully added to your cart.", HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Your input is invalid :(", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/orders")
    @Operation(description = "get the list of your orders")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<OrderDTO>> getOrders(@RequestHeader("Authorization") String auth){
        List<OrderDTO> orders = shoppingService.getUserOrders(jwt.subjectFromToken(auth.substring(7)));
        if(orders!=null) {
            return new ResponseEntity<>(orders, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }


    //оформить корзину в бд в заказ
    //по факту нужно послать только username
    @PostMapping("/orders/confirm")
    @Operation(description = "confirm your cart as an order")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<String> confirmOrder(@RequestHeader("Authorization") String auth){
        boolean isConfirmed = shoppingService.confirmOrder(jwt.subjectFromToken(auth.substring(7)));
        if(isConfirmed){
            return new ResponseEntity<>("Successfully confirmed your order", HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Your input is invalid :( (maybe cart is empty)", HttpStatus.BAD_REQUEST);
        }
    }

    //удалить из корзины в бд
    @DeleteMapping("/cart/{id}")
    @Operation(description = "delete product from the cart")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<String> deleteFromCart(@RequestHeader("Authorization") String auth, @PathVariable Long id){
        int isDeleted = shoppingService.deleteFromCart(jwt.subjectFromToken(auth.substring(7)), id);
        if(isDeleted>0){
            return new ResponseEntity<>("Successfully deleted from your cart.", HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Your input is invalid :(", HttpStatus.BAD_REQUEST);
        }
    }

    //посмотреть что сейчас в корзине
    //по факту нужно послать только username
    @GetMapping("/cart")
    @Operation(description = "get the list of products in your cart")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<OrderPositionDTO>> getCart(@RequestHeader("Authorization") String auth){
        List<OrderPositionDTO> cart = shoppingService.getCart(jwt.subjectFromToken(auth.substring(7)));
        if(cart!=null) {
            return new ResponseEntity<>(cart, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/categories")
    @Operation(description = "get the list of categories")
    public ResponseEntity<List<Category>> getCategories(){
        List<Category> categories = shoppingService.getCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/categories/{id}")
    @Operation(description = "get the list of products of certain category")
    public ResponseEntity<List<Product>> getByCategory(@PathVariable Long id){
        List<Product> products = shoppingService.getAllByCategory(id);
        if(products!=null) {
            return new ResponseEntity<>(products, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
