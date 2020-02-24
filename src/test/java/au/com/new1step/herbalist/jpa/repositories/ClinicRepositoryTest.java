package au.com.new1step.herbalist.jpa.repositories;

import au.com.new1step.herbalist.common.ApplicationBuilder;
import au.com.new1step.herbalist.jpa.model.Clinic;
import au.com.new1step.herbalist.jpa.model.ClinicAddress;
import au.com.new1step.herbalist.jpa.model.Doctor;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@ActiveProfiles("local")
@SpringBootTest
@Transactional
public class ClinicRepositoryTest {
    private Logger log = LoggerFactory.getLogger(ClinicRepositoryTest.class);

    @Autowired
    ClinicRepository clinicRepository;

    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    ClinicAddressRepository clinicAddressRepository;

    @Test
    public void testAdd3Clinics(){
        Clinic clinic1 = ApplicationBuilder.buildClinic("Chinese Shop1", "English Shop1");
        Clinic clinic2 = ApplicationBuilder.buildClinic("Chinese Shop2", "English Shop2");
        Clinic clinic3 = ApplicationBuilder.buildClinic("Chinese Shop3", "English Shop3");

        clinicRepository.save(clinic1);
        clinicRepository.save(clinic2);
        clinicRepository.save(clinic3);

        List<Clinic> clinics = clinicRepository.findAll();
        Assert.assertEquals(3, clinics.size());
    }


    @Test
    public void testAdd2ClinicsWithDoctors(){
        Clinic eastwoodShop = ApplicationBuilder.buildClinic("Eastwood Chinese Herb", "Eastwood Chinese Herb");
        Clinic eppingShop = ApplicationBuilder.buildClinic("Epping Chinese Herb", "Epping Chinese Herb");

        Doctor doctor1 = ApplicationBuilder.buildDoctor("Andy", "Wu", "0402902617", 15.00f);
        Doctor doctor2 = ApplicationBuilder.buildDoctor("Andy", "Wu", "0402902617", 20.00f);
        Doctor doctor3 = ApplicationBuilder.buildDoctor("Ethan", "Tang", "0421991372", 15.00f);
        Doctor doctor4 = ApplicationBuilder.buildDoctor("William", "Wu", "0421991372", 15.00f);
        doctorRepository.save(doctor1);
        doctorRepository.save(doctor2);
        doctorRepository.save(doctor3);
        doctorRepository.save(doctor4);

        List<Doctor> list1 = new ArrayList<>();
        list1.add(doctor1);
        list1.add(doctor3);
        eastwoodShop.setDoctors(list1);

        List<Doctor> list2 = new ArrayList<>();
        list2.add(doctor4);
        list2.add(doctor3);
        list2.add(doctor2);
        eppingShop.setDoctors(list2);
        clinicRepository.save(eastwoodShop);
        clinicRepository.save(eppingShop);

        List<Clinic> clinics = clinicRepository.findAll();
        Assert.assertEquals(2, clinics.size());

        // find number of doctors in a clinic
        Clinic eppingShop2 = clinicRepository.findByChineseName("Epping Chinese Herb");
        Assert.assertEquals(3, eppingShop2.getDoctors().size());

        log.info("==Show Doctors for a Clinic==");
        eppingShop2.getDoctors().stream().forEach(System.out::println);

        log.info("==Show Doctors for a Clinic sort by desc==");
        List<Doctor> sortedDoctors2 = eppingShop2.getDoctors().stream().sorted((d1, d2) -> d2.compareTo(d1)).collect(Collectors.toList());
        sortedDoctors2.stream().forEach(System.out::println);

        log.info("==Show Doctors for a Clinic sort by asc==");
        List<Doctor> sortedDoctors = eppingShop2.getDoctors().stream().sorted().collect(Collectors.toList());
        sortedDoctors.stream().forEach(System.out::println);
        Assert.assertEquals("Andy", sortedDoctors.get(0).getFirstName());
        Assert.assertEquals("Ethan", sortedDoctors.get(1).getFirstName());
        Assert.assertEquals("William", sortedDoctors.get(2).getFirstName());
    }

    @Test
    public void testAdd2ClinicsWithAddresses(){
        Clinic eastwoodShop = ApplicationBuilder.buildClinic("Eastwood Chinese Herb", "Eastwood Chinese Herb");
        Clinic eppingShop = ApplicationBuilder.buildClinic("Epping Chinese Herb", "Epping Chinese Herb");

        ClinicAddress eastwoodAddr = ApplicationBuilder.buildClinicAddress("28 Matthew Ave", null, "Eastwood", "NSW", "2122", "Australia");
        ClinicAddress eppingAddr = ApplicationBuilder.buildClinicAddress("21 Audine Ave", null, "Epping", "NSW", "2121", "Australia");
        ClinicAddress gosfordAddr = ApplicationBuilder.buildClinicAddress("26 Showground Ave", null, "Gosford", "NSW", "2500", "Australia");
        clinicAddressRepository.save(eastwoodAddr);
        clinicAddressRepository.save(eppingAddr);
        clinicAddressRepository.save(gosfordAddr);

        List<ClinicAddress> list1 = new ArrayList<>();
        list1.add(eastwoodAddr);
        list1.add(gosfordAddr);
        eastwoodShop.setAddresses(list1);

        List<ClinicAddress> list2 = new ArrayList<>();
        list2.add(eppingAddr);
        eppingShop.setAddresses(list2);

        clinicRepository.save(eastwoodShop);
        clinicRepository.save(eppingShop);

        List<Clinic> clinics = clinicRepository.findAll();
        Assert.assertEquals(2, clinics.size());

        // find number of doctors in a clinic
        Clinic eastwoodShop2 = clinicRepository.findByChineseName("Eastwood Chinese Herb");
        Assert.assertEquals(2, eastwoodShop2.getAddresses().size());

        log.info("==Show Addresss for a Clinic==");
        eastwoodShop2.getAddresses().stream().forEach(System.out::println);

        log.info("==Show Addresss for a Clinic sort by desc==");
        List<ClinicAddress> sortedAddress2 = eastwoodShop2.getAddresses().stream().sorted((d1, d2) -> d2.compareTo(d1)).collect(Collectors.toList());
        sortedAddress2.stream().forEach(System.out::println);

        log.info("==Show Addresses for a Clinic sort by asc==");
        List<ClinicAddress> sortedAddresses = eastwoodShop2.getAddresses().stream().sorted().collect(Collectors.toList());
        sortedAddresses.stream().forEach(System.out::println);
        Assert.assertEquals("28 Matthew Ave", sortedAddresses.get(0).getAddress1());
        Assert.assertEquals("26 Showground Ave", sortedAddresses.get(1).getAddress1());

    }

    @Test
    public void testFindAClinic(){
        testAdd3Clinics();
        Clinic  clinic = clinicRepository.findByChineseName("Chinese Shop2");
        Assert.assertEquals("English Shop2", clinic.getEnglishName());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testUniqueKey()  {
        Clinic clinic1 = ApplicationBuilder.buildClinic("Chinese Shop1", "English Shop1");
        Clinic clinic2 = ApplicationBuilder.buildClinic("Chinese Shop1", "English Shop2");

        clinicRepository.save(clinic1);
        clinicRepository.save(clinic2);
    }

}
