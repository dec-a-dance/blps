package com.example.blps;

import com.example.blps.message.KafkaProducerImpl;
import com.example.blps.util.XmlReader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class BlpsApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(BlpsApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(BlpsApplication.class);
	}

	@Bean
	protected XmlReader xmlReader(){
		return new XmlReader();
	}

	@Bean
	protected KafkaProducerImpl kafkaProducerImpl (){
		return new KafkaProducerImpl();
	}
}

