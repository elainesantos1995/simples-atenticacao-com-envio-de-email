package com.jwt.gestaodeprojetos.config;

import java.util.Properties;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:env/mail.properties")
public class EmailConfig {

    @Autowired
    private Environment env;

    @Bean
    public Session configureSessionEmail(){
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        // Configura a sessão de e-mail
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(env.getProperty("mail.smtp.username"), env.getProperty("mail.smtp.password"));
                    }
                });
        
        return session;
    }
    
}
