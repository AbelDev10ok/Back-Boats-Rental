package com.manager.boats.rental.boats_rental.configuration.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.manager.boats.rental.boats_rental.persistence.models.Role;
import com.manager.boats.rental.boats_rental.persistence.models.Users;
import com.manager.boats.rental.boats_rental.repositories.IRoleRepository;
import com.manager.boats.rental.boats_rental.repositories.IUserRepository;

import jakarta.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.modelmapper.*;

@Configuration
public class AppConfig {


    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


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

    @PostConstruct
    public void init() {
        if (userRepository.findByEmail("admin@example.com").isEmpty()) {
            Users user = new Users();
            user.setEmail("admin@example.com");
            user.setPassword(passwordEncoder.encode("adminpassword"));

            List<Role> roles = new  ArrayList<>();
            roles.add(roleRepository.findByName("ROLE_USER").orElseThrow(() -> new RuntimeException("Role not found")));
            roles.add(roleRepository.findByName("ROLE_ADMIN").orElseThrow(() -> new RuntimeException("Role not found")));

            user.setRoles(roles);
            userRepository.save(user);
        }
    }

}
