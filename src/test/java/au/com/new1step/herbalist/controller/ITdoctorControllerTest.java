package au.com.new1step.herbalist.controller;

import au.com.new1step.herbalist.common.TestDoctorBuilder;
import au.com.new1step.herbalist.common.TestWebAppContext;
import au.com.new1step.herbalist.jpa.model.Clinic;
import au.com.new1step.herbalist.jpa.model.ClinicAddress;
import au.com.new1step.herbalist.jpa.model.Doctor;
import au.com.new1step.herbalist.service.DoctorService;
import au.com.new1step.herbalist.service.ShopService;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { TestWebAppContext.class})  // can replace by @ContextConfiguration(classes = {TestWebAppContext.class})
@AutoConfigureMockMvc
@ActiveProfiles("local")
public class ITdoctorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DoctorService doctorServiceMock;

    @MockBean
    private ShopService shopServiceMock;

    @Test
    @WithMockUser(username = "doctor", password="password", roles = { "DOCTOR" })
    public void cannDisplayHomePage() throws Exception {
        mockMvc.perform(get("/doctor/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("/herbalist/doctor/home"));
    }

    @Test
    @WithMockUser(username = "doctor", password="password", roles = { "OWNER" })
    public void cannotDisplayHomePageAsAccessDenied() throws Exception {
        mockMvc.perform(get("/doctor/home"))
                .andExpect(status().isFound())  // 302
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error/403"));
    }

    @Test
    @WithMockUser(username = "doctor", password="password", roles = { "DOCTOR" })
    public void canDisplayCreateDoctorDetailPage() throws Exception{
       Doctor doctor = new TestDoctorBuilder()
               .withDrID(1L)
               .withFirstName("ANDY")
               .withLastName("WU")
               .build();
        Clinic clinic = new Clinic();
        clinic.setCID(1L);
        clinic.setEnglishName("Eastwood Chinese Herbalist");

        ClinicAddress address1 = new ClinicAddress();
        address1.setAddrId(1L);
        address1.setAddress1("21 Audine Ave Epping");
        ClinicAddress address2 = new ClinicAddress();
        address2.setAddrId(2L);
        address2.setAddress1("24 Sydney Road");


        clinic.addAddress(address1);
        clinic.addAddress(address2);
        when(shopServiceMock.saveClinic(clinic)).thenReturn(clinic);
       when(doctorServiceMock.findLoginDoctor()).thenReturn(doctor);

       mockMvc.perform(get("/createDoctorDetail"))
               .andExpect(status().isOk())
               .andExpect(view().name("/herbalist/doctor/createDoctorDetail"))
               .andExpect(model().attribute("doctorCommand", hasItem(
                       Matchers.allOf(
                               hasProperty("firstName", is("ANDY")),
                               hasProperty("lastName", is("WU"))
                       )
               )))
               .andExpect(model().attribute("clinicAddresses", hasSize(2)))
               .andExpect(model().attribute("clinicAddresses", hasItem(
                       Matchers.allOf(
                               hasProperty("addrId", is(1L)),
                               hasProperty("address1", is("21 Audine Ave Epping"))
                       )
               )))
               .andExpect(model().attribute("clinicAddresses", hasItem(
                       Matchers.allOf(
                               hasProperty("addrId", is(2L)),
                               hasProperty("address1", is("24 Sydney Road"))
                       )
               )));
        verify(doctorServiceMock, times(1)).findLoginDoctor();
        verify(shopServiceMock, times(1)).findAllClinics();
        verifyNoMoreInteractions(doctorServiceMock);
        verifyNoMoreInteractions(shopServiceMock);
    }

}
