package au.com.new1step.herbalist.common;

import au.com.new1step.herbalist.jpa.model.AppUser;
import au.com.new1step.herbalist.jpa.model.Clinic;
import au.com.new1step.herbalist.jpa.model.ClinicAddress;
import au.com.new1step.herbalist.jpa.model.Doctor;
import au.com.new1step.herbalist.jpa.model.Herb;
import au.com.new1step.herbalist.jpa.model.HerbClass;
import au.com.new1step.herbalist.jpa.model.Medicine;
import au.com.new1step.herbalist.jpa.model.Patient;
import au.com.new1step.herbalist.jpa.model.Prescription;
import au.com.new1step.herbalist.web.dto.AppUserDto;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.List;

public class ApplicationBuilder {
    // this method is used
    public static AppUser buildUser(AppUserDto appUserDto){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(appUserDto.getPassword());

        byte[] availableDays = appUserDto.getAvailableDays().toString().getBytes();
        LocalDate startDate = LocalDate.now();
        LocalDate expireDate = startDate.plusDays(appUserDto.getAvailableDays());

        AppUser user = new AppUser();
        user.setUsername(appUserDto.getUsername());
        user.setPassword(hashedPassword);
        user.setRole(appUserDto.getRole());
        user.setAvailableDays(availableDays);
        user.setStartDate(startDate);
        user.setExpiredDate(expireDate);
        user.setAccountNonExpired(appUserDto.isAccountNonExpired());
        user.setAccountNonLocked(appUserDto.isAccountNonLocked());
        user.setCredentialsNonExpired(appUserDto.isCredentialsNonExpired());
        user.setEnabled((appUserDto.isEnabled()));

        return user;
    }
    // this class should only for test, can remove later
    public static AppUser buildUser(String username, String password, String role){
        AppUser user = new AppUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(role);
        return user;
    }
    public static Clinic buildClinic(String chinseName, String englishName) {
        Clinic clinic = new Clinic();
        clinic.setChineseName(chinseName);
        clinic.setEnglishName(englishName);
        return clinic;
    }

    public static ClinicAddress buildClinicAddress(String address1, String address2, String suburb, String state, String postcode, String country){
        ClinicAddress address = new ClinicAddress();
        address.setAddress1(address1);
        address.setAddress2(address2);
        address.setSuburb(suburb);
        address.setState(state);
        address.setPostCode(postcode);
        address.setCountry(country);
        return address;
    }

    public static Doctor buildDoctor(String firstName, String lastName, String mobile, Float defaultFee){
        Doctor doctor = new Doctor();
        doctor.setFirstName(firstName);
        doctor.setLastName(lastName);
        doctor.setMobile(mobile);
        doctor.setDefaultFee(defaultFee);
        return doctor;
    }

    public static Herb buildHerb(String name1, String name2, Integer quantity, Float cost){
        Herb herb = new Herb();
        herb.setChineseName(name1);
        herb.setEnglishName(name2);
        herb.setQuantity(quantity);
        herb.setCost(cost);
        return herb;
    }

    public static Patient buildPatient(String firstName, String lastName, String sex, String phone, LocalDate dob) {
        Patient patient = new Patient();
        patient.setFirstName(firstName);
        patient.setLastName(lastName);
        patient.setSex(sex);
        patient.setPhone(phone);
        patient.setDob(dob);

        return patient;
    }

    public static HerbClass buildHerbClass(String name, String category){
        HerbClass herbClass = new HerbClass();
        herbClass.setCategory(category);
        herbClass.setName(name);
        return herbClass;
    }

    // write prescription
    public static Medicine buildMedicine(Herb herb, String chineseHerbName, Integer weight){
        if(weight == null){
            weight = 0;
        }
        Medicine medicine = new Medicine();
        medicine.setHerb(herb);
        medicine.setChineseName(chineseHerbName);
        medicine.setWeight(weight);  // gram
        if(herb != null) {
            Float price = 0.0f;
            if (herb.getUnitPrice() != null ){
                price = herb.getUnitPrice();
            }
            medicine.setCost(price * weight);    // dollar
            medicine.setEnglishName(herb.getEnglishName());
            medicine.setUnitPrice(herb.getUnitPrice());
        }

        return medicine;
    }

    public static Prescription buildPrescription(String symptom, Float fee, List<Medicine> medicines, Doctor doctor) {
        Prescription prescription = new Prescription();
        prescription.setSymptom(symptom);
        prescription.setFee(doctor.getDefaultFee()); // dollar
        prescription.addMedicines(medicines);
        prescription.setProcessingStatus(ProcessingStatus.UNPROCESSED);
        prescription.setDoctor(doctor);
        return prescription;
    }

    //----below need to check later------------
    public static SimpleMailMessage buildSimpleMeailMessage(String toEmail, String subject, String text, String fromEmail){
        SimpleMailMessage registrationEmail = new SimpleMailMessage();
        registrationEmail.setTo(toEmail);
        registrationEmail.setSubject(subject);
        registrationEmail.setText(text);
        registrationEmail.setFrom("noreply@herbalist.com.au");
        registrationEmail.setFrom(fromEmail);
        return registrationEmail;
    }


}
