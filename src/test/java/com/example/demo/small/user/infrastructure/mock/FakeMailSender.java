package com.example.demo.small.user.infrastructure.mock;

import com.example.demo.user.application.port.mail.MailSender;

public class FakeMailSender implements MailSender {

    public String email;
    public String title;
    public String content;

    @Override
    public void send(String email, String title, String content) {
        this.email = email;
        this.title = title;
        this.content = content;
    }

}
