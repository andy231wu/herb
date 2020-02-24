package au.com.new1step.herbalist.config;

import au.com.new1step.herbalist.security.CustomAccessDeniedHandler;
import au.com.new1step.herbalist.security.SimpleAuthenticationSuccessHandler;
import au.com.new1step.herbalist.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.context.WebApplicationContext;

import javax.sql.DataSource;

// http://docs.spring.io/spring-boot/docs/current/reference/html/howto-security.html
// Switch off the default Spring Boot security configuration
// template turn off
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomAccessDeniedHandler accessDeniedHandler;
    @Autowired
    private SimpleAuthenticationSuccessHandler successHandler;
    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;
    @Autowired
    private LogoutSuccessHandler myLogoutSuccessHandler;

    @Autowired
    private CustomUserDetailsService userDetailsService;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .authorizeRequests()
                    .antMatchers("/").permitAll()
                    .antMatchers("/home").permitAll()
                    .antMatchers("/login").permitAll()
                    .antMatchers("/static/**").permitAll()
                    .antMatchers("/images/**").permitAll()
                    .antMatchers("/wro/**").permitAll()
                    .antMatchers("/herbalist/home").permitAll()
                    .antMatchers("/admin/**").hasRole("ADMIN")
                    .antMatchers("/shop/**").hasAnyRole("OWNER, ADMIN")
                    .antMatchers("/doctor/**").hasAnyRole("DOCTOR, ADMIN")
                    .anyRequest().authenticated()
                   .and()
                .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/homepage.html")
                    .failureUrl("/login?error=true")
                    .successHandler(successHandler)
                    .failureHandler(authenticationFailureHandler)
                  //  .authenticationDetailsSource(authenticationDetailsSource)
                    .permitAll()
                    .and()
                .logout()
                   .logoutSuccessHandler(myLogoutSuccessHandler)
                    .invalidateHttpSession(false)
                    .logoutSuccessUrl("/logout?logSucc=true")
                    .deleteCookies("JSESSIONID")
                    .permitAll()
                    .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler);
    }


    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(11);
    }

}
