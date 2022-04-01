package com.example.blps.services;

import com.example.blps.dto.*;
import com.example.blps.entities.*;
import com.example.blps.repositories.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
    private final CategoryRepository categoryRepository;

    public ShoppingService(OrderProductRepository orderProductRepository,
                           ShoppingCartRepository shoppingCartRepository,
                           OrderRepository orderRepository,
                           UserRepository userRepository,
                           ProductRepository productRepository,
                           CategoryRepository categoryRepository){
        this.orderRepository = orderRepository;
        this.orderProductRepository = orderProductRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public boolean addToCart(long productId, String username, long count){
        log.debug("add to cart");
        try {
            log.debug("Add to cart");
            ShoppingCart cart = new ShoppingCart();
            cart.setCount(count);
            User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("No such user"));
            log.debug("User found.");
            cart.getKey().setUser(user);
            Product product = productRepository.findById(productId).orElseThrow(() -> new UsernameNotFoundException("No such product"));
            log.debug("Product found.");
            cart.getKey().setProduct(product);
            cart.setConfirmed(false);
            shoppingCartRepository.save(cart);
            return true;
        }
        catch(UsernameNotFoundException e){
            return false;
        }
    }

    public boolean confirmOrder(String username){
        try {
            User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("No such user"));
            List<ShoppingCart> carts = shoppingCartRepository.findAllByKeyUserAndConfirmed(user, false);
            Order order = new Order();
            order.setUser(user);
            Order fullOrder = orderRepository.save(order);
            for (ShoppingCart c : carts) {
                OrderProduct ent = new OrderProduct();
                ent.setCount(c.getCount());
                ent.getKey().setOrder(fullOrder);
                ent.getKey().setProduct(c.getKey().getProduct());
                orderProductRepository.save(ent);
            }
            return true;
        }
        catch(UsernameNotFoundException e){
            return false;
        }
    }

    public List<OrderDTO> getUserOrders(String username){
        try {
            User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("No such user"));
            List<Order> orders = orderRepository.findAllByUser(user);
            List<OrderDTO> response = new ArrayList<OrderDTO>();
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
                dto.setUserId(user.getId());
                dto.setId(o.getId());
                dto.setProducts(positions);
                response.add(dto);
            }
            return response;
        }
        catch(UsernameNotFoundException e){
            return null;
        }
    }

    @Transactional
    public int deleteFromCart(String username, long prodId){
        try {
            User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("No such user"));
            Product product = productRepository.findById(prodId).orElseThrow(() -> new UsernameNotFoundException("No such product"));
            return shoppingCartRepository.deleteByKey_UserAndKey_ProductAndConfirmed(user, product, false);
        }catch(UsernameNotFoundException e) {
            return 0;
        }
        }

    public boolean createOrderFromRequest(UserOrderDTO dto){
        try {
            Order order = new Order();
            User user = userRepository.findByUsername(dto.getUsername()).orElseThrow(() -> new UsernameNotFoundException("No such user"));
            order.setUser(user);
            Order fullOrder = orderRepository.save(order);
            for (UserOrderPositionDTO d : dto.getProducts()) {
                Product product = productRepository.findById(d.getProductId()).orElseThrow(() -> new UsernameNotFoundException("No such product"));
                OrderProduct op = new OrderProduct();
                op.setCount(d.getCount());
                op.getKey().setOrder(fullOrder);
                op.getKey().setProduct(product);
                orderProductRepository.save(op);
            }
        }
        catch(UsernameNotFoundException e){
            return false;
        }
        return true;
    }

    public List<ProductDTO> getProducts(){
        List<Product> products = productRepository.findAll();
        List<ProductDTO> out = new ArrayList<>();
        for (Product p: products){
            ProductDTO dto = new ProductDTO();
            dto.setName(p.getName());
            dto.setCategory(p.getCategory().getName());
            dto.setPrice(p.getPrice());
            dto.setId(p.getId());
            out.add(dto);
        }
        return out;
    }


    public List<OrderPositionDTO> getCart(String username){
        try {
            User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("No such user"));
            List<ShoppingCart> cart = shoppingCartRepository.findAllByKeyUserAndConfirmed(user, false);
            List<OrderPositionDTO> out = new ArrayList<>();
            for (ShoppingCart c : cart) {
                OrderPositionDTO dto = new OrderPositionDTO();
                dto.setCategoryName(c.getKey().getProduct().getCategory().getName());
                dto.setCount(c.getCount());
                dto.setName(c.getKey().getProduct().getName());
                dto.setPrice(c.getKey().getProduct().getPrice());
                dto.setId(c.getKey().getProduct().getId());
                out.add(dto);
            }
            return out;
        }
        catch(UsernameNotFoundException e){
            return null;
        }
    }

    public List<Category> getCategories(){
        return categoryRepository.findAll();
    }

    public List<Product> getAllByCategory(long categoryId){
        try {
            Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new UsernameNotFoundException("No such category"));
            return productRepository.findAllByCategory(category);
        }catch(UsernameNotFoundException e) {
            return null;
        }
    }
}
