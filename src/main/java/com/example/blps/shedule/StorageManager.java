package com.example.blps.shedule;

import com.example.blps.entities.*;
import com.example.blps.repositories.OrderProductRepository;
import com.example.blps.repositories.OrderRepository;
import com.example.blps.repositories.ProductRepository;
import com.example.blps.repositories.StorageRepository;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Properties;

@EnableScheduling
public class StorageManager {
    private final TransactionTemplate transactionTemplate;
    private final StorageRepository storageRepository;
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final Producer producer;

    public StorageManager(TransactionTemplate transactionTemplate,
                          StorageRepository storageRepository,
                          OrderRepository orderRepository,
                          OrderProductRepository orderProductRepository){
        this.transactionTemplate = transactionTemplate;
        this.storageRepository = storageRepository;
        this.orderRepository = orderRepository;
        this.orderProductRepository = orderProductRepository;
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        this.producer = new KafkaProducer<>(props);
    }

    @Scheduled(fixedRate=120000)
    public Boolean checkStorage(){
        return (Boolean) transactionTemplate.execute(new TransactionCallback() {
            @Override
            public Boolean doInTransaction(TransactionStatus status) {
                List<Order> orders = orderRepository.findAllByStatus(OrderStatus.NO_PRODUCTS);
                for (Order o : orders) {
                    List<OrderProduct> products = orderProductRepository.findAllByKey_Order(o);
                    for (OrderProduct p : products) {
                        Product prod = p.getKey().getProduct();
                        Storage stor = storageRepository.findByProduct(prod).orElseThrow(() -> new RuntimeException("Wrong storage"));
                        stor.setCount(stor.getCount() - p.getCount());
                        storageRepository.save(stor);
                    }
                    o.setStatus(OrderStatus.ACCEPTED);
                    orderRepository.save(o);
                }
                return true;
            }
    });
    }
}
