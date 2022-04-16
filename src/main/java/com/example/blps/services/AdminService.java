package com.example.blps.services;

import com.example.blps.dto.OrderDTO;
import com.example.blps.dto.OrderPositionDTO;
import com.example.blps.dto.OrderStatusDTO;
import com.example.blps.entities.Order;
import com.example.blps.entities.OrderProduct;
import com.example.blps.entities.OrderStatus;
import com.example.blps.entities.Product;
import com.example.blps.exceptions.WrongStatusException;
import com.example.blps.repositories.OrderProductRepository;
import com.example.blps.repositories.OrderRepository;
import com.example.blps.repositories.StorageRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class AdminService {
    private final TransactionTemplate transactionTemplate;
    private StorageRepository storageRepository;
    private OrderRepository orderRepository;
    private OrderProductRepository orderProductRepository;

    public List<OrderDTO> getOrdersByStatus(OrderStatusDTO input){
        try {
            List<Order> orders = orderRepository.findAllByStatus(OrderStatus.valueOf(input.getStatus()));
            List<OrderDTO> out = new ArrayList<>();
            for (Order o : orders) {
                List<OrderProduct> products = orderProductRepository.findAllByKey_Order(o);
                List<OrderPositionDTO> positions = new ArrayList<OrderPositionDTO>();
                for (OrderProduct p : products) {
                    OrderPositionDTO pos = new OrderPositionDTO();
                    Product prod = p.getKey().getProduct();
                    pos.setId(prod.getId());
                    pos.setName(prod.getName());
                    pos.setPrice(prod.getPrice());
                    pos.setCount(p.getCount());
                    pos.setCategoryName(prod.getCategory().getName());
                    positions.add(pos);
                }
                OrderDTO dto = new OrderDTO();
                dto.setUserId(o.getUser().getId());
                dto.setId(o.getId());
                dto.setProducts(positions);
                dto.setStatus(o.getStatus().name());
                out.add(dto);
            }
            return out;
        }
        catch(IllegalArgumentException e){
            log.warn("Wrong status in request");
            return null;
        }
    }

    public boolean tryToAccept(long orderId){
        //todo transactional realisation
        return true;
    }

    public boolean sendOrder(long orderId){
        try {
            Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Wrong id"));
            if (order.getStatus() == OrderStatus.ACCEPTED) {
                order.setStatus(OrderStatus.SENT);
                return true;
            } else {
                throw new WrongStatusException("Wrong status");
            }
        }
        catch(Exception e){
            return false;
        }
    }
}
