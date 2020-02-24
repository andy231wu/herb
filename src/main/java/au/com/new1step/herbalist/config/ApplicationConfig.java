package au.com.new1step.herbalist.config;

import au.com.new1step.herbalist.exception.EntityNotFoundException;
import au.com.new1step.herbalist.jpa.model.GlobalParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Properties;

@Configuration
public class ApplicationConfig {
    @Autowired
    private Environment env;

    // beans

    @Bean
    public String envBean(){
        return env.getProperty("spring.profiles.active");
    }

    @Bean
    public GlobalParameter globalParameter(){
        GlobalParameter globalParameter = new GlobalParameter();
        globalParameter.setSystem(env.getProperty("app.system"));
        globalParameter.setNoOfClinic(env.getProperty("no.of.clinic").getBytes());
        globalParameter.setNoOfDoctor(env.getProperty("no.of.doctors").getBytes());
        globalParameter.setAvailableDays(env.getProperty("days.available").getBytes());
        return globalParameter;
    }

    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:locale/message", "classpath:locale/viewMessages", "classpath:locale/validationMessages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public DataSource dataSource() throws SQLException {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("datasource.driverClassName"));
        dataSource.setUsername(env.getProperty("datasource.username"));
        dataSource.setPassword(env.getProperty("datasource.password"));
        dataSource.setUrl(env.getProperty("datasource.url"));
        return dataSource;
    }

    @Bean
    public Java8TimeDialect java8TimeDialect() {
        return new Java8TimeDialect();
    }

    @Bean
    public WebMvcConfigurer configurer () {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addInterceptors (InterceptorRegistry registry) {
                LocaleChangeInterceptor l = new LocaleChangeInterceptor();
                l.setParamName("language");
                registry.addInterceptor(l);
            }
        };
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.CHINA);
        return slr;
    }

    @Bean
    public JavaMailSenderImpl mailSender()  {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(env.getProperty("mail.host"));
        mailSender.setUsername(env.getProperty("mail.username"));
        mailSender.setPassword(env.getProperty("mail.password"));
        return mailSender;
    }


   /* this bean only use to handle 404 error */
    @Bean
    public SimpleMappingExceptionResolver exceptionResolver(){

        SimpleMappingExceptionResolver exceptionResolver = new SimpleMappingExceptionResolver();

        Properties exceptionMappings = new Properties();
        exceptionMappings.put(EntityNotFoundException.class.getName(), "error-404");

        // do not want to handle other error such s 500, 405 herr, let default to mapping to error.html
        //   exceptionMappings.put("java.lang.Exception", "error");
        //    exceptionMappings.put("java.lang.RuntimeException", "error");

        exceptionResolver.setExceptionMappings(exceptionMappings);
        Properties statusCodes = new Properties();
        statusCodes.put("error-404", "404");

        exceptionResolver.setStatusCodes(statusCodes);

        return exceptionResolver;
    }
}
