package com.manager.boats.rental.boats_rental.configuration.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

import org.modelmapper.*;

@Configuration
public class AppConfig {


    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public JavaMailSender javaMailSender() {


        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost("smtp.gmail.com"); // O el host de tu proveedor de correo
        mailSender.setPort(587); // O el puerto correcto para tu proveedor
        mailSender.setUsername(System.getenv("EMAIL_USERNAME")); // Tu dirección de correo
        mailSender.setPassword(System.getenv("EMAIL_PASSWORD")); //  Contraseña de aplicación para Gmail
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true"); //  Habilita el logging para depuración

        //Si usas SSL en lugar de TLS:
        //props.put("mail.smtp.ssl.enable", "true");
        //props.put("mail.smtp.socketFactory.port", "465");
        //props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        return mailSender;
    }

}
