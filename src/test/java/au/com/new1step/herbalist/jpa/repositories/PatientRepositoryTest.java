package au.com.new1step.herbalist.jpa.repositories;

import au.com.new1step.herbalist.common.ApplicationBuilder;
import au.com.new1step.herbalist.common.ProcessingStatus;
import au.com.new1step.herbalist.jpa.model.*;
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
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Fail.fail;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ActiveProfiles("local")
@SpringBootTest
@Transactional
public class PatientRepositoryTest {
    Logger log = LoggerFactory.getLogger(PatientRepositoryTest.class);

    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    HerbRepository herbRepository;

    @Autowired
    PrescriptionRepository   prescriptionRepository;

    // doctor has ownership, so add should from doctor site

    @Test
    public void testAssignTWoPatientsToOneDoctor(){
        add2Doctors();
        List<Doctor> doctors = doctorRepository.findByFirstNameAndLastName("Katherine", "Wu");
        assertEquals(1, doctors.size());
        Doctor currentDr = null;
        if(!CollectionUtils.isEmpty(doctors)){
            currentDr = doctors.get(0);
        }else{
            fail("Some wrong - cannot find the dr Katherine Wu");
        }
        // case1: assign new patientTom
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate tomLocalDate = LocalDate.parse("12/10/1996", formatter);
        Date tomDob = Date.from(tomLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Patient patientTom = ApplicationBuilder.buildPatient("Tom", "Wang", "Male", "0477777777", tomLocalDate);

        currentDr.addPatient(patientTom);
        doctorRepository.save(currentDr);  // doctor has ownership

        List<Patient> drPatients = doctorRepository.findByFirstNameAndLastName("Katherine", "Wu").get(0).getPatients();
        assertEquals(1, drPatients.size());
        assertEquals("Tom", drPatients.get(0).getFirstName());

        // case2: add another patient
        LocalDate andyLocalDate = LocalDate.parse("23/01/1963", formatter);
        Date andyDob = Date.from(andyLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Patient patientAndy = ApplicationBuilder.buildPatient("Andy", "Wu", "Male", "0402902617", andyLocalDate);

        currentDr.addPatient(patientAndy);
        doctorRepository.save(currentDr);  // doctor has ownership

        drPatients = doctorRepository.findByFirstNameAndLastName("Katherine", "Wu").get(0).getPatients();
        assertEquals(2, drPatients.size());
        assertEquals("Tom", drPatients.get(0).getFirstName());
        assertEquals("Andy", drPatients.get(1).getFirstName());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testAssignTWoPatientsToOneDoctorWithDuplicatePhoneNo(){
        add2Doctors();
        List<Doctor> doctors = doctorRepository.findByFirstNameAndLastName("Katherine", "Wu");
        assertEquals(1, doctors.size());
        Doctor currentDr = null;
        if(!CollectionUtils.isEmpty(doctors)){
            currentDr = doctors.get(0);
        }else{
            fail("Some wrong - cannot find the dr Katherine Wu");
        }
        // case1: assign new patientTom
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate tomLocalDate = LocalDate.parse("12/10/1996", formatter);
        Date tomDob = Date.from(tomLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Patient patientTom = ApplicationBuilder.buildPatient("Tom", "Wang", "Male", "0477777777", tomLocalDate);

        currentDr.addPatient(patientTom);
        doctorRepository.save(currentDr);  // doctor has ownership

        List<Patient> drPatients = doctorRepository.findByFirstNameAndLastName("Katherine", "Wu").get(0).getPatients();
        assertEquals(1, drPatients.size());
        assertEquals("Tom", drPatients.get(0).getFirstName());

        // case2: add another patient
        LocalDate andyLocalDate = LocalDate.parse("23/01/1963", formatter);
        Date andyDob = Date.from(andyLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Patient patientAndy = ApplicationBuilder.buildPatient("Andy", "Wu", "Male", "0477777777", andyLocalDate);

        currentDr.addPatient(patientAndy);
        doctorRepository.save(currentDr);  // doctor has ownership
    }

    @Test
    public void testAssignTWoPatientsAndFindAPatient(){
        add2Doctors();
        List<Doctor> doctors = doctorRepository.findByFirstNameAndLastName("Katherine", "Wu");
        Doctor currentDr = doctors.get(0);
        // case1: assign new patientTom
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate tomLocalDate = LocalDate.parse("12/10/1996", formatter);
        Date tomDob = Date.from(tomLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Patient patientTom = ApplicationBuilder.buildPatient("Tom", "Wang", "Male", "0477777777", tomLocalDate);
        // case2: add another patient
        LocalDate andyLocalDate = LocalDate.parse("23/01/1963", formatter);
        Date andyDob = Date.from(andyLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Patient patientAndy = ApplicationBuilder.buildPatient("Andy", "Wu", "Male", "0477777778", andyLocalDate);

        currentDr.addPatient(patientTom);
        currentDr.addPatient(patientAndy);

        doctorRepository.save(currentDr);
        Patient andyPatient = patientRepository.findByPhone("0477777778");
        assertEquals("Andy", andyPatient.getFirstName());
        assertEquals("Katherine", andyPatient.doctors.get(0).getFirstName());
    }


    @Test
    public void testDoctorWritePrescriptionForPatine(){
        add2Doctors();
        add3Herbs();
        List<Doctor> doctors = doctorRepository.findByFirstNameAndLastName("Katherine", "Wu");
        Doctor currentDr = currentDr = doctors.get(0);

        // case1: assign new patientTom
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate tomLocalDate = LocalDate.parse("12/10/1996", formatter);
        Date tomDob = Date.from(tomLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Patient patientTom = ApplicationBuilder.buildPatient("Tom", "Wang", "Male", "0477777777", tomLocalDate);

        currentDr.addPatient(patientTom);
        doctorRepository.save(currentDr);  // doctor has ownership

        List<Patient> drPatients = doctorRepository.findByFirstNameAndLastName("Katherine", "Wu").get(0).getPatients();
        Patient tomPatient = drPatients.stream().filter(x->x.getFirstName().equals("Tom")).collect(Collectors.toList()).get(0);
        assertEquals("Tom", tomPatient.getFirstName());

        //---------------------------
        Herb her1 = herbRepository.findByChineseName("Jensen1");
        Herb her2 = herbRepository.findByChineseName("DuDong1");
        Herb her3 = herbRepository.findByChineseName("ShanZhai1");
        Herb her4 = herbRepository.findByChineseName("Release fever");
        // write prescription
        Medicine medicine1 = ApplicationBuilder.buildMedicine(her1, her1.getChineseName(),10);
        Medicine medicine2 = ApplicationBuilder.buildMedicine(her2, her2.getChineseName(),20);
        Medicine medicine3 = ApplicationBuilder.buildMedicine(her3, her3.getChineseName(), 30);
        Medicine medicine4 = ApplicationBuilder.buildMedicine(her3, her4.getChineseName(),10);

        List<Medicine> medicines = new ArrayList<>();
        medicines.add(medicine1);
        medicines.add(medicine2);
        medicines.add(medicine3);
        medicines.add(medicine4);

        Prescription prescription = ApplicationBuilder.buildPrescription("This patient get flu", currentDr.getDefaultFee(), medicines, currentDr);
        prescription.setDoctor(currentDr);

        tomPatient.addPrescription(prescription);
        patientRepository.save(tomPatient);

        // 2nd prescription
        List<Medicine> medicines2 = new ArrayList<>();
        medicines2.add(medicine1);
        medicines2.add(medicine3);
        Prescription prescription2 = ApplicationBuilder.buildPrescription("This patient has High fever", currentDr.getDefaultFee(), medicines2, currentDr);
        prescription2.setDoctor(currentDr);

        tomPatient.addPrescription(prescription2);
        patientRepository.save(tomPatient);

        // print unprocessed prescriptions
        List<Prescription> prescriptions = prescriptionRepository.findByProcessingStatus(ProcessingStatus.UNPROCESSED);
        assertEquals(2, prescriptions.size());
        prescriptions.stream().forEach(p -> showPrescription(p));

        System.out.println("========TEST========");
        Prescription newPr = prescriptionRepository.findFirstByProcessingStatusOrderByUpdateDateAsc(ProcessingStatus.UNPROCESSED);
        showPrescription(newPr);

    }

    private void showPrescription(Prescription unprocessedPre){
        System.out.println("==Patinent Info====");
        Patient currPatient = unprocessedPre.getPatient();
        System.out.println("Patient Name: " + currPatient.getFirstName() +
                " Doctor: " + unprocessedPre.getDoctor().getFirstName() +
                " Service Fee: " + unprocessedPre.getFee());
        System.out.println("==Medicine Info====");
        unprocessedPre.getMedicines().stream()
                .forEach(x->System.out.println("Medicine Chinese: " + x.getHerb().getChineseName()
                        + " Eng: " + x.getHerb().getEnglishName()
                        + " weig: " + x.getWeight()));
    }

    private void add3Herbs(){
        Herb jensen = ApplicationBuilder.buildHerb("Jensen1", "Jensen2", 10, 50.00f);
        Herb dudong = ApplicationBuilder.buildHerb("DuDong1", "DuDong2", 11, 50.00f);
        Herb shanzhai = ApplicationBuilder.buildHerb("ShanZhai1", "ShanZhai2", 12, 50.00f);
        Herb fever = ApplicationBuilder.buildHerb("Release fever", "Release fever2", 13, 50.00f);

        herbRepository.save(jensen);
        herbRepository.save(dudong);
        herbRepository.save(shanzhai);
        herbRepository.save(fever);

    }

    private void add2Doctors(){
        Doctor doctor1 = ApplicationBuilder.buildDoctor("William", "Wu", "0433333333", 15.00f);
        Doctor doctor2 = ApplicationBuilder.buildDoctor("Katherine", "Wu", "0444444444", 20.00f);

        doctorRepository.save(doctor1);
        doctorRepository.save(doctor2);
    }

    private void add3Patients(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        LocalDate andyLocalDate = LocalDate.parse("23/01/1963", formatter);
        Date andyDob = Date.from(andyLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Patient patientAndy = ApplicationBuilder.buildPatient("Andy", "Wu", "Male", "0402902617", andyLocalDate);

        LocalDate ethanLocalDate = LocalDate.parse("11/08/2014", formatter);
        Date ethanDob = Date.from(andyLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Patient patientEthan = ApplicationBuilder.buildPatient("Ethan", "Tang", "Male", "0488888888", ethanLocalDate);

        LocalDate leeLocalDate = LocalDate.parse("02/06/1975", formatter);
        Date leeDob = Date.from(andyLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Patient patientLee = ApplicationBuilder.buildPatient("Lee", "Chu", "Male", "0499999999", leeLocalDate);

        patientRepository.save(patientAndy);
        patientRepository.save(patientEthan);
        patientRepository.save(patientLee);

    }
}
