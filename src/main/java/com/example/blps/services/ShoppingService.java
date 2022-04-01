package com.example.blps.services;

import com.example.blps.dto.OrderDTO;
import com.example.blps.dto.OrderPositionDTO;
import com.example.blps.entities.*;
import com.example.blps.repositories.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ShoppingService {
    private final OrderProductRepository orderProductRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public ShoppingService(OrderProductRepository orderProductRepository,
                           ShoppingCartRepository shoppingCartRepository,
                           OrderRepository orderRepository,
                           UserRepository userRepository,
                           ProductRepository productRepository){
        this.orderRepository = orderRepository;
        this.orderProductRepository = orderProductRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public void addToCart(long productId, String username, long count){
            ShoppingCart cart = new ShoppingCart();
            cart.setCount(count);
            User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("No such user"));
            cart.getKey().setUser(user);
            Product product = productRepository.findById(productId).orElseThrow(() -> new UsernameNotFoundException("No such product"));
            cart.getKey().setProduct(product);
            cart.setConfirmed(false);
            shoppingCartRepository.save(cart);
    }

    public void confirmOrder(String username){
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("No such user"));
        List<ShoppingCart> carts = shoppingCartRepository.findAllByKeyUserAndConfirmed(user, false);
        Order order = new Order();
        order.setUser(user);
        Order fullOrder = orderRepository.save(order);
        for (ShoppingCart c: carts){
            OrderProduct ent = new OrderProduct();
            ent.setCount(c.getCount());
            ent.getKey().setOrder(fullOrder);
            ent.getKey().setProduct(c.getKey().getProduct());
            orderProductRepository.save(ent);
        }
    }

    public List<OrderDTO> getUserOrders(String username){
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("No such user"));
        List<Order> orders = orderRepository.findAllByUser(user);
        List<OrderDTO> response = new ArrayList<OrderDTO>();
        for (Order o: orders){
            List<OrderProduct> products = orderProductRepository.findAllByKey_Order(o);
            List<OrderPositionDTO> positions = new ArrayList<OrderPositionDTO>();
            for (OrderProduct p: products){
                OrderPositionDTO pos = new OrderPositionDTO();
                Product prod = p.getKey().getProduct();
                pos.setId(prod.getId());
                pos.setName(prod.getName());
                pos.setPrice(prod.getPrice());
                pos.setCategoryName(prod.getCategory().getName());
                positions.add(pos);
            }
            OrderDTO dto = new OrderDTO();
            dto.setUserId(user.getId());
            dto.setId(o.getId());
            dto.setProducts(positions);
            response.add(dto);
        }
        return response;
    }
}
