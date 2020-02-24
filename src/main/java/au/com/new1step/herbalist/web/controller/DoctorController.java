package au.com.new1step.herbalist.web.controller;

import au.com.new1step.herbalist.common.ApplicationBuilder;
import au.com.new1step.herbalist.common.ProcessingStatus;
import au.com.new1step.herbalist.exception.EntityNotFoundException;
import au.com.new1step.herbalist.exception.EntityNotHaveIdException;
import au.com.new1step.herbalist.jpa.model.*;
import au.com.new1step.herbalist.jpa.repositories.GlobalParameterRepository;
import au.com.new1step.herbalist.jpa.repositories.UserRepository;
import au.com.new1step.herbalist.pdf.PdfGenarator;
import au.com.new1step.herbalist.service.AdminService;
import au.com.new1step.herbalist.service.DoctorService;
import au.com.new1step.herbalist.service.ShopService;
import au.com.new1step.herbalist.util.MessageUtil;
import au.com.new1step.herbalist.util.ThrowExceptionUtil;
import au.com.new1step.herbalist.web.dto.*;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Controller
//@PreAuthorize("hasRole('DOCTOR')")// --  not working
@RequestMapping("/doctor")
public class DoctorController {
    private static final Logger logger = LoggerFactory.getLogger(DoctorController.class);

    @Qualifier("messageSource")
    @Autowired
    private MessageSource messageSource;

    @Autowired
    private AdminService adminService;
    @Autowired
    private ShopService shopService;

    @Autowired
    PdfGenarator pdfGenarator;

    @Autowired
    DoctorService doctorService;

    @Autowired
    GlobalParameterRepository globalParameterRepository;

   // @Autowired
   // private ReloadableResourceBundleMessageSource messageSource;

    @GetMapping("/home")
    public String home(){
        return "/herbalist/doctor/home";
    }

    /* submenu 1: create doctor detail */

    @GetMapping("/createDoctorDetail")
    public String createDoctorDetail(@ModelAttribute ("feedbackMessage") String feedbckMessage,  Model model, ServletRequest request){
        Doctor doctor = doctorService.findLoginDoctor();
        List<ClinicAddress> addresses = shopService.findAllClinics().get(0).getAddresses();

        DoctorDto1 doctor1 = new DoctorDto1();
        DoctorDto2 doctor2 = new DoctorDto2();
        if(doctor == null){
            String male = messageSource.getMessage("patient.sex.man", null, LocaleContextHolder.getLocale());
            doctor1.setSex(male);
            doctor1.setDefaultFee(0.00f);
            doctor1.setShowPresHistorySize(10);
            doctor1.setHerbSize(24);
            doctor1.setShowPatientPageSize(10);
            doctor1.setCopyPrePrescription(false);
            doctor1.setValidateHerbName(false);
            doctor1.setPrintChineseNameOnly(true);
        }else{
            doctor1.setDrID(doctor.getDrID());
            doctor1.setFirstName(doctor.getFirstName());
            doctor1.setLastName(doctor.getLastName());
            doctor1.setMobile(doctor.getMobile());
            doctor1.setEmail(doctor.getEmail());
            doctor1.setSex(doctor.getSex());
            doctor1.setDefaultFee(doctor.getDefaultFee());
            doctor1.setShowPatientPageSize(doctor.getShowPatientPageSize());
            doctor1.setPrintChineseNameOnly(doctor.getPrintChineseNameOnly());
            doctor1.setCopyPrePrescription(doctor.getCopyPrePrescription());
            doctor1.setValidateHerbName(doctor.getValidateHerbName());
            doctor1.setHerbSize(doctor.getHerbSize());
            doctor1.setShowPresHistorySize(doctor.getShowPresHistorySize());
            doctor1.setAddrId(doctor.getClinicAddress().getAddrId());
            doctor2.setDrID(doctor.getDrID());
            doctor2.setMedicinePackages(doctor.getMedicinePackages());
            doctor2.setMedicineDays(doctor.getMedicineDays());
            doctor2.setWaterCups(doctor.getWaterCups());
            doctor2.setWaterRemain(doctor.getWaterRemain());
            doctor2.setDrinkTime(doctor.getDrinkTime());
            doctor2.setSecondWaterCups(doctor.getSecondWaterCups());
            doctor2.setSecondWaterRemain(doctor.getSecondWaterRemain());
            doctor2.setSignature(doctor.getSignature());
        }

        if(!Strings.isBlank(feedbckMessage)) {
           ;// model.addAttribute("feedbackMessage", feedbckMessage);
        }else{
            model.addAttribute("feedbackMessage", null);
        }

        model.addAttribute("doctorCommand", doctor1);
        model.addAttribute("doctorCommand2", doctor2);
        model.addAttribute("clinicAddresses", addresses);
        return "/herbalist/doctor/createDoctorDetail";
    }

    @PostMapping("/createDoctorDetail")
    public String createDoctorDetailPost(@Valid @ModelAttribute ("doctorCommand") DoctorDto1 doctor1, BindingResult result, RedirectAttributes attributes, Model model) throws EntityNotFoundException {
        // do not need to check herbSize range, it will check automatically
        if(doctor1.getHerbSize() >= 3 && doctor1.getHerbSize() <= 30) {
            int remainder = doctor1.getHerbSize() % 3;
            if (remainder != 0) {
                result.rejectValue("herbSize", "error.herb.size.cannot.divide.by.3", "the Herb Size can not be divided by 3.");
            }
        }
        if (result.hasErrors()) {
            List<ClinicAddress> addresses = shopService.findAllClinics().get(0).getAddresses();
            model.addAttribute("clinicAddresses", addresses);
            model.addAttribute("doctorCommand", doctor1);
            return "/herbalist/doctor/createDoctorDetail";
        }

        Clinic clinic = shopService.findAllClinics().get(0);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AppUser appUser = adminService.findByUsername(auth.getName());

        // copy data
        Doctor doctor = new Doctor();
        doctor.setDrID(doctor1.getDrID());
        doctor.setFirstName(doctor1.getFirstName());
        doctor.setLastName(doctor1.getLastName());
        doctor.setMobile(doctor1.getMobile());
        doctor.setEmail(doctor1.getEmail());
        doctor.setSex(doctor1.getSex());
        doctor.setShowPatientPageSize(doctor1.getShowPatientPageSize());
        doctor.setDefaultFee(doctor1.getDefaultFee());
        doctor.setPrintChineseNameOnly(doctor1.getPrintChineseNameOnly());
        doctor.setCopyPrePrescription(doctor1.getCopyPrePrescription());
        doctor.setValidateHerbName(doctor1.getValidateHerbName());
        doctor.setHerbSize(doctor1.getHerbSize());
        doctor.setShowPresHistorySize(doctor1.getShowPresHistorySize());
        ClinicAddress clinicAddress = shopService.findAddressByAddrId(doctor1.getAddrId());
        doctor.setClinicAddress(clinicAddress);

        doctor.setUser(appUser);
        String entityName = messageSource.getMessage("doctor.entity.name", null, LocaleContextHolder.getLocale());
        if(doctor.getDrID() == null){
            // add doctor
            if(clinic.getDoctors().size() < adminService.findGlobalParameterByDefaultSystem().fetchNoOfDoctor()) {
                // add default value
                doctor.setMedicinePackages(3);
                doctor.setMedicineDays(3);
                doctor.setWaterCups("4");
                doctor.setWaterRemain("3/4");
                doctor.setDrinkTime("1");
                doctor.setSecondWaterCups("3");
                doctor.setSecondWaterRemain("3/4");
                doctor.setSignature(doctor.getLastName() + doctor.getFirstName());

                clinic.addDoctor(doctor);
                shopService.saveClinic(clinic);
                new MessageUtil(messageSource).addFeedbackMessage(attributes, "feedbackMessage","feedback.message.entity.added",
                        entityName, (doctor.getLastName() + " " + doctor.getFirstName()));
            }else{
                // this should never happen as it already check in ShopController when creating user login id/password
                String message = messageSource.getMessage("reach.max.doctor", null, LocaleContextHolder.getLocale());
                // this message can also using injection by @Value("reach.max.doctor") private String message
                model.addAttribute("errorMessage", message);
                return "/herbalist/doctor/createDoctorDetail";
            }
        }else{
            // edit doctor
            Doctor currDoctor = doctorService.findLoginDoctor();
            if(currDoctor.getDrID() != doctor.getDrID()){
                String message = messageSource.getMessage("doctor.id.not.match", null, LocaleContextHolder.getLocale());
                model.addAttribute("errorMessage", message);
                return "/herbalist/doctor/createDoctorDetail";
            }
            doctor.setMedicinePackages(currDoctor.getMedicinePackages());
            doctor.setMedicineDays(currDoctor.getMedicineDays());
            doctor.setWaterCups(currDoctor.getWaterCups());
            doctor.setWaterRemain(currDoctor.getWaterRemain());
            doctor.setDrinkTime(currDoctor.getDrinkTime());
            doctor.setSecondWaterCups(currDoctor.getSecondWaterCups());
            doctor.setSecondWaterRemain(currDoctor.getSecondWaterRemain());
            doctor.setSignature(currDoctor.getSignature());
            doctorService.updateDoctor(doctor);

            new MessageUtil(messageSource).addFeedbackMessage(attributes,"feedbackMessage",
                    "feedback.message.entity.updated", entityName, (doctor.getLastName() + " " + doctor.getFirstName()));
        }

        /* do not use addAttribute because it displays message on url, using feedback message better
        String message = messageSource.getMessage(mode, null, LocaleContextHolder.getLocale());
        attributes.addAttribute("successMsg", message);
        */

        return "redirect:/doctor/createDoctorDetail";
    }

    @PostMapping("/createDoctorInstruction")
    public String createDoctorInstructionPost(@Valid @ModelAttribute ("doctorCommand2") DoctorDto2 doctor2, BindingResult result, RedirectAttributes attributes, Model model) throws EntityNotFoundException {
        Doctor currDoctor = doctorService.findLoginDoctor();
        if(currDoctor == null){
            String entityName = messageSource.getMessage("doctor.entity.name", null, LocaleContextHolder.getLocale());
            new MessageUtil(messageSource).addFeedbackMessage(attributes,"errorMessage","entity.not.created.message", entityName);
            attributes.addAttribute("anchor", "tab2");
            return "redirect:/doctor/createDoctorDetail";      // return to tab2
        }

        if (result.hasErrors()) {
            List<FieldError> errors = result.getFieldErrors();
            String errorMessage = "";
            for (FieldError error : errors ) {
                String key = error.getCode() + ".doctorCommand2." + error.getField();
                errorMessage = messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
                break;
            }

            attributes.addFlashAttribute("errorMessage", errorMessage);
            attributes.addAttribute("anchor", "tab2");
            return "redirect:/doctor/createDoctorDetail";      // return to tab2

        }
        currDoctor.setMedicinePackages(doctor2.getMedicinePackages());
        currDoctor.setMedicineDays(doctor2.getMedicineDays());
        currDoctor.setWaterCups(doctor2.getWaterCups());
        currDoctor.setWaterRemain(doctor2.getWaterRemain());
        currDoctor.setDrinkTime(doctor2.getDrinkTime());
        currDoctor.setSecondWaterCups(doctor2.getSecondWaterCups());
        currDoctor.setSecondWaterRemain(doctor2.getSecondWaterRemain());
        currDoctor.setSignature(doctor2.getSignature());

        doctorService.updateDoctor(currDoctor);

        String entityName = messageSource.getMessage("doctor.entity.name", null, LocaleContextHolder.getLocale());
        new MessageUtil(messageSource).addFeedbackMessage(attributes,"feedbackMessage2",
                "feedback.message.entity.updated", entityName, (currDoctor.getLastName() + " " + currDoctor.getFirstName()));
        attributes.addAttribute("anchor", "tab2");
        return "redirect:/doctor/createDoctorDetail";
    }

    /* submenu 2: create patient detail */
 /*
    @GetMapping("/createPatientDetail")
    public String createPatientDetail(Model model){
        Patient patient = new Patient();
        String male = messageSource.getMessage("patient.sex.man", null, LocaleContextHolder.getLocale());
        patient.setSex(male);
        model.addAttribute("searchCommand", new SearchParameterDto()); // tab1
        model.addAttribute("patientCommand", patient); // tab1
        model.addAttribute("allPatients", doctorService.findAllPatients()); //tab 2

        return "/herbalist/doctor/createPatientDetail";
    }*/


   @GetMapping("/createPatientDetail")
   public String createPatientDetail(@PageableDefault(size=10, page=999) Pageable pageable, @RequestParam(value="anchor", required = false) String anchor,
                                     RedirectAttributes attributes, Model model){
       Integer initPageNo = pageable.getPageNumber();
       Patient patient = new Patient();
       String male = messageSource.getMessage("patient.sex.man", null, LocaleContextHolder.getLocale());
       patient.setSex(male);
       model.addAttribute("searchCommand", new SearchParameterDto()); // tab1
       model.addAttribute("patientCommand", patient); // tab1

       // tab 2
       Doctor currDoctor = doctorService.findLoginDoctor();

       if(pageable.getPageNumber() == 999 && pageable.getPageSize() == 10){
           Integer patientPageSize = currDoctor.getShowPatientPageSize();
           if(patientPageSize == null || patientPageSize == 0){
               patientPageSize = 10;
           }
           pageable = PageRequest.of(0,patientPageSize);
       }
/*

       System.out.println("Andy-number "+  page.getNumber());            // orginal page No = 0
       System.out.println("Andy-totalPages "+  page.getTotalPages());   // how many pages
       System.out.println("Andy-totalElements "+ page.getTotalElements()); // all records
       System.out.println("Andy-size "+  page.getSize());  // page size
*/


       if(initPageNo == 999 || "tab2".equalsIgnoreCase(anchor) ){
           Page<Patient> page = doctorService.findAllPatients(pageable);
           model.addAttribute("page", page);
           return "/herbalist/doctor/createPatientDetail";
       }else{
           attributes.addAttribute("page", pageable.getPageNumber());
           attributes.addAttribute("size", pageable.getPageSize());
           attributes.addAttribute("anchor", "tab2");
           return "redirect:/doctor/createPatientDetail";
       }
   }

    @PostMapping("/searchPatientDetail")
    public String createPatientDetailPost(@Valid @ModelAttribute ("searchCommand") SearchParameterDto searchParameter,
                                          BindingResult result, RedirectAttributes attributes, Model model) {
        if (result.hasErrors()) {
            List<FieldError> errors = result.getFieldErrors();
            String errorMessage = "";
            for (FieldError error : errors) {
                String key = error.getCode() + ".searchCommand." + error.getField();
                errorMessage = messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
                break;
            }

            attributes.addFlashAttribute("errorMessage", errorMessage);

            return "redirect:/doctor/createPatientDetail";      // return to tab
        }

        String searchStr = searchParameter.getPatientSearchString().trim();

        Patient patient = null;
        try {
            patient = searchPatient(searchStr);
            patient.setDobString(patient.getDob().toString());
        }catch (EntityNotFoundException ex){
            attributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/doctor/createPatientDetail";
        }

        model.addAttribute("searchCommand", new SearchParameterDto()); // tab1
        model.addAttribute("patientCommand", patient); // tab1
       /* model.addAttribute("allPatients", doctorService.findAllPatients()); //tab 2*/
        Doctor currDoctor = doctorService.findLoginDoctor();
        Integer patientPageSize = currDoctor.getShowPatientPageSize();
        if(patientPageSize == null || patientPageSize == 0){
            patientPageSize = 10;
        }

        Page<Patient> page = doctorService.findAllPatients(PageRequest.of(0,patientPageSize));
        model.addAttribute("page", page);
        return "/herbalist/doctor/createPatientDetail";
    }

    @PostMapping("/updatePatientDetail")
    public String updatePatientDetailPost(@Valid @ModelAttribute ("patientCommand") Patient patient,
                                          BindingResult result, RedirectAttributes attributes, Model model) {
        //  BindingResult result, RedirectAttributes attributes, Model model) throws EntityNotHaveIdException1, EntityNotFoundException2 {
        try{
            patient.setDob(LocalDate.parse(patient.getDobString()));
        }catch(Exception ex){
            result.rejectValue("dobString", "error.dob.format", "dob format error.");
        }
        if (result.hasErrors()) {
            model.addAttribute("searchCommand", new SearchParameterDto()); // tab1
            model.addAttribute("allPatients", doctorService.findAllPatients());
            return "/herbalist/doctor/createPatientDetail";
        }

        Doctor currDoctor = doctorService.findLoginDoctor();
        if(currDoctor == null){
            String entityName = messageSource.getMessage("doctor.entity.name", null, LocaleContextHolder.getLocale());
            new MessageUtil(messageSource).addFeedbackMessage(attributes,"errorMessage","entity.not.created.message", entityName);
            return "redirect:/doctor/createDoctorDetail";      // return to tab2
        }

        patient.setFirstName(patient.getFirstName().trim());
        String entityName = messageSource.getMessage("patient.entity.name", null, LocaleContextHolder.getLocale());

        if (patient.getPID() == null) {
            currDoctor.addPatient(patient);
            try {
                doctorService.updateDoctor(currDoctor);
                new MessageUtil(messageSource).addFeedbackMessage(attributes, "feedbackMessage",
                        "feedback.message.entity.added", entityName, (patient.getLastName() + " " + patient.getFirstName()));
            }catch (Exception ex) {
                logger.debug("EXCEPTION: " + ex.getMessage());
                String errMessage = messageSource.getMessage("error.phone.firstname.exist.add", null, LocaleContextHolder.getLocale());
                attributes.addFlashAttribute("errorMessage", errMessage);
            }
        } else {
            try {
                Patient currentPatient = doctorService.findPatientByID(patient.getPID());
                currentPatient.setFirstName(patient.getFirstName());
                currentPatient.setLastName(patient.getLastName());
                currentPatient.setSex(patient.getSex());
                currentPatient.setPhone(patient.getPhone());
                currentPatient.setDob(patient.getDob());
                doctorService.updatePatient(currentPatient);
                new MessageUtil(messageSource).addFeedbackMessage(attributes, "feedbackMessage",
                        "feedback.message.entity.updated", entityName, (patient.getLastName() + " " + patient.getFirstName()));
            }catch (Exception ex) {
                logger.debug("EXCEPTION: " + ex.getMessage());
                String errMessage = messageSource.getMessage("error.phone.firstname.exist.edit", null, LocaleContextHolder.getLocale());
                attributes.addFlashAttribute("errorMessage", errMessage);
            }
        }

        return "redirect:/doctor/createPatientDetail";
    }

    /*----- Create Prescription -----*/

    @GetMapping("/createPrescription")
    public String createPrescription(@ModelAttribute ("prevMedicines") List<Medicine> prevMedicines, Model model, @RequestParam (value="pId", required = false) Long pId){
        model.addAttribute("searchCommand", new SearchParameterDto()); // tab1
        if(pId != null) {
            Patient patient = doctorService.findPatientByID(pId);
            Prescription prescription = doctorService.findPatientLastPrescripton(patient);
            Doctor currDoctor = doctorService.findLoginDoctor();
            PrescriptionDto prescriptionDto = new PrescriptionDto();
            List<HerbItemDto> herbItemDtos = prescriptionDto.getHerbItems();

            for (int i = 0; i < currDoctor.getHerbSize(); i++) {
                HerbItemDto herbItemDto = new HerbItemDto();
                if(i < prevMedicines.size()){
                    Medicine medicine = prevMedicines.get(i);
                    herbItemDto.setChineseName(medicine.getChineseName());
                    herbItemDto.setGram(medicine.getWeight());
                    herbItemDtos.add(herbItemDto);
                }else {
                    herbItemDto.setChineseName("");
                   // herbItemDto.setGram(0);
                    herbItemDtos.add(herbItemDto);
                }
            }

            prescriptionDto.setPresId(prescription.getPsID());
            prescriptionDto.setHerbSize(currDoctor.getHerbSize());
            Locale current = LocaleContextHolder.getLocale();
            prescriptionDto.setSelectHerbCaption(messageSource.getMessage("label.form.doctor.select.herb.name", null, current));

            model.addAttribute("currPrescription", prescriptionDto);
            model.addAttribute("currPatient", patient);
            model.addAttribute("currentPatient", patient);
            model.addAttribute("now", LocalDateTime.now());

            List<HerbClass> herbClasses = shopService.findAllHerbClasses();
            model.addAttribute("herbClasses", herbClasses);

            SymptomDto symptomDto = new SymptomDto();
            symptomDto.setPatientID(pId);
            symptomDto.setSymptom(prescription.getSymptom());
            model.addAttribute("symptomCommand", symptomDto); // tab1
        }else{
            model.addAttribute("symptomCommand", new SymptomDto()); // tab1
        }
        return "/herbalist/doctor/createPrescription";
    }

    @PostMapping("/createPrescriptionPost")
    public String createPrescriptionPost(HttpServletRequest request, @ModelAttribute ("currPrescription") PrescriptionDto prescriptionDto,
                    RedirectAttributes attributes, BindingResult result, Model model) throws EntityNotHaveIdException {

        Doctor currentDr = doctorService.findLoginDoctor();

        Clinic clinic = shopService.findAllClinics().get(0); // in current application only one clinic
        if(clinic.getValidateHerbName() == true || currentDr.getValidateHerbName() == true) {
            try {
                validateHerb(prescriptionDto, result);
            }catch (EntityNotFoundException ex){
                String editPrescrition = messageSource.getMessage("error.medicine.edit.current.patient", null, LocaleContextHolder.getLocale());
                attributes.addFlashAttribute("errorMessage", ex.getMessage() + " - " + editPrescrition);
                return "redirect:/doctor/editPrintPrescription";
            }
        }
        if(prescriptionDto.getMedicineDays() == null || prescriptionDto.getMedicineDays() == 0){
            prescriptionDto.setMedicineDays(currentDr.getMedicineDays());
        }
        if(prescriptionDto.getMedicinePackages() == null || prescriptionDto.getMedicinePackages() ==0){
            prescriptionDto.setMedicinePackages(currentDr.getMedicinePackages());
        }
        if(prescriptionDto.getPresId() != null){
            Prescription prescription = doctorService.findPrescriptionById(prescriptionDto.getPresId());
            updatePrescription(prescription, prescriptionDto, false);
        }else{
            ThrowExceptionUtil throwExceptionUtil = new ThrowExceptionUtil(messageSource);
            throwExceptionUtil.throwEntityNotHaveIdException("prescription.entity.name", "");
        }
        request.getSession().setAttribute("preId",  prescriptionDto.getPresId());
        attributes.addAttribute("anchor", "tab3");
        return "redirect:/doctor/createPrescription";
    }

    @PostMapping("/searchCurrentPatient")
    public String searchCurrentPatientPost(@Valid @ModelAttribute ("searchCommand") SearchParameterDto searchParameter,
                                           BindingResult result, RedirectAttributes attributes, Model model) {

        if (result.hasErrors()) {
            List<FieldError> errors = result.getFieldErrors();
            String errorMessage = "";
            for (FieldError error : errors) {
                String key = error.getCode() + ".searchCommand." + error.getField();
                errorMessage = messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
                break;
            }

            attributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/doctor/createPrescription";     // return to tab
        }

        String searchStr = searchParameter.getPatientSearchString().trim();
        Patient patient = null;
        try {
            patient = searchPatient(searchStr);
        }catch (EntityNotFoundException ex){
            attributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/doctor/createPrescription";
        }


        model.addAttribute("searchCommand", new SearchParameterDto()); // tab1
        model.addAttribute("currentPatient", patient); // tab1

        Doctor currDoctor = doctorService.findLoginDoctor();
        SymptomDto symptomDto = new SymptomDto();
        symptomDto.setPatientID(patient.getPID());

        if(currDoctor.getCopyPrePrescription()){
           Prescription prescription = doctorService.findPatientLastPrescripton(patient);
           if(prescription != null){
               symptomDto.setSymptom(prescription.getSymptom());
               model.addAttribute("copyPrescription", true);
           }
        }
        model.addAttribute("symptomCommand", symptomDto); // tab1
        return "/herbalist/doctor/createPrescription";
    }

    @PostMapping("/createSymptom")
    public String createSymptomPost(@ModelAttribute ("symptomCommand") SymptomDto symptomDto, RedirectAttributes attributes, Model model) throws EntityNotHaveIdException {
        if(symptomDto.getPatientID() == null){
            String errorMessage = messageSource.getMessage("error.search.patient.first", null, LocaleContextHolder.getLocale());
            attributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/doctor/createPrescription";     // return to tab1
        }
        Patient patient = doctorService.findPatientByID(symptomDto.getPatientID());
        if(patient == null){
            String errorMessage = messageSource.getMessage("error.current.patient.not.found", null, LocaleContextHolder.getLocale());
            attributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/doctor/createPrescription";     // return to tab1
        }

        // current doctor
        Doctor currDoctor = doctorService.findLoginDoctor();
        Prescription prescription = new Prescription();
        prescription.setSymptom(symptomDto.getSymptom());
        prescription.setFee(currDoctor.getDefaultFee());
        prescription.setProcessingStatus(ProcessingStatus.UNPROCESSED);
        prescription.setDoctor(currDoctor);
        prescription.setPatient(patient);

        // copy prev prescription
        Prescription prevPrex = doctorService.findPatientLastPrescripton(patient);
        List<Medicine> medicines = new ArrayList<>();
        if(prevPrex != null){
            List<Medicine> prevMedicines = prevPrex.getMedicines();
            for (int i = 0; i < currDoctor.getHerbSize(); i++) {
                if (currDoctor.getCopyPrePrescription()) {
                    if (i < prevMedicines.size()) {
                        Medicine medicine = prevMedicines.get(i);
                        medicines.add(medicine);
                    }
                }
            }
        }

        if(medicines.size() > 0){
            attributes.addFlashAttribute("prevMedicines", medicines);
        }
        doctorService.writePrescription(patient, prescription);

        String entityName = messageSource.getMessage("prescription.entity.name", null, LocaleContextHolder.getLocale());
        new MessageUtil(messageSource).addFeedbackMessage(attributes, "feedbackMessage",
                "feedback.message.entity.added", entityName, (patient.getLastName() + " " + patient.getFirstName()));

        if(currDoctor.getCopyPrePrescription()){
                attributes.addFlashAttribute("copyPrescription", true);
        }
        attributes.addAttribute("pId",  patient.getPID());
        attributes.addAttribute("anchor", "tab2");
        return "redirect:/doctor/createPrescription";
    }

    /* modify prescription */
    @GetMapping("/editPrintPrescription")
    public String editPrintPrescription(Model model, @RequestParam (value="psId", required = false) Long psId){

        model.addAttribute("searchCommand", new SearchParameterDto()); // tab1
        if(psId != null) {
            Prescription prescription = doctorService.findPrescriptionById(psId);
            Patient patient = prescription.getPatient();

            Doctor currDoctor = doctorService.findLoginDoctor();
            PrescriptionDto prescriptionDto = new PrescriptionDto();
            List<HerbItemDto> herbItemDtos = new ArrayList<>();
            for (int i = 0; i < currDoctor.getHerbSize(); i++) {
                HerbItemDto herbItemDto = new HerbItemDto();
                if(i<prescription.medicines.size()){
                    Medicine medicine = prescription.getMedicines().get(i);
                    herbItemDto.setChineseName(medicine.getChineseName());
                    herbItemDto.setGram(medicine.getWeight());
                }else{
                    herbItemDto.setChineseName("");
                   // herbItemDto.setGram(0);
                }

                herbItemDtos.add(herbItemDto);
            }
            prescriptionDto.setMedicineDays(prescription.getMedicineDays());
            prescriptionDto.setMedicinePackages(prescription.getMedicinePackages());
            prescriptionDto.setHerbItems(herbItemDtos);
            prescriptionDto.setPresId(prescription.getPsID());
            prescriptionDto.setHerbSize(currDoctor.getHerbSize());
            Locale current = LocaleContextHolder.getLocale();
            prescriptionDto.setSelectHerbCaption(messageSource.getMessage("label.form.doctor.select.herb.name", null, current));

            model.addAttribute("currPrescription", prescriptionDto);
            model.addAttribute("currPatient", patient);
            model.addAttribute("currentPatient", patient);
            model.addAttribute("now", prescription.getUpdateDate());

            List<HerbClass> herbClasses = shopService.findAllHerbClasses();
            model.addAttribute("herbClasses", herbClasses);

            SymptomDto symptomDto = new SymptomDto();
            symptomDto.setPatientID(patient.getPID());
            symptomDto.setPresID(psId);
            symptomDto.setSymptom(prescription.getSymptom());
            model.addAttribute("symptomCommand", symptomDto); // tab1
        }else{
            model.addAttribute("symptomCommand", new SymptomDto()); // tab1
        }
        return "/herbalist/doctor/editPrintPrescription";
     }

    @PostMapping("/searchCurrentPatientWithPrescription")
    public String searchCurrentPatientWithPrescriptionPost(@Valid @ModelAttribute ("searchCommand") SearchParameterDto searchParameter,
                                                           BindingResult result, RedirectAttributes attributes, Model model) {
        if (result.hasErrors()) {
            List<FieldError> errors = result.getFieldErrors();
            String errorMessage = "";
            for (FieldError error : errors) {
                String key = error.getCode() + ".searchCommand." + error.getField();
                errorMessage = messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
                break;
            }

            attributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/doctor/editPrintPrescription";     // return to tab
        }

        String searchStr = searchParameter.getPatientSearchString().trim();
        Patient patient = null;
        try {
            patient = searchPatient(searchStr);
        }catch (EntityNotFoundException ex){
            attributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/doctor/editPrintPrescription";
        }

        Prescription prescription = doctorService.findPatientLastPrescripton(patient);
        if(prescription == null){
            String errorMessage = messageSource.getMessage("error.patient.not.have.prescription", null, LocaleContextHolder.getLocale());
            attributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/doctor/editPrintPrescription";
        }
        attributes.addAttribute("psId",  prescription.getPsID());
        return "redirect:/doctor/editPrintPrescription";
    }

    @PostMapping("/editSymptom")
    public String editSymptomPost(@ModelAttribute ("symptomCommand") SymptomDto symptomDto, RedirectAttributes attributes, Model model) throws EntityNotHaveIdException {
        if(symptomDto.getPatientID() == null){
            String errorMessage = messageSource.getMessage("error.search.patient.first", null, LocaleContextHolder.getLocale());
            attributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/doctor/editPrintPrescription";     // return to tab1
        }
        Patient patient = doctorService.findPatientByID(symptomDto.getPatientID());
        if(patient == null){
            String errorMessage = messageSource.getMessage("error.current.patient.not.found", null, LocaleContextHolder.getLocale());
            attributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/doctor/editPrintPrescription";     // return to tab1
        }

        Prescription prescription = doctorService.findPatientLastPrescripton(patient);
        prescription.setSymptom(symptomDto.getSymptom());
        prescription.setProcessingStatus(ProcessingStatus.UNPROCESSED);
        doctorService.updatePrescription(prescription);

        attributes.addAttribute("psId",  prescription.getPsID());
        attributes.addAttribute("anchor", "tab2");
        return "redirect:/doctor/editPrintPrescription";
    }

    @PostMapping("/editPrescriptionPost")
    public String editPrescriptionPost(HttpServletRequest request, @ModelAttribute ("currPrescription") PrescriptionDto prescriptionDto,
                                         RedirectAttributes attributes, BindingResult result) throws EntityNotFoundException, EntityNotHaveIdException {

        Doctor currentDr = doctorService.findLoginDoctor();
        Clinic clinic = shopService.findAllClinics().get(0);
        if(clinic.getValidateHerbName() == true || currentDr.getValidateHerbName() == true) {
            try {
                validateHerb(prescriptionDto, result);
            }catch (EntityNotFoundException ex){
                String editPrescrition = messageSource.getMessage("error.medicine.edit.current.patient", null, LocaleContextHolder.getLocale());
                attributes.addFlashAttribute("errorMessage", ex.getMessage() + " - " + editPrescrition);
                return "redirect:/doctor/editPrintPrescription";
            }
        }
        if(prescriptionDto.getMedicineDays() == null || prescriptionDto.getMedicineDays() == 0){
            prescriptionDto.setMedicineDays(currentDr.getMedicineDays());
        }
        if(prescriptionDto.getMedicinePackages() == null || prescriptionDto.getMedicinePackages() ==0){
            prescriptionDto.setMedicinePackages(currentDr.getMedicinePackages());
        }
        if(prescriptionDto.getPresId() != null){
            Prescription prescription = doctorService.findPrescriptionById(prescriptionDto.getPresId());
            updatePrescription(prescription, prescriptionDto, true);
        }else{
            ThrowExceptionUtil throwExceptionUtil = new ThrowExceptionUtil(messageSource);
            throwExceptionUtil.throwEntityNotFoundException("prescription.entity.name", "");
        }
        request.getSession().setAttribute("preId",  prescriptionDto.getPresId()); // pdf file
        attributes.addAttribute("anchor", "tab3");
        return "redirect:/doctor/editPrintPrescription";
    }
    /*--- other --*/

    @GetMapping("/prescription")  // generate pdf prescription
    public void showPrescriptionPdf(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long preId = (Long) request.getSession().getAttribute("preId");
        if(preId == null){
            return; // should throw exception
        }
        pdfGenarator.generatePdf(request, response, "prescription", preId);
    }

    @GetMapping("/demoPrescription")  // generate demo pdf prescription
    public void showPrescriptionDemoPdf(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        pdfGenarator.generateDemoPdf(request, response, "prescription");
    }

    /*----- Show History Records ---- */
    @GetMapping("/searchHistoryRecord")
    public String searchHistoryRecord(Model model, @RequestParam (value="psId", required = false) Long psId){
        model.addAttribute("searchCommand", new SearchParameterDto()); // tab1
        if(psId != null) {
            Prescription prescription = doctorService.findPrescriptionById(psId);

            Doctor currDoctor = doctorService.findLoginDoctor();
            PrescriptionDto prescriptionDto = new PrescriptionDto();
            List<HerbItemDto> herbItemDtos = new ArrayList<>();
            for (int i = 0; i < currDoctor.getHerbSize(); i++) {
                HerbItemDto herbItemDto = new HerbItemDto();
                if(i<prescription.medicines.size()){
                    Medicine medicine = prescription.getMedicines().get(i);
                    herbItemDto.setChineseName(medicine.getChineseName());
                    herbItemDto.setGram(medicine.getWeight());
                }else{
                    herbItemDto.setChineseName("");
                    herbItemDto.setGram(0);
                }

                herbItemDtos.add(herbItemDto);
            }
            prescriptionDto.setMedicinePackages(prescription.getMedicinePackages());
            prescriptionDto.setMedicineDays((prescription.getMedicineDays()));
            prescriptionDto.setHerbItems(herbItemDtos);
            prescriptionDto.setPresId(prescription.getPsID());
            prescriptionDto.setHerbSize(currDoctor.getHerbSize());
            Locale current = LocaleContextHolder.getLocale();
            prescriptionDto.setSelectHerbCaption(messageSource.getMessage("label.form.doctor.select.herb.name", null, current));

            model.addAttribute("currPrescription", prescriptionDto);
            model.addAttribute("currPatient", prescription.getPatient());
            model.addAttribute("currentPatient", prescription.getPatient());
            model.addAttribute("now", prescription.getUpdateDate());

            List<Prescription> prescriptions = doctorService.findPatientLastPrescriptions(prescription.getPatient(), currDoctor.getShowPresHistorySize());
            model.addAttribute("prescriptions", prescriptions);

            SymptomDto symptomDto = new SymptomDto();
            symptomDto.setSymptom(prescription.getSymptom());
            model.addAttribute("symptomCommand", symptomDto); // tab1
        }else{
            model.addAttribute("symptomCommand", new SymptomDto()); // tab1
        }
        return "/herbalist/doctor/searchHistoryRecord";
    }

    @PostMapping("/searchHistoryPatient")
    public String searchHistoryPatientPost(@Valid @ModelAttribute ("searchCommand") SearchParameterDto searchParameter,
                                           BindingResult result, RedirectAttributes attributes, Model model) {
        if (result.hasErrors()) {
            List<FieldError> errors = result.getFieldErrors();
            String errorMessage = "";
            for (FieldError error : errors) {
                String key = error.getCode() + ".searchCommand." + error.getField();
                errorMessage = messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
                break;
            }

            attributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/doctor/searchHistoryRecord";     // return to tab
        }

        String searchStr = searchParameter.getPatientSearchString().trim();
        Patient patient = null;
        try {
            patient = searchPatient(searchStr);
        }catch (EntityNotFoundException ex){
            attributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/doctor/searchHistoryRecord";
        }

        Doctor currDoctor = doctorService.findLoginDoctor();
        model.addAttribute("currentPatient", patient);
        model.addAttribute("pId", patient.getPID());
        List<Prescription> prescriptions = doctorService.findPatientLastPrescriptions(patient, currDoctor.getShowPresHistorySize());
        model.addAttribute("prescriptions", prescriptions);
        model.addAttribute("symptomCommand", new SymptomDto());
        return "/herbalist/doctor/searchHistoryRecord";
      //  return "redirect:/doctor/searchHistoryRecord";
    }

    @PostMapping("/showHistoryPrescription")
    public String showHistoryPrescriptionPost(@ModelAttribute ("symptomCommand") SymptomDto symptomDto, RedirectAttributes attributes, Model model) {
        if(symptomDto.getPresID() == null){
            return "redirect:/errorMessage"; //Todo
        }
        attributes.addAttribute("psId",  symptomDto.getPresID());

        return "redirect:/doctor/searchHistoryRecord";
    }


    /*----display chart ---*/
    @GetMapping("/showPatientFeeCharts")
    public String showPatientFeeCharts(Model model){
        return "/herbalist/doctor/showPatientFeeCharts";
    }

    /*----Purge old prescriptions ----*/
    @GetMapping("purgeOldPrescriptions")
    public String purgeOldPrescriptions(Model model){
        YearDto yearDto = new YearDto();
        int year = Year.now().getValue();
        yearDto.setYearToPurge(year - 1);
        model.addAttribute("yearCommand", yearDto);
        return "/herbalist/doctor/purgeOldPrescriptions";
    }

    @PostMapping("purgeOldPrescriptions") // using on click X button
    public String purgeOldPrescriptionsPost(@ModelAttribute("yearCommand") YearDto yearDto, RedirectAttributes attributes, Model model){
        int year = Year.now().getValue();
        if(yearDto.getYearToPurge() >= year){
            String errorMessage = messageSource.getMessage("error.cannot.purge.current.year", null, LocaleContextHolder.getLocale());
            model.addAttribute("errorMessage", errorMessage);
            return "/herbalist/doctor/purgeOldPrescriptions";
        }

        doctorService.purgePrescriptionsByYear(yearDto.getYearToPurge());
        String entityName = messageSource.getMessage("prescription.entity.name", null, LocaleContextHolder.getLocale());
        new MessageUtil(messageSource).addFeedbackMessage(attributes, "feedbackMessage",
                "feedback.message.entity.deleted", yearDto.getYearToPurge(), entityName);

        return "redirect:/doctor/purgeOldPrescriptions";
    }

    @GetMapping("purgeOldPrescriptions/{year}")
    public String purgeOldPrescriptionsPost(@PathVariable("year") Integer yearToPurge, RedirectAttributes attributes, Model model){
        int year = Year.now().getValue();
        if(yearToPurge == 9999){
            String errorMessage = messageSource.getMessage("error.null.purge.current.year", null, LocaleContextHolder.getLocale());
            model.addAttribute("errorMessage", errorMessage);
            YearDto yearDto = new YearDto();
            model.addAttribute("yearCommand", yearDto);
            return "/herbalist/doctor/purgeOldPrescriptions";
        }
        if(yearToPurge >= year){
            String errorMessage = messageSource.getMessage("error.cannot.purge.current.year", null, LocaleContextHolder.getLocale());
            model.addAttribute("errorMessage", errorMessage);
            YearDto yearDto = new YearDto();
            yearDto.setYearToPurge(yearToPurge);
            model.addAttribute("yearCommand", yearDto);
            return "/herbalist/doctor/purgeOldPrescriptions";
        }

        doctorService.purgePrescriptionsByYear(yearToPurge);
        String entityName = messageSource.getMessage("prescription.entity.name", null, LocaleContextHolder.getLocale());
        new MessageUtil(messageSource).addFeedbackMessage(attributes, "feedbackMessage",
                "feedback.message.entity.deleted", yearToPurge.toString(), entityName);

        return "redirect:/doctor/purgeOldPrescriptions";
    }

    private void validateHerb(PrescriptionDto prescriptionDto, BindingResult result) throws EntityNotFoundException {
        for (HerbItemDto item : prescriptionDto.getHerbItems()){
            String herbName = item.getChineseName();
            if(!StringUtils.isEmpty(herbName)) {
               Herb herb = shopService.findHerbByChineseName(herbName);
               if(herb == null){
                   ThrowExceptionUtil throwExceptionUtil = new ThrowExceptionUtil(messageSource);
                   throwExceptionUtil.throwEntityNotFoundException("herb.entity.name", herbName);
               }
            }
        }
    }

    private void updatePrescription(Prescription prescription, PrescriptionDto prescriptionDto, boolean isEdit) throws EntityNotHaveIdException {
        List<Medicine> medicines = new ArrayList<>();
        Medicine medicine;
        Herb herb;
        for(HerbItemDto itemDto: prescriptionDto.getHerbItems()){
            if(StringUtils.isEmpty(itemDto.getChineseName())){
                continue;
            }
            herb = shopService.findHerbByChineseName(itemDto.getChineseName());
            medicine = ApplicationBuilder.buildMedicine(herb, itemDto.getChineseName(), itemDto.getGram());
            medicines.add(medicine);
        }

        if(isEdit) {
            prescription.getMedicines().clear();
        }
        prescription.addMedicines(medicines);

        prescription.setMedicinePackages(prescriptionDto.getMedicinePackages());
        prescription.setMedicineDays(prescriptionDto.getMedicineDays());
        doctorService.updatePrescription(prescription);
    }

    private Patient searchPatient(String searchStr) throws EntityNotFoundException {
        Patient patient = null;
        searchStr = StringUtils.trim(searchStr);
        if(StringUtils.isNumeric(searchStr)){
            patient = doctorService.findPatientByPhone(searchStr);
        }else {
            patient = doctorService.findPatientByFirstName(searchStr);
        }

        if(patient == null){

         /*   Locale current = LocaleContextHolder.getLocale();
            // keep for reference
           // String entity = messageResource.getMessage("patient.entity.name", null, Locale.CHINA);
           // String msg = messageResource.getMessage("entity.not.found.message", new Object[] { entity, searchStr }, Locale.CHINA);
            String entity = messageResource.getMessage("patient.entity.name", null, current);
            String msg = messageResource.getMessage("entity.not.found.message", new Object[] { entity, searchStr }, current);
*/
            ThrowExceptionUtil throwExceptionUtil = new ThrowExceptionUtil(messageSource);
            throwExceptionUtil.throwEntityNotFoundException("patient.entity.name", searchStr);

          //  throw new EntityNotFoundException(msg);
        }
        return patient;
    }

    @ModelAttribute("prevMedicines")
    public List<Medicine> setupMedicines(){
        List<Medicine> prevMedicines = new ArrayList<>();
        return prevMedicines;
    }

}
