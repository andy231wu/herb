package au.com.new1step.herbalist.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.mail.SimpleMailMessage;
        import org.springframework.mail.javamail.JavaMailSender;
        import org.springframework.scheduling.annotation.Async;
        import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("emailNewService")
@Transactional
public class EmailServiceImpl {

    private JavaMailSender mailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendEmail(SimpleMailMessage email) {
        mailSender.send(email);
    }
}