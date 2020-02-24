package au.com.new1step.herbalist.profile;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("uat")
@Transactional
public class UATPropertyFileTest {

    @Autowired
    @Qualifier("envBean")
    private String envBean;

    @Autowired
    private DataSource dataSource;

    @Test
    public void testEnvProperty(){
        assertEquals("uat", envBean);
    }

    @Test
    public void testDatasource() throws SQLException {
        Assert.assertNotNull(dataSource);
        String url = dataSource.getConnection().getMetaData().getURL();
        Assert.assertTrue(url.contains("uat"));
    }

}


