package au.com.new1step.herbalist.service;

import au.com.new1step.herbalist.exception.EntityNotFoundException;
import au.com.new1step.herbalist.exception.EntityNotHaveIdException;
import au.com.new1step.herbalist.jpa.model.Doctor;
import au.com.new1step.herbalist.jpa.model.Patient;
import au.com.new1step.herbalist.jpa.model.Prescription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface DoctorService {
    public Doctor updateDoctor(Doctor doctor) throws EntityNotFoundException;  // add doctor from ShopService
    public Patient updatePatient(Patient patient) throws EntityNotHaveIdException;  // add patient from updateDoctor
    public Patient writePrescription(Patient patient, Prescription prescription) throws EntityNotHaveIdException;
    public Patient findPatientByID(Long pid);
    public Patient findPatientByPhone(String phone);
    public List<Patient> findAllPatients();
    public Page<Patient> findAllPatients(Pageable pageable);
    public Patient findPatientByFirstName(String firstName); // first name is unique
    public List<Patient> findPatientByFirstNameAndLastName(String firstName, String lastName);
    public Prescription findPatientLastPrescripton(Patient patient);
    public Prescription findPrescriptionById(Long presID);
    public void updatePrescription(Prescription prescription) throws EntityNotHaveIdException;
    public List<Prescription> findPatientLastPrescriptions(Patient patient, Integer number);

    public Doctor findLoginDoctor();
    public List<Doctor> findDoctorsByMobile(String mobile);
    public List<Doctor> findDoctorsByFirstName(String firstName);
    public List<Doctor> findDoctorsByFirstNameAndLastName(String firstName, String lastName);

    public List<Double> findDailyNoOfWeekPatients(LocalDate firstDateOfWeek);
    public List<Double> findDailyWeeklyFees(LocalDate firstDateOfWeek);
    public List<Double> findDailyNoOfPatientsForTheMonth(LocalDate start);
    public List<Double> findDailyFeesForTheMonth(LocalDate start);
    public List<Double> findMonthlyNoOfPatientsForTheYear(LocalDate start);
    public List<Double> findMonthlyFeesForTheYear(LocalDate start);

    public void purgePrescriptionsByYear(Integer getYearToPurge);
}
