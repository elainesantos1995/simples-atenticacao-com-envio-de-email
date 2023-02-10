package com.jwt.gestaodeprojetos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jwt.gestaodeprojetos.config.EmailConfig;
import com.jwt.gestaodeprojetos.model.MensagemDeEmail;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Service
public class MailService {

    @Autowired
    private EmailConfig emailConfig;

    public void enviarMensagem(MensagemDeEmail mensagem) throws MessagingException {

        Session session = emailConfig.configureSessionEmail();

        int contDestinatarios = 0;
        int totalDestinatarios= mensagem.getDestinatarios().size();

        while(contDestinatarios < totalDestinatarios){
            try {
                Message message = prepararMensagem(session, mensagem, contDestinatarios);

                Transport.send(message);

                System.out.println("E-mail enviado com sucesso!");
                contDestinatarios++;

            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Message prepararMensagem(Session session, MensagemDeEmail mensagem, int contDestinatarios) throws AddressException, MessagingException{
        
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(mensagem.getRemetente()));
        message.setRecipients(Message.RecipientType.TO,
            InternetAddress.parse(mensagem.getDestinatarios().get(contDestinatarios)));
        message.setSubject(mensagem.getAssunto());
        message.setText(mensagem.getMensagem());

        return message;
    }

}
