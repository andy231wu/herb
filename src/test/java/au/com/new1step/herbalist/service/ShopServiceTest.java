package au.com.new1step.herbalist.service;

import au.com.new1step.herbalist.HerbalistApplication;
import au.com.new1step.herbalist.common.ApplicationBuilder;
import au.com.new1step.herbalist.config.ApplicationConfig;
import au.com.new1step.herbalist.jpa.model.*;
import au.com.new1step.herbalist.jpa.repositories.ClinicAddressRepository;
import au.com.new1step.herbalist.jpa.repositories.GlobalParameterRepository;
import au.com.new1step.herbalist.service.impl.ShopServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import unit.au.com.new1step.herbalist.service.ShopService;

import java.util.List;


@RunWith(SpringRunner.class)
@ActiveProfiles("local")
@SpringBootTest
@Transactional
public class ShopServiceTest {

    /*
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = ApplicationConfig.class)
@TestPropertySource(
        locations = "classpath:application.properties")

    */

    @Autowired
    private ShopServiceImpl shopService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private ClinicAddressRepository clinicAddressRepository;

    /* ------ Clinic ------*/
    @Test
    public void canSaveClinic(){
        // given
        Clinic clinic = ApplicationBuilder.buildClinic("Chinese Shop1", "English Shop1");

        // when
         Clinic clinic1 = shopService.saveClinic(clinic);
         // then
         Assert.assertTrue(clinic1.getCID() != null);
    }

    @Test
    public void cannotSaveClinicReachMax(){
        // given
        Clinic clinic1 = ApplicationBuilder.buildClinic("Chinese Shop1", "English Shop1");
        Clinic clinic2 = ApplicationBuilder.buildClinic("Chinese Shop2", "English Shop2");

        // when
        Clinic clinic = shopService.saveClinic(clinic1);
        Assert.assertTrue(clinic.getCID() != null);

        // when
        clinic = shopService.saveClinic(clinic2);
        Assert.assertTrue(clinic == null);
    }

    /* ----- Address -----*/
    @Test
    public void canSaveClinicWithAddress(){
        // given
        Clinic clinic = ApplicationBuilder.buildClinic("Chinese Shop1", "English Shop1");
        ClinicAddress eastwoodAddr = ApplicationBuilder.buildClinicAddress("28 Matthew Ave", null, "Eastwood", "NSW", "2122", "Australia");
        clinic.addAddress(eastwoodAddr);
        // when
        Clinic clinic1 = shopService.saveClinic(clinic);
        // then
        Assert.assertTrue(clinic1.getCID() != null);

        Clinic theClinic = shopService.findClinicByChineseName("Chinese Shop1");
        Assert.assertEquals("English Shop1", theClinic.getEnglishName());
        Assert.assertEquals("28 Matthew Ave", theClinic.getAddresses().get(0).getAddress1());
    }

    @Test
    public void canSaveClinicWithTwoAddress(){
        // given
        Clinic clinic = ApplicationBuilder.buildClinic("Chinese Shop1", "English Shop1");
        shopService.saveClinic(clinic);
        Assert.assertTrue(clinic.getCID() != null);

        Clinic clinic1 = shopService.findClinicByChineseName("Chinese Shop1");

        ClinicAddress eastwoodAddr = ApplicationBuilder.buildClinicAddress("28 Matthew Ave", null, "Eastwood", "NSW", "2122", "Australia");
        ClinicAddress eppingAddr = ApplicationBuilder.buildClinicAddress("21 Audine Ave", null, "Epping", "NSW", "2121", "Australia");
        clinic1.addAddress(eastwoodAddr);
        clinic1.addAddress((eppingAddr));

        shopService.updateClinic(clinic);

        Clinic theClinic = shopService.findClinicByChineseName("Chinese Shop1");
        Assert.assertEquals("English Shop1", theClinic.getEnglishName());
        Assert.assertEquals("28 Matthew Ave", theClinic.getAddresses().get(0).getAddress1());
        Assert.assertEquals("21 Audine Ave", theClinic.getAddresses().get(1).getAddress1());
    }

    /* ---- doctor ---*/

    @Test
    public void canSaveDoctor(){
        // given
        Clinic clinic = ApplicationBuilder.buildClinic("Chinese Shop1", "English Shop1");
        ClinicAddress eastwoodAddr = ApplicationBuilder.buildClinicAddress("28 Matthew Ave", null, "Eastwood", "NSW", "2122", "Australia");
        clinic.addAddress(eastwoodAddr);
        // when
        Clinic clinic1 = shopService.saveClinic(clinic);
        // then
        Assert.assertTrue(clinic1.getCID() != null);

        // add doctgor to clinic
        Doctor doctor = ApplicationBuilder.buildDoctor("Andy", "Wu", "0402902617", 15.00f);
        doctor.setClinicAddress(eastwoodAddr);
        Clinic clinic2 = shopService.findClinicByChineseName("Chinese Shop1");
        clinic2.addDoctor(doctor);

        Clinic clinic3 = shopService.findClinicByChineseName("Chinese Shop1");
        Assert.assertEquals("0402902617", clinic3.getDoctors().get(0).getMobile());

    }

    @Test
    public void cannotAddDoctorAsReachMax(){
        Clinic clinic = ApplicationBuilder.buildClinic("Chinese Shop1", "English Shop1");
        ClinicAddress eastwoodAddr = ApplicationBuilder.buildClinicAddress("28 Matthew Ave", null, "Eastwood", "NSW", "2122", "Australia");
        clinic.addAddress(eastwoodAddr);
        Clinic clinic1 = shopService.saveClinic(clinic);
        Assert.assertTrue(clinic1.getCID() != null);

        Doctor doctor1 = ApplicationBuilder.buildDoctor("Andy", "Wu", "0402902617", 15.00f);
        doctor1.setClinicAddress(eastwoodAddr);
        Doctor doctor2 = ApplicationBuilder.buildDoctor("Andy", "Wu", "0402902617", 20.00f);
        doctor2.setClinicAddress(eastwoodAddr);
        Doctor doctor3 = ApplicationBuilder.buildDoctor("Ethan", "Tang", "0421991372", 15.00f);
        doctor3.setClinicAddress(eastwoodAddr);

        Clinic clinic2 = shopService.findClinicByChineseName("Chinese Shop1");
        clinic2.addDoctor(doctor1);                                           // add 1 doctor
        Clinic clinic3 = shopService.updateClinic(clinic2);
        Assert.assertEquals(1, clinic3.getDoctors().size());
        Assert.assertNotNull(clinic3.getDoctors().get(0).getDrID());

        Clinic clinic4 = shopService.findClinicByChineseName("Chinese Shop1");
        clinic4.addDoctor(doctor2);
        shopService.updateClinic(clinic4); // add 2nd doctor
        Assert.assertEquals(2, clinic4.getDoctors().size());
        Assert.assertNotNull(clinic4.getDoctors().get(0).getDrID());
        Assert.assertNotNull(clinic4.getDoctors().get(1).getDrID());
        Assert.assertEquals("28 Matthew Ave", clinic4.getDoctors().get(0).getClinicAddress().getAddress1());
        Assert.assertEquals("28 Matthew Ave", clinic4.getDoctors().get(1).getClinicAddress().getAddress1());

        // reach max of doctors
        Clinic clinic5 = shopService.findClinicByChineseName("Chinese Shop1");
        clinic5.addDoctor(doctor3);
        shopService.updateClinic(clinic5); // add 1 doctor
        Assert.assertEquals(2, clinic5.getDoctors().size());

        // find a address for doctors
        ClinicAddress clinicAddress = clinicAddressRepository.findBySuburb("Eastwood").get(0);
        Assert.assertEquals(2, clinicAddress.getClinic().getDoctors().size());
    }

    /* ---- Herb ---*/
    @Test
    public void canAddHerbClass(){
        // clinic
        Clinic clinic = ApplicationBuilder.buildClinic("Chinese Shop1", "English Shop1");
        shopService.saveClinic(clinic);
        Assert.assertTrue(clinic.getCID() != null);

        Clinic clinic1 = shopService.findClinicByChineseName("Chinese Shop1");

        // herb
        HerbClass herbClass  = ApplicationBuilder.buildHerbClass("Class1", "Category1");
        clinic1.addHerbClass(herbClass);
        shopService.updateClinic(clinic1);

        Assert.assertEquals("Class1", shopService.findHerbClassByName("Class1").getName());
    }
    @Test
    public void canAddHerbClassWithHerb(){
        // clinic
        Clinic clinic = ApplicationBuilder.buildClinic("Chinese Shop1", "English Shop1");
        shopService.saveClinic(clinic);
        Assert.assertTrue(clinic.getCID() != null);

        Clinic clinic1 = shopService.findClinicByChineseName("Chinese Shop1");

        // herb
        HerbClass herbClass  = ApplicationBuilder.buildHerbClass("Class1", "Category1");
        clinic1.addHerbClass(herbClass);
        shopService.updateClinic(clinic1);

        HerbClass herbClass1 = shopService.findHerbClassByName("Class1");
        Assert.assertEquals("Class1", herbClass1.getName());
        Assert.assertEquals("Chinese Shop1", herbClass1.getClinic().getChineseName());

        Herb jensen = ApplicationBuilder.buildHerb("Jensen1", "Jensen2", 10, 50.00f);
        Herb dudong = ApplicationBuilder.buildHerb("DuDong1", "DuDong2", 11, 50.00f);
        herbClass1.addHerb(jensen);
        shopService.updateClinic(clinic1);

        HerbClass herbClass2 = shopService.findHerbClassByName("Class1");
        Assert.assertEquals("Class1", herbClass2.getName());
        Assert.assertEquals(1, herbClass2.getHerbs().size());

        // add 2nd herb
        herbClass2.addHerb(dudong);
        shopService.updateClinic(herbClass2.getClinic());

        HerbClass herbClass3 = shopService.findHerbClassByName("Class1");
        Assert.assertEquals("Class1", herbClass3.getName());
        Assert.assertEquals(2, herbClass3.getHerbs().size());

        // search
        Herb dudong1 = shopService.findHerbByChineseName("DuDong1");
        Assert.assertEquals("DuDong2", dudong1.getEnglishName());
        Assert.assertEquals("Class1", dudong1.getHerbClass().getName());
    }

}
