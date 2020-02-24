package au.com.new1step.herbalist.security;

import au.com.new1step.herbalist.config.ApplicationConfig;
import au.com.new1step.herbalist.jpa.model.AppUser;
import au.com.new1step.herbalist.jpa.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.ServletContext;
import java.time.LocalDate;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@TestPropertySource(locations={"classpath:application.properties", "classpath:application-local.properties"})
@ContextConfiguration(classes = {UserRepository.class, ApplicationConfig.class})
@DirtiesContext
public class SpringDataWithSecurityTest {
    AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
    @Autowired
    private ServletContext servletContext;
    private static UserRepository userRepository;

    @Value("${app.system}")
    private String system;

    @Before
    public void testInit() {
        ctx.register(ApplicationConfig.class);
        ctx.setServletContext(servletContext);
        ctx.refresh();
        userRepository = ctx.getBean(UserRepository.class);
    }

   // @AfterClass
    public static void tearDown() {
       // tweetRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testPropertiesFile(){
        Assert.assertEquals("herb", system);
    }
    @Test
    public void givenAppUser_whenLoginSuccessful_shouldUpdateLastLogin() {
        AppUser appUser = userRepository.findByUsername("doctor");
        Authentication auth = new UsernamePasswordAuthenticationToken(new AppUserPrincipal(appUser), null, DummyContentUtil.getAuthorities());
        SecurityContextHolder.getContext()
            .setAuthentication(auth);
        userRepository.updateLastLogin(LocalDate.now(), null,null);
    }

    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void givenNoAppUserInSecurityContext_whenUpdateLastLoginAttempted_shouldFail() {
        userRepository.updateLastLogin(LocalDate.now(), null, null);
    }

}
