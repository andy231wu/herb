package au.com.new1step.herbalist.service;

import au.com.new1step.herbalist.exception.EntityNotFoundException;
import au.com.new1step.herbalist.jpa.model.GlobalParameter;
import au.com.new1step.herbalist.service.impl.AdminServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@ActiveProfiles("local")
@SpringBootTest
@Transactional
public class AdminServiceTest {

    @Autowired
    private AdminServiceImpl adminService;

    @Test
    public void canDisplayGlobalParameter() throws EntityNotFoundException {
         GlobalParameter globalParameter = adminService.findGlobalParameterByDefaultSystem();
         Assert.assertNotNull(globalParameter);
         System.out.println(globalParameter.toString());
    }

    @Test
    public void canEditGlobalParmeter() throws EntityNotFoundException {
        GlobalParameter globalParameter = adminService.findGlobalParameterByDefaultSystem();
        System.out.println("Before changed: " + globalParameter.toString());
        globalParameter.setNoOfDoctor("5".getBytes());
        globalParameter.setAvailableDays("365".getBytes());
        adminService.updateGlobalParameter(globalParameter);

        GlobalParameter globalParameter1 = adminService.findGlobalParameterByDefaultSystem();
        System.out.println("After changed: " + globalParameter1.toString());
        Assert.assertEquals(5, globalParameter1.fetchNoOfDoctor().intValue());
        Assert.assertEquals(365, globalParameter1.fetchAvailableDays().intValue());
    }

    // create Shop Owner login id

}
