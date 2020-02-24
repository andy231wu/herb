package au.com.new1step.herbalist.jpa.repositories;

import au.com.new1step.herbalist.common.ApplicationBuilder;
import au.com.new1step.herbalist.jpa.model.Clinic;
import au.com.new1step.herbalist.jpa.model.ClinicAddress;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RunWith(SpringRunner.class)
@ActiveProfiles("local")
@SpringBootTest
@Transactional
public class ClinicAddressRepositoryTest {
    Logger log = LoggerFactory.getLogger(ClinicAddressRepositoryTest.class);

    @Autowired
    ClinicAddressRepository clinicAddressRepository;

    @Autowired
    ClinicRepository clinicRepository;

    @Test
    public void testAdd3Addresses(){
        add3Addresses();
        List<ClinicAddress> addresses = clinicAddressRepository.findAll();
        Assert.assertEquals(3, addresses.size());
    }


    @Test
    public void testFindBySuburb(){
        add3Addresses();
        List<ClinicAddress> addresses = clinicAddressRepository.findBySuburb("Gosford");
        Assert.assertEquals(1, addresses.size());
        Assert.assertEquals("26 Showground Ave", addresses.get(0).getAddress1());
        Assert.assertEquals("Eastwood Chinese Herb", addresses.get(0).getClinic().getChineseName());
    }

    private void add3Addresses(){
        ClinicAddress eastwoodAddr = ApplicationBuilder.buildClinicAddress("28 Matthew Ave", null, "Eastwood", "NSW", "2122", "Australia");
        ClinicAddress eppingAddr = ApplicationBuilder.buildClinicAddress("21 Audine Ave", null, "Epping", "NSW", "2121", "Australia");
        ClinicAddress gosfordAddr = ApplicationBuilder.buildClinicAddress("26 Showground Ave", null, "Gosford", "NSW", "2500", "Australia");

        Clinic eastwoodShop = ApplicationBuilder.buildClinic("Eastwood Chinese Herb", "Eastwood Chinese Herb");
        Clinic eppingShop = ApplicationBuilder.buildClinic("Epping Chinese Herb", "Epping Chinese Herb");
        clinicRepository.save(eastwoodShop);
        clinicRepository.save(eppingShop);

        eastwoodAddr.setClinic(eastwoodShop);
        eppingAddr.setClinic(eppingShop);
        gosfordAddr.setClinic(eastwoodShop);

        clinicAddressRepository.save(eastwoodAddr);
        clinicAddressRepository.save(eppingAddr);
        clinicAddressRepository.save(gosfordAddr);

    }


}
