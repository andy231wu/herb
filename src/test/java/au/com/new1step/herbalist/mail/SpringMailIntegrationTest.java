package au.com.new1step.herbalist.mail;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@ActiveProfiles("local")
@SpringBootTest
@Transactional
public class SpringMailIntegrationTest {

    @Autowired
    private EmailService emailService;

    @Rule
    public SmtpServerRule smtpServerRule = new SmtpServerRule(2626);

    @Test
    public void shouldSendSingleMail() throws MessagingException, IOException {
        // this can send real email as localhost email system did not setup
        Mail mail = new Mail();
        mail.setFrom("no-reply@memorynotfound.com");
        mail.setTo("andy.wu@property.nsw.gov.au");
        mail.setSubject("Spring Mail Integration Testing with GreenMail Example");
        mail.setContent("999111-We show how to write Integration Tests using Spring and GreenMail.");

        emailService.sendMessageViaTestMailSender(mail);
        mail.setContent("999222-We show how to write Integration Tests using Spring and GreenMail.");
        emailService.sendMessageViaTestMailSender(mail);


        MimeMessage[] receivedMessages = smtpServerRule.getMessages();
        assertEquals(2, receivedMessages.length);

        MimeMessage current = receivedMessages[1];

        assertEquals(mail.getSubject(), current.getSubject());
        assertEquals(mail.getTo(), current.getAllRecipients()[0].toString());
        assertTrue(String.valueOf(current.getContent()).contains(mail.getContent()));
    }

    @Test
    public void shouldSendRealMail() throws MessagingException, IOException {
        Mail mail = new Mail();
        mail.setFrom("no-reply@memorynotfound.com");
        mail.setTo("andy.wu@property.nsw.gov.au");
        mail.setSubject("Spring Mail Integration Testing with GreenMail Example");
        mail.setContent("99999-We show how to write Integration Tests using Spring and GreenMail.");

        emailService.sendRealMessage(mail);
        // check your eamil box to see if email has arrived
        assertTrue(true);
    }

}
