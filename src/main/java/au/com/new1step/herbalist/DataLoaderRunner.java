package au.com.new1step.herbalist;

import au.com.new1step.herbalist.common.ApplicationBuilder;
import au.com.new1step.herbalist.jpa.model.GlobalParameter;
import au.com.new1step.herbalist.jpa.model.AppUser;
import au.com.new1step.herbalist.jpa.repositories.GlobalParameterRepository;
import au.com.new1step.herbalist.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;



@Component
public class DataLoaderRunner implements CommandLineRunner {

    @Autowired
    private GlobalParameterRepository globalParameterRepository;

    @Autowired
    private GlobalParameter globalParameter;

    @Autowired
    private AdminService adminService;


    private static final Logger logger = LoggerFactory.getLogger(DataLoaderRunner.class);

    @Transactional
    @Override
    public void run(String... args) throws Exception {

        if(adminService.initialFindGlobalParameterByDefaultSystem() == null){
             String admin = "admin";
             LocalDate today = LocalDate.now();
             globalParameter.setStartDate(today);

             LocalDate expireDate = today.plusDays(globalParameter.fetchAvailableDays());
             globalParameter.setExpiredDate(expireDate);

             adminService.createGlobalParameter(globalParameter);

             AppUser user = ApplicationBuilder.buildUser(admin, "$2a$10$tkuuFSURXIFMrVBpoTry8.8N0ErtE7eRwSyOSBzReDtpp4BZ7sPtK", "ROLE_ADMIN");
             user.setCreateUser(admin);
             user.setUpdateUser(admin);
             adminService.insertUser(user);
        }
    }

}
