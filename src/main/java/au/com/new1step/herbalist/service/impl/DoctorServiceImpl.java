package au.com.new1step.herbalist.service.impl;

import au.com.new1step.herbalist.exception.EntityNotFoundException;
import au.com.new1step.herbalist.exception.EntityNotHaveIdException;
import au.com.new1step.herbalist.jpa.model.*;
import au.com.new1step.herbalist.jpa.repositories.*;
import au.com.new1step.herbalist.service.DoctorService;
import au.com.new1step.herbalist.util.ThrowExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class DoctorServiceImpl implements DoctorService {

	private Logger log = LoggerFactory.getLogger(DoctorServiceImpl.class);

	@Autowired
    private MessageSource messageSource;

	@Autowired
	private PatientRepository patientRepository;

	@Autowired
	private PrescriptionRepository prescriptionRepository;

	@Autowired
	private DoctorRepository doctorRepository;

	@Autowired
	private UserRepository userRepository;

	@PreAuthorize("hasRole('ROLE_ADMIN')")
    public Doctor updateDoctor(Doctor doctor) throws EntityNotFoundException {
    	if(doctor.getDrID() == null){
            ThrowExceptionUtil throwExceptionUtil = new ThrowExceptionUtil(messageSource);
            throwExceptionUtil.throwEntityNotFoundException("doctor.entity.name", doctor.getLastName() + " " + doctor.getFirstName());
    	}
		return doctorRepository.save(doctor);
    }

	public Patient updatePatient(Patient patient) throws EntityNotHaveIdException {
		if(patient.getPID() == null){
			ThrowExceptionUtil throwExceptionUtil = new ThrowExceptionUtil(messageSource);
			throwExceptionUtil.throwEntityNotHaveIdException("patient.entity.name", patient.getLastName() + " " + patient.getFirstName());
		}
		return patientRepository.save(patient);
	}

	public Patient writePrescription(Patient patient, Prescription prescription) throws EntityNotHaveIdException {
    	patient.addPrescription(prescription);
    	return updatePatient(patient);
	}

	public void updatePrescription(Prescription prescription) throws EntityNotHaveIdException {
		if(prescription.getPsID() == null){
			ThrowExceptionUtil throwExceptionUtil = new ThrowExceptionUtil(messageSource);
			throwExceptionUtil.throwEntityNotHaveIdException("prescription.entity.name", prescription.getPatient().getLastName() + " " + prescription.getPatient().getFirstName());
		}
		prescriptionRepository.save(prescription);
	}

	public List<Prescription> findPatientLastPrescriptions(Patient patient, Integer number){
    	return prescriptionRepository.findByPatientOrderByUpdateDateDesc(patient, PageRequest.of(0,number));
	}

    public Patient findPatientByID(Long pid){
        return patientRepository.findById(pid).get();
    }
	public Patient findPatientByPhone(String phone){
		return patientRepository.findByPhone(phone);
	}

	public Patient findPatientByFirstName(String firstName){
		return patientRepository.findByFirstName(firstName);
	}

	public List<Patient> findPatientByFirstNameAndLastName(String firstName, String lastName){
		return patientRepository.findByFirstNameAndLastName(firstName, lastName);
	}

	public List<Patient> findAllPatients(){
    	return patientRepository.findAllPatients();
	}
	public Page<Patient> findAllPatients(Pageable pageable){
		return patientRepository.findAll(pageable);
	}

	public Prescription findPatientLastPrescripton(Patient patient){
    	return  prescriptionRepository.findFirstByPatientOrderByUpdateDateDesc(patient);
	}
	public Prescription findPrescriptionById(Long presID){
    	return prescriptionRepository.findByPsID(presID);
	}

	public Doctor findLoginDoctor(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		AppUser appUser = userRepository.findByUsername(auth.getName());
    	return doctorRepository.findByUser(appUser);
	}
	public List<Doctor> findDoctorsByFirstName(String firstName){
		return doctorRepository.findByFirstName(firstName);
	}

	public List<Doctor> findDoctorsByFirstNameAndLastName(String firstName, String lastName){
		return doctorRepository.findByFirstNameAndLastName(firstName, lastName);
	}

	public List<Doctor> findDoctorsByMobile(String mobile){
		return doctorRepository.findByMobile(mobile);
	}

	public List<Double> findDailyNoOfWeekPatients(LocalDate firstDateOfWeek){
    	Doctor doctor = findLoginDoctor();
		LocalDateTime startDate = firstDateOfWeek.atStartOfDay();
		LocalDateTime endDate = firstDateOfWeek.atTime(23,59,59);

    	List<Double> servenDaysPatients = new ArrayList<>();
    	Integer noOfPatientsMon = prescriptionRepository.findDailyNoOfPatients(doctor, startDate, endDate);
		Integer noOfPatientsTue = prescriptionRepository.findDailyNoOfPatients(doctor, startDate.plusDays(1), endDate.plusDays(1));
		Integer noOfPatientsWed = prescriptionRepository.findDailyNoOfPatients(doctor, startDate.plusDays(2), endDate.plusDays(2));
		Integer noOfPatientsThu = prescriptionRepository.findDailyNoOfPatients(doctor, startDate.plusDays(3), endDate.plusDays(3));
		Integer noOfPatientsFri = prescriptionRepository.findDailyNoOfPatients(doctor, startDate.plusDays(4), endDate.plusDays(4));
		Integer noOfPatientsSat = prescriptionRepository.findDailyNoOfPatients(doctor, startDate.plusDays(5), endDate.plusDays(5));
		Integer noOfPatientsSun = prescriptionRepository.findDailyNoOfPatients(doctor, startDate.plusDays(6), endDate.plusDays(6));

		servenDaysPatients.add(new Double(noOfPatientsMon.byteValue()));
		servenDaysPatients.add(new Double(noOfPatientsTue.byteValue()));
		servenDaysPatients.add(new Double(noOfPatientsWed.byteValue()));
		servenDaysPatients.add(new Double(noOfPatientsThu.byteValue()));
		servenDaysPatients.add(new Double(noOfPatientsFri.byteValue()));
		servenDaysPatients.add(new Double(noOfPatientsSat.byteValue()));
		servenDaysPatients.add(new Double(noOfPatientsSun.byteValue()));

		return servenDaysPatients;
	}
	public List<Double> findDailyWeeklyFees(LocalDate firstDateOfWeek){
		Doctor doctor = findLoginDoctor();
		LocalDateTime startDate = firstDateOfWeek.atStartOfDay();
		LocalDateTime endDate = firstDateOfWeek.atTime(23,59,59);

		List<Double> servenDaysFees = new ArrayList<>();
		Double feesMon = prescriptionRepository.findDailyServiceFee(doctor, startDate, endDate);
		Double feesTue = prescriptionRepository.findDailyServiceFee(doctor, startDate.plusDays(1), endDate.plusDays(1));
		Double feesWed = prescriptionRepository.findDailyServiceFee(doctor, startDate.plusDays(2), endDate.plusDays(2));
		Double feesThu = prescriptionRepository.findDailyServiceFee(doctor, startDate.plusDays(3), endDate.plusDays(3));
		Double feesFri = prescriptionRepository.findDailyServiceFee(doctor, startDate.plusDays(4), endDate.plusDays(4));
		Double feesSat = prescriptionRepository.findDailyServiceFee(doctor, startDate.plusDays(5), endDate.plusDays(5));
		Double feesSun = prescriptionRepository.findDailyServiceFee(doctor, startDate.plusDays(6), endDate.plusDays(6));

		servenDaysFees.add(feesMon);
		servenDaysFees.add(feesTue);
		servenDaysFees.add(feesWed);
		servenDaysFees.add(feesThu);
		servenDaysFees.add(feesFri);
		servenDaysFees.add(feesSat);
		servenDaysFees.add(feesSun);

		return servenDaysFees;

	}

    public List<Double> findDailyNoOfPatientsForTheMonth(LocalDate dayOne) {
        Doctor doctor = findLoginDoctor();
        LocalDateTime startDate = dayOne.atStartOfDay();
        LocalDateTime endDate = dayOne.atTime(23,59,59);

        List<Double> aMonthPatients = new ArrayList<>();
        Integer noOfPatients = null;
        for(int i=0; i < dayOne.lengthOfMonth(); i++) {
            noOfPatients = prescriptionRepository.findDailyNoOfPatients(doctor, startDate.plusDays(i), endDate.plusDays(i));
            aMonthPatients.add(new Double(noOfPatients.byteValue()));
        }

        return aMonthPatients;
    }

    public List<Double> findDailyFeesForTheMonth(LocalDate dayOne){
        Doctor doctor = findLoginDoctor();
        LocalDateTime startDate = dayOne.atStartOfDay();
        LocalDateTime endDate = dayOne.atTime(23,59,59);

        List<Double> aMonthFees = new ArrayList<>();
        Double fee = null;
        for(int i=0; i < dayOne.lengthOfMonth(); i++) {
            fee = prescriptionRepository.findDailyServiceFee(doctor, startDate.plusDays(i), endDate.plusDays(i));
            aMonthFees.add(fee);
        }

        return aMonthFees;
    }

    public List<Double> findMonthlyNoOfPatientsForTheYear(LocalDate dayOneOfMonth){
        Doctor doctor = findLoginDoctor();
        List<Double> aYearPatients = new ArrayList<>();
        Integer noOfPatients = null;
        for(int i=0; i < 12; i++) {
            noOfPatients = prescriptionRepository.findMonthlyNoOfPatients(doctor, dayOneOfMonth.plusMonths(i).atStartOfDay(),
                           dayOneOfMonth.plusMonths(i).plusDays(dayOneOfMonth.lengthOfMonth()).atTime(23,59,59));
            aYearPatients.add(new Double(noOfPatients.byteValue()));
        }

        return aYearPatients;
    }

    public List<Double> findMonthlyFeesForTheYear(LocalDate dayOneOfMonth){
        Doctor doctor = findLoginDoctor();

        List<Double> aYearFees = new ArrayList<>();
        Double fee = null;
        for(int i=0; i < 12; i++) {
            fee = prescriptionRepository.findMonthlyServiceFee(doctor, dayOneOfMonth.plusMonths(i).atStartOfDay(),
                    dayOneOfMonth.plusMonths(i).plusDays(dayOneOfMonth.lengthOfMonth()).atTime(23,59,59));
            aYearFees.add(fee);
        }

        return aYearFees;
    }

	public void purgePrescriptionsByYear(Integer yearToPurge){
    	Doctor doctor = findLoginDoctor();
		LocalDateTime firstDayOfYear = LocalDateTime.of(yearToPurge, 1, 1,0, 0, 1);
		LocalDateTime lastDayOfYear = LocalDateTime.of(yearToPurge, 12, 31,23,59,59);
    	List<Prescription> prescriptions  = prescriptionRepository.findPrescriptionsByYear(doctor, firstDayOfYear, lastDayOfYear);

    	prescriptions.forEach(p-> {
            prescriptionRepository.delete(p);
		});

	}
}

