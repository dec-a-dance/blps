package com.example.blps.shedule;

import com.example.blps.entities.*;
import com.example.blps.message.KafkaProducerImpl;
import com.example.blps.repositories.OrderProductRepository;
import com.example.blps.repositories.OrderRepository;
import com.example.blps.repositories.ProductRepository;
import com.example.blps.repositories.StorageRepository;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Properties;

@EnableScheduling
@Slf4j
@Service
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
    @Transactional
    public void checkStorage(){
        log.info("scheduled event");
        List<Order> orders = orderRepository.findAllByStatus(OrderStatus.NO_PRODUCTS);
        for (Order o : orders) {
            producer.sendProductsAppeared(o);
        }
    }
}

