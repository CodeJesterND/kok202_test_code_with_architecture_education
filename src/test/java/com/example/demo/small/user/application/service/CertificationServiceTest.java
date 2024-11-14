package com.example.demo.small.user.application.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.example.demo.small.mock.FakeMailSender;
import com.example.demo.user.application.service.CertificationService;
import org.junit.jupiter.api.Test;

class CertificationServiceTest {

    @Test
    void 이메일과_컨텐츠가_제대로_만들어져서_보내지는지_테스트한다() {
        // given
        FakeMailSender fakeMailSender = new FakeMailSender();
        CertificationService certificationService = new CertificationService(fakeMailSender);

        // when
        certificationService.send("dev.hyoseung@gmail.com", 1,"aaaaaaaa-aaaa-aaaa-aaaaaaaaaaaa");

        // then
        assertThat(fakeMailSender.email).isEqualTo("dev.hyoseung@gmail.com");
        assertThat(fakeMailSender.title).isEqualTo("Please certify your email address");
        assertThat(fakeMailSender.content).isEqualTo(
                "Please click the following link to certify your email address : http://localhost:8080/api/users/1/verify?certificationCode=aaaaaaaa-aaaa-aaaa-aaaaaaaaaaaa"
        );
    }

}
