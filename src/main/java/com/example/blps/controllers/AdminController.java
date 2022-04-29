package com.example.blps.controllers;

import com.example.blps.dto.OrderDTO;
import com.example.blps.dto.OrderRequest;
import com.example.blps.dto.OrderStatusDTO;
import com.example.blps.services.AdminService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin")
@AllArgsConstructor

//todo realization of accepting an offer
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/ordersByStatus")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<OrderDTO>> getOrdersByStatus(@RequestBody OrderStatusDTO dto){
        List<OrderDTO> orders = adminService.getOrdersByStatus(dto);
        if (orders!=null){
            return new ResponseEntity<>(orders, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/sendOrder")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> sendOrder(@RequestBody OrderRequest req){
        if (adminService.sendOrder(req.getId())){
            return new ResponseEntity<>("Order have been sent successfully.", HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Something have gone wrong.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/tryToAccept")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> tryToAccept(@RequestBody OrderRequest req){
        try{
            boolean result = adminService.tryToAccept(req.getId());
            if(result){
                return new ResponseEntity<>("Order accepted", HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>("Not enough products in storage", HttpStatus.OK);
            }
        }
        catch(Exception e){
            return new ResponseEntity<>("Something gone wrong", HttpStatus.BAD_REQUEST);
        }
    }
}
