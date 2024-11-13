package com.example.demo.user.service.port;

public interface MailSender {

    void send(String to, String subject, String content);
}
