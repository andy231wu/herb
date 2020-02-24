package au.com.new1step.herbalist.web.controller;

import au.com.new1step.herbalist.common.ApplicationBuilder;
import au.com.new1step.herbalist.exception.EntityNotFoundException;
import au.com.new1step.herbalist.jpa.model.AppUser;
import au.com.new1step.herbalist.jpa.model.GlobalParameter;
import au.com.new1step.herbalist.service.AdminService;
import au.com.new1step.herbalist.web.dto.AppUserDto;
import au.com.new1step.herbalist.web.dto.GlobalParameterDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
//@PreAuthorize("hasRole('ROLE_ADMIN')")
@Slf4j
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/home")
    public String home(Model model){
        return "/herbalist/admin/home";
    }


    @GetMapping("/showGlobalParameter")
    public String showGlobalParameter(Model model) throws EntityNotFoundException {
        GlobalParameter globalParameter = adminService.findGlobalParameterByDefaultSystem();
        GlobalParameterDto globalParameterDto = new GlobalParameterDto();
        globalParameterDto.setNumberOfDoctor(globalParameter.fetchNoOfDoctor());
        globalParameterDto.setNumberOfClinic(globalParameter.fetchNoOfClinic());
        globalParameterDto.setDaysAvailable(globalParameter.fetchAvailableDays());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        globalParameterDto.setStartDate(globalParameter.getStartDate().format(formatter));
        globalParameterDto.setEndDate(globalParameter.getExpiredDate().format(formatter));
        model.addAttribute("globalParameter", globalParameterDto);
        return "/herbalist/admin/showGlobalParameter";
    }


    @GetMapping("/editGlobalParameter")
    public String editGlobalParameter(){
        return "/herbalist/admin/editGlobalParameter";
    }

    @PostMapping("/editGlobalParameter")
    public String editGlobalParameterPost(@ModelAttribute("globalParameterCommand") GlobalParameterDto gpDto, Model model) throws EntityNotFoundException {
        GlobalParameter globalParameter = adminService.findGlobalParameterByDefaultSystem();
        globalParameter.setNoOfDoctor(gpDto.getNumberOfDoctor().toString().getBytes());
        globalParameter.setNoOfClinic(gpDto.getNumberOfClinic().toString().getBytes());
        globalParameter.setAvailableDays(gpDto.getDaysAvailable().toString().getBytes());

        globalParameter.setStartDate(LocalDate.now());
        LocalDate today = LocalDate.now();
        LocalDate expireDate = today.plusDays(globalParameter.fetchAvailableDays());
        globalParameter.setExpiredDate(expireDate);

        adminService.updateGlobalParameter(globalParameter);

        return "redirect:/admin/showGlobalParameter";
    }

    @GetMapping("/createShopOwner")
    public String createShowOwner(){
        return "/herbalist/admin/createShopOwner";
    }

    @PostMapping("/createShopOwner")
     public String createShowOwnerPost(@ModelAttribute ("ownerUserCommand") AppUserDto appUserDto){
        AppUser user = ApplicationBuilder.buildUser(appUserDto);
        adminService.insertUser(user);
        return "redirect:/admin/displayAllUsers";
    }

    @GetMapping("/displayAllUsers")
    public String displayAllUsers(ModelMap model){
        List<AppUser> appUsers = adminService.findAllUsersExcludeAdmin();
        List<AppUserDto> appUserDtos = new ArrayList<>();

        appUsers.forEach(u ->{
                    AppUserDto userDto = new AppUserDto();
                    userDto.setUsername(u.getUsername());
                    userDto.setPassword(u.getPassword());
                    userDto.setRole(u.getRole());
                    userDto.setLastLogin(u.getLastLogin());
                    String num = new String(u.getAvailableDays());
                    Integer validDays = Integer.parseInt(num);
                    userDto.setAvailableDays(validDays);
                    userDto.setStartDate(u.getStartDate());
                    userDto.setExpiredDate(u.getExpiredDate());
                    userDto.setAccountNonExpired(u.isAccountNonExpired());
                    userDto.setAccountNonLocked(u.isAccountNonLocked());
                    userDto.setCredentialsNonExpired(u.isCredentialsNonExpired());
                    userDto.setEnabled(u.isEnabled());
                    appUserDtos.add(userDto);
                });
        model.put("appUsers", appUserDtos);
        return "/herbalist/admin/displayAllUsers";
    }


    @ModelAttribute("globalParameterCommand")
    public GlobalParameterDto fetchGlobalParameter() throws EntityNotFoundException {
        GlobalParameter globalParameter = adminService.findGlobalParameterByDefaultSystem();
        GlobalParameterDto globalParameterDto = new GlobalParameterDto();
        globalParameterDto.setSID(globalParameter.getSID());
        globalParameterDto.setNumberOfDoctor(globalParameter.fetchNoOfDoctor());
        globalParameterDto.setDaysAvailable(globalParameter.fetchAvailableDays());
        globalParameterDto.setNumberOfClinic(globalParameter.fetchNoOfClinic());
        return globalParameterDto;
    }

    @ModelAttribute("ownerUserCommand")
    public AppUserDto setupShopOnwer() throws EntityNotFoundException {
        AppUserDto user = new AppUserDto();
        user.setRole("ROLE_OWNER");

        GlobalParameter globalParameter = adminService.findGlobalParameterByDefaultSystem();
        user.setAvailableDays(globalParameter.fetchAvailableDays());
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);

        return  user;
    }


}
