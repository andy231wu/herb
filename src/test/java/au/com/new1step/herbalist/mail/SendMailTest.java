package au.com.new1step.herbalist.mail;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("local")
@javax.transaction.Transactional
public class SendMailTest  {
    private Logger log = LoggerFactory.getLogger(SendMailTest.class);

    @Test
    public void testSendEmailViaMainEngine() {
      //
    }

    // send email working when test by SendMailTest, however when run testSendEmailViaLog4j,
    // it will not be working why ????
    @Test
    public void testSendEmailViaLog4j() {
        log.error("I HAVE ERROR-8888 - SEND ME EMAIL NO from Spring Boot Project");
    }


    @Test
    public void testLog4jEmail(){
        Logger log = LoggerFactory.getLogger(SendMailTest.class);
        log.error("I HAVE ERROR - SEND ME EMAIL");
    }
}
