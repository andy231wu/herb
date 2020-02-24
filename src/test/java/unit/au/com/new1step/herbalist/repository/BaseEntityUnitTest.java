package unit.au.com.new1step.herbalist.repository;

import au.com.new1step.herbalist.jpa.model.Doctor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

public class BaseEntityUnitTest {

    @Before
    public void setup() {
        Authentication auth = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(securityContext.getAuthentication()).thenReturn(auth);
        Mockito.when(auth.getName()).thenReturn("andywu");
    }

    @Test
    public void testBuildWithManadatoryInfo(){
        Doctor build = new Doctor();
        build.setFirstName("Andy");

        assertNull(build.getDrID());
        assertNull(build.getCreateDate());
        assertNull((build.getCreateUser()));
        assertNull(build.getUpdateDate());
        assertNull((build.getUpdateUser()));
        assertEquals("Andy",build.getFirstName());
}

    @Test
    public void testBuildPrePersist(){
        Doctor build = new Doctor();
        build.setFirstName("Andy");
        build.prePersist();

        assertNull(build.getDrID());
        assertNotNull(build.getCreateDate());
        assertEquals("andywu", build.getCreateUser());
        assertNotNull(build.getUpdateDate());
        assertEquals("andywu", build.getUpdateUser());
        assertEquals("Andy",build.getFirstName());
        assertTrue(build.getUpdateDate().isEqual(build.getCreateDate()));
    }


    @Test
    public void testBuildPreUpdate() {
        Doctor build = new Doctor();
        build.prePersist();

        pause(1000);

        build.preUpdate();

        assertNull(build.getDrID());
        assertNotNull(build.getCreateDate());
        assertEquals("andywu", build.getCreateUser());
        assertNotNull(build.getUpdateDate());
        assertEquals("andywu", build.getUpdateUser());
        assertTrue(build.getUpdateDate().isAfter(build.getCreateDate()));
    }


    private void pause(long timeInMillis) {
        try {
            Thread.currentThread().sleep(timeInMillis);
        }
        catch (InterruptedException e) {
            //Do Nothing
        }
    }
}
