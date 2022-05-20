package com.example.blps.shedule;

import com.example.blps.entities.*;
import com.example.blps.message.KafkaProducerImpl;
import com.example.blps.repositories.OrderProductRepository;
import com.example.blps.repositories.OrderRepository;
import com.example.blps.repositories.ProductRepository;
import com.example.blps.repositories.StorageRepository;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Properties;

@EnableScheduling
public class StorageManager {
    private final TransactionTemplate transactionTemplate;
    private final StorageRepository storageRepository;
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final KafkaProducerImpl producer;

    public StorageManager(TransactionTemplate transactionTemplate,
                          StorageRepository storageRepository,
                          OrderRepository orderRepository,
                          OrderProductRepository orderProductRepository,
                          KafkaProducerImpl producer){
        this.transactionTemplate = transactionTemplate;
        this.storageRepository = storageRepository;
        this.orderRepository = orderRepository;
        this.orderProductRepository = orderProductRepository;
        this.producer = producer;
    }

    @Scheduled(fixedRate=120000)
    public void checkStorage(){
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
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
                    producer.sendProductsAppeared(o);
                }
            }
    });
    }
}
