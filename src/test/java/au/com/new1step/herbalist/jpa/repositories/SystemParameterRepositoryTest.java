package au.com.new1step.herbalist.jpa.repositories;

import au.com.new1step.herbalist.jpa.model.GlobalParameter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

@RunWith(SpringRunner.class)
@ActiveProfiles("local")
@SpringBootTest
@Transactional
public class SystemParameterRepositoryTest {
    @Autowired
    private GlobalParameterRepository globalParameterRepository;

    @Autowired
    public GlobalParameter globalParameter;

    @Test
    public void testAddSystemParameters(){
        globalParameterRepository.save(globalParameter);

        GlobalParameter systemParameter = globalParameterRepository.findBySystem("Herb");
        Assert.assertEquals("Herb", systemParameter.getSystem());
        Assert.assertEquals("2", new String(systemParameter.getNoOfDoctor()));
        Assert.assertEquals("90", new String(systemParameter.getAvailableDays()));

    }

   @Test
    public void testInt2Byte(){
       String test1 = "90";
       byte[] b1 = test1.getBytes(StandardCharsets.UTF_8);
       System.out.println("Andy-byte-array: " + b1);
       System.out.println("Andy-byte-array-to-string: " + new String(b1, StandardCharsets.UTF_8));

       test1 = "1";
       b1 = test1.getBytes(StandardCharsets.UTF_8);
       System.out.println("Andy-byte-array: " + b1);
       System.out.println("Andy-byte-array-to-string: " + new String(b1, StandardCharsets.UTF_8));


   }
}
