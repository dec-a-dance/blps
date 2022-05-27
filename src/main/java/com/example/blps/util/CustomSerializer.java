package com.example.blps.util;

import com.example.blps.message.model.ChangeOrderStatusMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class CustomSerializer implements Serializer<ChangeOrderStatusMessage> {

    private Logger logger = LogManager.getLogger(this.getClass());

    @Override
    public void configure(Map map, boolean b) {

    }

    @Override
    public byte[] serialize(String s, ChangeOrderStatusMessage o) {
        byte[] retVal = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            retVal = objectMapper.writeValueAsBytes(o);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return retVal;
    }


    @Override
    public void close() {

    }
}
