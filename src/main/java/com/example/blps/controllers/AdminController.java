package com.example.blps.controllers;

import com.example.blps.dto.OrderDTO;
import com.example.blps.dto.OrderRequest;
import com.example.blps.dto.OrderStatusDTO;
import com.example.blps.services.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Properties;

@Slf4j
@RestController
@RequestMapping("/admin")
@Tag(name="admin", description = "Controller for all admin operations. Only users with ADMIN role can use this.")
public class AdminController {
    private final AdminService adminService;
    private final Producer<String, String> producer;
    public AdminController(AdminService adminService) {
        this.adminService= adminService;
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        this.producer = new KafkaProducer(props);
    }

    @GetMapping("/orders/{status}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(description = "show all orders of certain status")
    public ResponseEntity<List<OrderDTO>> getOrdersByStatus(@PathVariable String status){
        OrderStatusDTO dto = new OrderStatusDTO();
        dto.setStatus(status);
        List<OrderDTO> orders = adminService.getOrdersByStatus(dto);
        if (orders!=null){
            return new ResponseEntity<>(orders, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/orders/send")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(description = "change order status to SENT")
    public ResponseEntity<String> sendOrder(@RequestBody OrderRequest req){
        if (adminService.sendOrder(req.getId())){
            return new ResponseEntity<>("Order have been sent successfully.", HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Something have gone wrong.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/orders/try-to-accept")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(description = "try to accept the order. Result - order became ACCEPTED or NO_PRODUCTS.")
    public ResponseEntity<String> tryToAccept(@RequestBody OrderRequest req){
        try{
            adminService.tryToAccept(req.getId());
            return new ResponseEntity<>("Order is trying to be accepted, check order status.", HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>("Something gone wrong", HttpStatus.BAD_REQUEST);
        }
    }
}
