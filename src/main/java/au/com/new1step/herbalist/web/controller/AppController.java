package au.com.new1step.herbalist.web.controller;

import au.com.new1step.herbalist.jpa.model.Clinic;
import au.com.new1step.herbalist.service.ShopService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
//@RequestMapping("/webservice")
//@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AppController {
    private static final Logger logger = LoggerFactory.getLogger(AppController.class);

    @Autowired
    private LocaleResolver localeResolver;
    @Autowired
    private ShopService shopService;


    @GetMapping("/")
    public String home1() {
        return "redirect:/herbalist/home";
    }

    @GetMapping("/home")
    public String home2() {
        return "redirect:/herbalist/home";
    }

    @GetMapping("/herbalist/home")
    public String home3(HttpServletRequest request) {
        List<Clinic> shops = shopService.findAllClinics();
        Clinic clinic = null;
        if(shops.size() > 0){
            clinic = shops.get(0);
        }
        request.getSession().setAttribute("shop", clinic);
        return "/herbalist/home";
    }

    @GetMapping("/contactUs")
    public String contactUs() {
        return "/herbalist/contactUs";
    }
    @GetMapping("/userGuide")
    public String userGuide() {
        return "/herbalist/userGuide";
    }

    @GetMapping("/error/403")
    public String error403() {
        return "/error-403";
    }

    @GetMapping("/error/404")
    public String error404() {
        return "/error-404";
    }


    @GetMapping("/login")
    public String login(HttpServletRequest request) {
        List<Clinic> shops = shopService.findAllClinics();
        Clinic clinic = null;
        if(shops.size() > 0){
            clinic = shops.get(0);
        }
        request.getSession().setAttribute("shop", clinic);

        return "/herbalist/login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().removeAttribute("shop");
        return "/herbalist/logout";
    }

}
