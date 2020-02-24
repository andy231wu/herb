package au.com.new1step.herbalist.security;

import au.com.new1step.herbalist.jpa.model.AppUser;
import au.com.new1step.herbalist.jpa.repositories.UserRepository;
import au.com.new1step.herbalist.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;

@Component
public class SimpleAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Autowired
    private AdminService adminService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest arg0, HttpServletResponse arg1, Authentication authentication)
            throws IOException, ServletException {

        // update AppUser table
        if(!"admin".equalsIgnoreCase(authentication.getName())) {
            AppUser user = adminService.findByUsername(authentication.getName());
            LocalDate today = LocalDate.now();
            String num = new String(user.getAvailableDays());
            Integer validDays = Integer.parseInt(num);
            if (user.getLastLogin() == null || !today.isEqual(user.getLastLogin())) {
                validDays += -1;
            }
            adminService.updateLastLogin(LocalDate.now(), validDays.toString().getBytes(), authentication.getName());
        }

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        authorities.forEach(authority -> {

            if(authority.getAuthority().equals("ROLE_OWNER")) {
                try {
                    redirectStrategy.sendRedirect(arg0, arg1, "/shop/home");
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else if(authority.getAuthority().equals("ROLE_DOCTOR")) {
                try {
                    redirectStrategy.sendRedirect(arg0, arg1, "/doctor/home");
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } else if(authority.getAuthority().equals("ROLE_ADMIN")) {
                try {
                    redirectStrategy.sendRedirect(arg0, arg1, "/admin/home");
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                throw new IllegalStateException();
            }

        });

    }

}