package com.example.blps.message;

import com.example.blps.entities.Order;
import com.example.blps.entities.OrderStatus;
import com.example.blps.message.model.AbstractMessage;
import com.example.blps.message.model.ChangeOrderStatusMessage;
import com.example.blps.message.model.ProductsAppearedMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.context.annotation.Bean;

import java.util.Properties;

public class KafkaProducerImpl {
    private final KafkaProducer<Integer, String> producer;
    private final ObjectMapper mapper;

    public KafkaProducerImpl(){
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("key.serializer", "org.apache.kafka.common.serialization.IntSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        this.producer = new KafkaProducer<>(props);
        this.mapper = new ObjectMapper();
    }

    public boolean sendProductsAppeared(Order o){
        ProductsAppearedMessage mes = new ProductsAppearedMessage(o.getUser().getUsername(), o.getId(), o.getUser().getEmail());
        try {
            String messageStr = mapper.writeValueAsString(mes);
            producer.send(new ProducerRecord<>("products-appeared", mes.hashCode(), messageStr));
            return true;
        }
        catch(JsonProcessingException e){
            return false;
        }

    }

    public boolean sendChangeOrderStatus(Order o, OrderStatus newStatus) {
        ChangeOrderStatusMessage mes = new ChangeOrderStatusMessage(o.getUser().getUsername(), o.getId(), o.getUser().getEmail(), newStatus);
        try {
            String messageStr = mapper.writeValueAsString(mes);
            producer.send(new ProducerRecord<>("status-changed", mes.hashCode(), messageStr));
            return true;
        }
        catch(JsonProcessingException e){
            return false;
        }
    }
}
