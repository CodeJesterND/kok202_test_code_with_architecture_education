package com.example.demo.user.application.port.mail;

public interface MailSender {

    void send(String to, String subject, String content);
}
