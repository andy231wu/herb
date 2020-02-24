package au.com.new1step.herbalist.jpa.repositories;

import au.com.new1step.herbalist.common.ApplicationBuilder;
import au.com.new1step.herbalist.jpa.model.Clinic;
import au.com.new1step.herbalist.jpa.model.Doctor;
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
public class DoctorRepositoryTest {
    Logger log = LoggerFactory.getLogger(DoctorRepositoryTest.class);

    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    ClinicRepository clinicRepository;

    @Test
    public void testAdd3Doctors(){
        Add3CDoctors();
        List<Doctor> doctors = doctorRepository.findAll();
        Assert.assertEquals(3, doctors.size());
    }


    @Test
    public void testFindByFirstName(){
        Add3CDoctors();
        List<Doctor> doctors = doctorRepository.findByFirstName("Andy");
        Assert.assertEquals(2, doctors.size());
        Assert.assertEquals(Double.valueOf(20.00f), Double.valueOf(doctors.get(1).getDefaultFee()));
    }

    @Test
    public void testFindADoctorClinic(){
        Add3CDoctors();
        List<Doctor> doctors = doctorRepository.findByFirstName("Andy");
        Assert.assertEquals("Eastwood Chinese Herb", doctors.get(0).getClinic().getChineseName());
        Assert.assertEquals("Epping Chinese Herb", doctors.get(1).getClinic().getChineseName());
    }


    private void Add3CDoctors(){
        Doctor doctor1 = ApplicationBuilder.buildDoctor("Andy", "Wu", "0402902617", 15.00f);
        Doctor doctor2 = ApplicationBuilder.buildDoctor("Andy", "Wu", "0402902617", 20.00f);
        Doctor doctor3 = ApplicationBuilder.buildDoctor("Ethan", "Tang", "0421991372", 15.00f);

        Clinic eastwoodShop = ApplicationBuilder.buildClinic("Eastwood Chinese Herb", "Eastwood Chinese Herb");
        Clinic eppingShop = ApplicationBuilder.buildClinic("Epping Chinese Herb", "Epping Chinese Herb");
        clinicRepository.save(eastwoodShop);
        clinicRepository.save(eppingShop);

        doctor1.setClinic(eastwoodShop);
        doctor2.setClinic(eppingShop);
        doctor3.setClinic(eastwoodShop);

        doctorRepository.save(doctor1);
        doctorRepository.save(doctor2);
        doctorRepository.save(doctor3);
    }
}
