package au.com.new1step.herbalist.web.controller;

import au.com.new1step.herbalist.common.ApplicationBuilder;
import au.com.new1step.herbalist.exception.EntityNotFoundException;
import au.com.new1step.herbalist.jpa.model.*;
import au.com.new1step.herbalist.service.AdminService;
import au.com.new1step.herbalist.service.ShopService;
import au.com.new1step.herbalist.service.impl.ShopServiceImpl;
import au.com.new1step.herbalist.web.dto.AppUserDto;
import au.com.new1step.herbalist.web.dto.HerbDto;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/shop")
//@PreAuthorize("hasRole('OWNER')")// -- not working
@Slf4j
public class ShopController {

    @Qualifier("messageSource")
    @Autowired
    private MessageSource messageSource;


    @Autowired
    private AdminService adminService;
    @Autowired
    private ShopService shopService;

   @GetMapping("/home")
    public String home(){
        return "/herbalist/shop/home";
    }

    @GetMapping("/createShopDetail")
    public String createShopDetail(Model model){
        model.addAttribute("clinics",shopService.findAllClinics());
        return "/herbalist/shop/createShopDetail";
    }

    @PostMapping("/createShopDetail")
    public String createShopDetailPost(HttpServletRequest request, @ModelAttribute ("shopCommand") Clinic clinic, RedirectAttributes attributes){
        if(clinic.getCID() == null){
            shopService.saveClinic(clinic);
        }else{
            shopService.updateClinic(clinic);
        }

        if(clinic == null){
            attributes.addAttribute("errMessage", "You have reached maximum of clinic");
            return "redirect:/errorMessage";
        }

        //Todo: remove session attribute to redirectAttributes
        request.getSession().setAttribute("shop", clinic);
        return "redirect:/shop/createShopDetail";
    }

    @PostMapping("/createShopAddress")
    public String createShopDetailPost(@ModelAttribute ("addressCommand") ClinicAddress address, Model model){
        Clinic clinic = shopService.findClinicByChineseName(address.getClinic().getChineseName());
        clinic.addAddress(address);
        shopService.updateClinic(clinic);

        if(address == null){
            model.addAttribute("message", "add address failure");
            return "redirect:/errorMessage";
        }

        return "redirect:/shop/createShopDetail";
    }

    @GetMapping("/createDoctorUser")
    public String createDoctorUser(ModelMap model){
        List<AppUser> appUsers = adminService.findDoctorUsers();
        model.put("appUsers", appUsers); // for display tab 3
        return "/herbalist/shop/createDoctorUser";
    }

    @PostMapping("/createDoctorUser")
    public String createDoctorUserPost(@ModelAttribute ("doctorUserCommand") AppUserDto appUserDto, ModelMap model, RedirectAttributes attributes) throws EntityNotFoundException {
       // validate doctors number if reach max
        GlobalParameter globalParameter = adminService.findGlobalParameterByDefaultSystem();
        int doctorNo = Integer.parseInt(new String(globalParameter.getNoOfDoctor()));
        System.out.println("ANDY-DOCTOR-no: " + doctorNo + " Doctors: " + adminService.findDoctorUsers().size());
        if(adminService.findDoctorUsers().size() >= doctorNo){
           // attributes.addFlashAttribute("message", "You have reached Maximum of doctor number");  //Todo not working yet
            String message = messageSource.getMessage("reach.max.doctor", null, LocaleContextHolder.getLocale());
            model.put("errMessage", message);
            return "/herbalist/shop/createDoctorUser";
        }

        appUserDto.setAvailableDays(globalParameter.fetchAvailableDays());
        appUserDto.setAccountNonExpired(true);
        appUserDto.setAccountNonLocked(true);
        appUserDto.setCredentialsNonExpired(true);
        appUserDto.setEnabled(true);

        AppUser user = ApplicationBuilder.buildUser(appUserDto);
        adminService.insertUser(user);

        attributes.addAttribute("anchor", "tab3");
        return "redirect:/shop/createDoctorUser";
    }

    @GetMapping("/createHerb")
    public String createHerb(Model model){
        model.addAttribute("clinics",shopService.findAllClinics()); // for tab 1
        model.addAttribute("clinic", shopService.findAllClinics().get(0));
        model.addAttribute("herbs", shopService.findAllHerbsSortByHerbClass());
        return "/herbalist/shop/createHerb";
    }

    @PostMapping("/createHerbGroup")
    public String createHerbGroupPost(@ModelAttribute ("herbGroupCommand") HerbClass herbClass){
        Clinic clinic = shopService.findClinicByChineseName(herbClass.getClinic().getChineseName());
        clinic.addHerbClass(herbClass);
        shopService.updateClinic(clinic);

/*  todo: if add wrong, need to go to error page
        if(herbClass.getCID() == null){
            model.addAttribute("message", "add address failure");
            return "redirect:/errorMessage";
        }
*/
      // attributes.addAttribute("anchor", "tab2");
       return "redirect:/shop/createHerb";
    }

    @PostMapping("/createHerb")
    public String createHerbPost(@ModelAttribute ("herbCommand") Herb herb, Model model){
        HerbClass herbClass = shopService.findHerbClassByName(herb.getHerbClass().getName());
        Date today = new Date();
        // template dates
        herb.setStartDate(today);
        herb.setExpireDate(today);

        herbClass.addHerb(herb);
        shopService.updateHerbClass(herbClass);

/*  todo: if add wrong, need to go to error page
        if(herbClass.getCID() == null){
            model.addAttribute("message", "add address failure");
            return "redirect:/errorMessage";
        }
*/
        return "redirect:/shop/createHerb";
    }


    @GetMapping("/pickupPrescription")
    public String pickupPrescription(){
        return "/herbalist/shop/pickupPrescription";
    }

    @GetMapping("/showPatientIncomeCharts")
    public String showPatientIncomeCharts(){
        return "/herbalist/shop/showPatientIncomeCharts";
    }

    //----------------------------------

    @ModelAttribute("shopCommand")
    public Clinic setupShop(){
        Clinic clinic;
        List<Clinic> shops = shopService.findAllClinics();
        if(shops.size() == 0){
            clinic = new Clinic();
        }else{
            clinic = shops.get(0);
        }
        return  clinic;
    }

    @ModelAttribute("addressCommand")
    public ClinicAddress setupAddress(){
        ClinicAddress address = new ClinicAddress();
        address.setCountry("AUSTRALIA");
        return address;
    }

    @ModelAttribute("doctorUserCommand")
    public AppUserDto setupShopDoctor(){
        AppUserDto user = new AppUserDto();
        user.setRole("ROLE_DOCTOR");
        return  user;
    }

    @ModelAttribute("herbGroupCommand")
    public HerbClass setupHerbGroup(){
        HerbClass herbClass = new HerbClass();
        return herbClass;
    }

  /*  @ModelAttribute("herbCommand")
    public HerbDto setupHerb(){
        HerbDto herb = new HerbDto();
        Clinic clinic = shopService.findAllClinics().get(0);
        herb.setHerbClasses(clinic.getHerbClasses());
        return herb;
    }*/

    @ModelAttribute("herbCommand")
    public Herb setupHerb(){
        return new Herb();
    }
/*
    @ModelAttribute("clinics")
    public List<Clinic> listClinic(){
        return shopService.findAllClinics();

    }*/

   /* @GetMapping("/home")
    public String home() {
        return "/home";
    }

    @GetMapping("/login")
    public String login() {
        return "/login";
    }

    @GetMapping("/403")
    public String error403() {
        return "/error/403";
    }*/
/*
    @RequestMapping("/event-filler/start")
    public String fillerStart(ModelMap model) {
        if(taskScheduler.getScheduledExecutor().isShutdown()){
            logger.error("Thread Pool Task Scheduler is cancellation ... (this is not an error)");  // force to send system admin email
            String message = "Event Filler Scheduler already Starts ...";
            model.addAttribute("message", message);
            return "/home";
        }
        if (eventFillerFuture == null || eventFillerFuture.isDone() ){
            eventFillerFuture = taskScheduler.schedule(new EventFillerTaskRunner(), periodicTrigger);
            // eventFillerFuture= taskScheduler.schedule(new EventFillerTaskRunner(), cronTrigger);
           logger.error("Start Event Filler Scheduler ... (this is not an error)");  // force to send system admin email

            String message = "Start Event Filler Scheduler ...";
            model.addAttribute("message", message);
           return "/home";
        }else{
            logger.error("Event Filler Scheduler already Starts ... (this is not an error)");  // force to send system admin email
            String message = "Event Filler Scheduler already Starts ...";
            model.addAttribute("message", message);
            return "/home";
            // return "Event Filler Scheduler already Starts ...";
        }
    }

    @RequestMapping("/event-filler/stop")
    public String fillerStop(ModelMap model) {
        if(taskScheduler.getScheduledExecutor().isShutdown()){
            logger.error("Thread Pool Task Scheduler is cancellation ... (this is not an error)");  // force to send system admin email
            String message = "Event Filler Scheduler already Starts ...";
            model.addAttribute("message", message);
            return "/home";
        }
        if (eventFillerFuture == null || eventFillerFuture.isDone()){
            logger.error("Event Filler Scheduler already stops ... (this is not an error)"); // force to send system admin email
            String message = "Event Filler Scheduler already stops ...";
            model.addAttribute("message", message);
            return "/home";
          //  return "Event Filler Scheduler already stops ...";
        }else {
            eventFillerFuture.cancel(true);
            logger.error("Stop Event Filler Scheduler ... (this is not an error)"); // force to send system admin email
            String message = "Start Event Filler Scheduler ...";
            model.addAttribute("message", message);
            return "/home";
           // return "Start Event Filler Scheduler ...";
        }
    }

    @RequestMapping("event-filler/pause")
    public String fillerPause(@RequestParam(value="duration", defaultValue="4") Integer duration, ModelMap model) throws InterruptedException {
        logger.error("processing will be paused for " + duration + " secs ... (this is not an error)");
        eventFillerFuture.cancel(true);
        Thread.sleep(duration * 1000l);
        eventFillerFuture = taskScheduler.schedule(new EventFillerTaskRunner(), periodicTrigger);

        String message = "processing will be paused for " + duration + " secs";
        model.addAttribute("message", message);
        return "/home";
        //return "processing will be paused for " + duration + " secs";
    }

    @RequestMapping("/event-processor/start")
    public String processorStart(ModelMap model) {
        eventProcessorFuture = taskScheduler.schedule(new EventProcessorTaskRunner(), periodicTrigger);
        // eventFillerFuture= taskScheduler.schedule(new EventFillerTaskRunner(), cronTrigger);
        logger.error("Start Event Processor Scheduler ... (this is not an error)");  // force to send system admin email

        String message = "Event Processor startup ...";
        model.addAttribute("message", message);
        return "/home";
       // return "Start Event Process Scheduler ...";
    }

    @RequestMapping("/event-processor/stop")
    public String processorStop(ModelMap model) {
        eventProcessorFuture.cancel(true);
        logger.error("Stop Event Processor Scheduler ... (this is not an error)"); // force to send system admin email
      //  return "Start Event Processor Scheduler ...";

        String message = "Start Event Processor Scheduler ...";
        model.addAttribute("message", message);
        return "/home";
    }*/
/*

    @RequestMapping("/event-server/shutdown")
    public String scheduledServerShutdown(ModelMap model) {
        taskScheduler.shutdown();
        logger.error("Event Scheduler Shutdown ... (this is not an error)");  // force to send system admin email
      //  return "Event Scheduler Shutdown ...";

        String message = "Event Scheduler Shutdown ...";
        model.addAttribute("message", message);
        return "/home";
    }
*/
/*

    @RequestMapping("/event-server/shutdown")
    public String scheduledServerShutdown(ModelMap model) {
        ScheduledExecutorService scheduledExecutorService = taskScheduler.getScheduledExecutor();

        if(eventProcessorFuture != null)
           eventFillerFuture.cancel(true);
        if(eventProcessorFuture != null)
           eventProcessorFuture.cancel(true);
        scheduledExecutorService.shutdown();

        logger.error("Event Scheduler Shutdown ... (this is not an error)");  // force to send system admin email
        //  return "Event Scheduler Shutdown ...";

        String message = "Event Scheduler Shutdown ...";
        model.addAttribute("message", message);
        return "/home";
    }
*/

/*
@GetMapping is a shortcut for @RequestMapping(method=GET)
	@GetMapping(path="/add") // Map ONLY GET Requests
	public @ResponseBody String addNewUser (@RequestParam String name
			, @RequestParam String email) {
		// @ResponseBody means the returned String is the response, not a view name
		// @RequestParam means it is a parameter from the GET or POST request

		User n = new User();
		n.setName(name);
		n.setEmail(email);
		userRepository.save(n);
		return "Saved";
	}

	@GetMapping(path="/all")
	public @ResponseBody Iterable<User> getAllUsers() {
		// This returns a JSON or XML with the users
		return userRepository.findAll();
	}

 */

}
