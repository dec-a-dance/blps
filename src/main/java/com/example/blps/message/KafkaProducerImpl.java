package com.example.blps.message;

import com.example.blps.entities.Order;
import com.example.blps.entities.OrderStatus;
import com.example.blps.message.model.ChangeOrderStatusMessage;
import com.example.blps.util.CustomSerializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class KafkaProducerImpl {
    private final KafkaProducer<Integer, ChangeOrderStatusMessage> producer;
    private final ObjectMapper mapper;

    public KafkaProducerImpl(){
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
        props.put("value.serializer", "com.example.blps.util.CustomSerializer");
        this.producer = new KafkaProducer<>(props);
        this.mapper = new ObjectMapper();
    }

    public boolean sendProductsAppeared(Order o){
        ChangeOrderStatusMessage mes = new ChangeOrderStatusMessage(o.getUser().getUsername(), o.getId(), o.getUser().getEmail(), null);
        try {
            String messageStr = mapper.writeValueAsString(mes);
            producer.send(new ProducerRecord<>("products-appeared", mes));
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
            producer.send(new ProducerRecord<>("status-changed", mes));
            return true;
        }
        catch(JsonProcessingException e){
            return false;
        }
    }
}
