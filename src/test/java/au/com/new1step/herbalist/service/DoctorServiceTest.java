package au.com.new1step.herbalist.service;

import au.com.new1step.herbalist.common.ApplicationBuilder;
import au.com.new1step.herbalist.common.ProcessingStatus;
import au.com.new1step.herbalist.exception.EntityNotFoundException;
import au.com.new1step.herbalist.exception.EntityNotHaveIdException;
import au.com.new1step.herbalist.jpa.model.Clinic;
import au.com.new1step.herbalist.jpa.model.ClinicAddress;
import au.com.new1step.herbalist.jpa.model.Doctor;
import au.com.new1step.herbalist.jpa.model.Herb;
import au.com.new1step.herbalist.jpa.model.HerbClass;
import au.com.new1step.herbalist.jpa.model.Medicine;
import au.com.new1step.herbalist.jpa.model.Patient;
import au.com.new1step.herbalist.jpa.model.Prescription;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@ActiveProfiles("local")
@SpringBootTest
@Transactional
public class DoctorServiceTest {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private AdminService adminService;

    @Test
    public void canAddPatient() throws EntityNotFoundException {
        Doctor doctor = findDoctors("Andy").get(0);

        // case1: assign new patientTom
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate tomLocalDate = LocalDate.parse("12/10/1996", formatter);
       // Date tomDob = Date.from(tomLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Patient patientTom = ApplicationBuilder.buildPatient("Tom", "Wang", "Male", "0477777777",tomLocalDate);

        doctor.addPatient(patientTom);
        Doctor doctor1 = doctorService.updateDoctor(doctor);
        Assert.assertEquals(1, doctor1.getPatients().size());
    }

    @Test
    public void canUpdatePatient() throws EntityNotFoundException, EntityNotHaveIdException {
        Doctor doctor = findDoctors("Andy").get(0);

        // case1: assign new patientTom
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate tomLocalDate = LocalDate.parse("12/10/1996", formatter);
        Date tomDob = Date.from(tomLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Patient patientTom = ApplicationBuilder.buildPatient("Tom", "Wang", "Male", "0477777777", tomLocalDate);

        doctor.addPatient(patientTom);
        Doctor doctor1 = doctorService.updateDoctor(doctor);
        Assert.assertEquals(1, doctor1.getPatients().size());

        // update patient
        Patient patient = doctorService.findPatientByPhone("0477777777");
        patient.setFirstName("Tony");
        doctorService.updatePatient(patient);

        Doctor doctor2 = doctorService.findDoctorsByFirstName("Andy").get(0);
        Assert.assertEquals(1, doctor2.getPatients().size());
        Assert.assertEquals("Tony", doctor2.getPatients().get(0).getFirstName());
    }

    @Test
    public void canRemovePatient() throws EntityNotFoundException {
        Doctor doctor = findDoctors("Andy").get(0);

        // case1: assign new patientTom
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate tomLocalDate = LocalDate.parse("12/10/1996", formatter);
        Date tomDob = Date.from(tomLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Patient patientTom = ApplicationBuilder.buildPatient("Tom", "Wang", "Male", "0477777777", tomLocalDate);

        // case2: add another patient
        LocalDate andyLocalDate = LocalDate.parse("23/01/1963", formatter);
        Date andyDob = Date.from(andyLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Patient patientAndy = ApplicationBuilder.buildPatient("Andy", "Wu", "Male", "0477778888", andyLocalDate);

        doctor.addPatient(patientTom);
        doctor.addPatient(patientAndy);
        Doctor doctor1 = doctorService.updateDoctor(doctor);
        Assert.assertEquals(2, doctor1.getPatients().size());

        // uremove patient
        Patient patient = doctorService.findPatientByPhone("0477777777");
        doctor.removePatient(patient);
        doctorService.updateDoctor(doctor);

        Patient andyPatient = doctorService.findPatientByPhone("0477778888");

        // only remove link between patientId in doctor_patient table
        Doctor doctor2 = doctorService.findDoctorsByFirstName("Andy").get(0);
        Assert.assertEquals(1, doctor2.getPatients().size());
        Assert.assertEquals("Andy", doctor2.getPatients().get(0).getFirstName());
        Assert.assertFalse(doctor2.getPatients().contains(patient));
        Assert.assertTrue(doctor2.getPatients().contains(andyPatient));

        // this is why the removedPatient still existing, and it still can find its doctor
        Patient removedPatient = doctorService.findPatientByPhone("0477777777");
        Assert.assertNotNull(removedPatient);
        Assert.assertEquals(1,removedPatient.doctors.size());
        Assert.assertEquals("Andy", removedPatient.doctors.get(0).getFirstName());
    }

    @Test
    public void canWritePrescription() throws EntityNotHaveIdException, EntityNotFoundException {
        canAddTwoHerbsToClinic();
        Doctor doctor1 = ApplicationBuilder.buildDoctor("Andy", "Wu", "0402902617", 15.00f);
        Doctor doctor2 = ApplicationBuilder.buildDoctor("Ethan", "Tang", "0421991372", 15.00f);

        Clinic clinic2 = shopService.findClinicByChineseName("Chinese Shop1");
        clinic2.addDoctor(doctor1);                                           // add 1 doctor
        clinic2.addDoctor(doctor2);                                           // add 1 doctor
        shopService.updateClinic(clinic2);

        // find clinic
        Clinic theClinic = shopService.findClinicByChineseName("Chinese Shop1");
        Doctor theDoctor = theClinic.getDoctors().get(1);
        HerbClass herbClass = theDoctor.getClinic().getHerbClasses().get(0);
        Assert.assertEquals(2, herbClass.getHerbs().size());
        Herb herb1 = herbClass.getHerbs().get(0);
        Herb herb2 = herbClass.getHerbs().get(1);

        // case1: assign new patientTom
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate tomLocalDate = LocalDate.parse("12/10/1996", formatter);
        Date tomDob = Date.from(tomLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Patient patientTom = ApplicationBuilder.buildPatient("Tom", "Wang", "Male", "0477777777", tomLocalDate);

        theDoctor.addPatient(patientTom);
        doctorService.updateDoctor(theDoctor);
        Assert.assertEquals(1, theDoctor.getPatients().size());

        Patient thePatient = theDoctor.getPatients().stream().filter(p -> p.getPhone().equals("0477777777")).collect(Collectors.toList()).get(0);

        // write prescription
        Medicine medicine1 = ApplicationBuilder.buildMedicine(herb1, "",10);
        Medicine medicine2 = ApplicationBuilder.buildMedicine(herb2, "",20);

        List<Medicine> medicines = new ArrayList<>();
        medicines.add(medicine1);
        medicines.add(medicine2);

        Prescription prescription = ApplicationBuilder.buildPrescription("This patient get flu", theDoctor.getDefaultFee(), medicines, theDoctor);

        doctorService.writePrescription(thePatient, prescription);

        // check if prescription is created
        Clinic theClinic2 = shopService.findClinicByChineseName("Chinese Shop1");
        Doctor theDoctor2 = theClinic.getDoctors().get(1);
        Assert.assertEquals("This patient get flu", theDoctor2.getPatients().get(0).getPrescriptions().get(0).getSymptom());

        // start pickup prescription
        Prescription prescription1 = shopService.findFirstUnprocessedPrescription();
        // display the pdf format prescription
        prescription1.setProcessingStatus(ProcessingStatus.PROCESSED);
        shopService.updatePrescription(prescription1);

        Prescription prescription2 = shopService.findPrescriptionById(prescription1.getPsID());
        Assert.assertEquals(ProcessingStatus.PROCESSED, prescription2.getProcessingStatus());
    }

    public void canViewPatientRecord(){
       //given

        // when

        // then
    }

    public void canDeletePatient(){
        //given

        // when

        // then
    }

    private List<Doctor> findDoctors(String firstname){
        Clinic clinic = ApplicationBuilder.buildClinic("Chinese Shop1", "English Shop1");
        ClinicAddress eastwoodAddr = ApplicationBuilder.buildClinicAddress("28 Matthew Ave", null, "Eastwood", "NSW", "2122", "Australia");
        clinic.addAddress(eastwoodAddr);
        shopService.saveClinic(clinic);

        Doctor doctor1 = ApplicationBuilder.buildDoctor("Andy", "Wu", "0402902617", 15.00f);
        doctor1.setClinicAddress(eastwoodAddr);
        Doctor doctor2 = ApplicationBuilder.buildDoctor("Ethan", "Tang", "0421991372", 15.00f);
        doctor2.setClinicAddress(eastwoodAddr);

        Clinic clinic2 = shopService.findClinicByChineseName("Chinese Shop1");
        clinic2.addDoctor(doctor1);                                           // add 1 doctor
        shopService.updateClinic(clinic2);

        Clinic clinic4 = shopService.findClinicByChineseName("Chinese Shop1");
        clinic4.addDoctor(doctor2);
        shopService.updateClinic(clinic4); // add 2nd doctor
 /*
        Assert.assertEquals(2, clinic4.getDoctors().size());
        Assert.assertNotNull(clinic4.getDoctors().get(0).getDrID());
        Assert.assertNotNull(clinic4.getDoctors().get(1).getDrID());
        Assert.assertEquals("28 Matthew Ave", clinic4.getDoctors().get(0).getClinicAddress().getAddress1());
        Assert.assertEquals("28 Matthew Ave", clinic4.getDoctors().get(1).getClinicAddress().getAddress1());*/

        return doctorService.findDoctorsByFirstName(firstname);
    }

    private void canAddTwoHerbsToClinic(){
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
/*
        HerbClass herbClass3 = shopService.findHerbClassByName("Class1");
        Assert.assertEquals("Class1", herbClass3.getName());
        Assert.assertEquals(2, herbClass3.getHerbs().size());

        // search
        Herb dudong1 = shopService.findHerbByChineseName("DuDong1");
        Assert.assertEquals("DuDong2", dudong1.getEnglishName());
        Assert.assertEquals("Class1", dudong1.getHerbClass().getName());
        */
    }

    @Test
    public void testPurgePrescription(){
        int year = 2017;
        doctorService.purgePrescriptionsByYear(year);
    }
}
