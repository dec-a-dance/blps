package com.example.blps.util;

import com.example.blps.dto.TokenStorage;
import com.example.blps.entities.AuthToken;
import org.springframework.context.annotation.Bean;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class XmlReader {

    public boolean writeUser(AuthToken token) {
        StringWriter sw = new StringWriter();
        File file = new File("users.xml");
        if (!file.exists()) {
            try {
                file.createNewFile();
                TokenStorage newStor = new TokenStorage();
                newStor.setTokens(new ArrayList<>());
                JAXB.marshal(newStor, System.out);
                JAXB.marshal(newStor, file.getAbsoluteFile());
            }
            catch (IOException ex) {
                System.out.println(ex.getMessage());
                return false;
            }
            System.out.println("created empty file");
        }
        TokenStorage stor = JAXB.unmarshal(file.getAbsoluteFile(), TokenStorage.class);
        if(stor.getTokens()==null){
            stor.setTokens(new ArrayList<>());
        }
        stor.getTokens().add(token);
        JAXB.marshal(stor, file.getAbsoluteFile());
        return true;
    }

    public AuthToken getToken(String username) {
        File file = new File("users.xml");
        if (!file.exists()) {
            try {
                file.createNewFile();
                TokenStorage newStor = new TokenStorage();
                newStor.setTokens(new ArrayList<>());
                String test;
                JAXB.marshal(newStor, System.out);
                JAXB.marshal(newStor, file.getAbsoluteFile());
            }
            catch (IOException ex) {
                System.out.println(ex.getMessage());
                return null;
            }
            System.out.println("created empty file");
        }
        System.out.println("reading");
        TokenStorage stor = JAXB.unmarshal(file.getAbsoluteFile(), TokenStorage.class);
        JAXB.marshal(stor, file.getAbsoluteFile());
        AuthToken token = new AuthToken();
        if (stor.getTokens()==null){
            System.out.println("null");
            return null;
        }
        if (stor.getTokens().isEmpty()){
            System.out.println("empty");
            return null;
        }
        System.out.println("searching for token");
        System.out.println(stor.getTokens());
        System.out.println(username);
        for (AuthToken t: stor.getTokens()){
            if (Objects.equals(t.getUsername(), username)){
                System.out.println("token found!");
                return t;
            }
        }
        return null;
    }
}
