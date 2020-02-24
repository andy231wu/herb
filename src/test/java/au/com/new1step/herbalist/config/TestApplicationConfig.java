package au.com.new1step.herbalist.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class TestApplicationConfig {

    @Bean
    public JavaMailSenderImpl testMailSender()  {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("localhost");
        mailSender.setUsername("username");
        mailSender.setPassword("secret");
        mailSender.setPort(2626);
        return mailSender;
    }
}
