package au.com.new1step.herbalist.service;

import au.com.new1step.herbalist.exception.EntityHasIdException;
import au.com.new1step.herbalist.exception.EntityNotFoundException;
import au.com.new1step.herbalist.jpa.model.AppUser;
import au.com.new1step.herbalist.jpa.model.GlobalParameter;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface AdminService {
    public GlobalParameter initialFindGlobalParameterByDefaultSystem();
    public GlobalParameter findGlobalParameterById(Long id) throws EntityNotFoundException;
    public GlobalParameter findGlobalParameterByDefaultSystem() throws EntityNotFoundException;

    public GlobalParameter createGlobalParameter(GlobalParameter globalParameter) throws EntityHasIdException;
    public GlobalParameter updateGlobalParameter(GlobalParameter globalParameter) throws EntityNotFoundException;
    public GlobalParameter deleteGlobalParameterById(Long id) throws EntityNotFoundException;

    public void insertUser(AppUser user);
    public List<AppUser> findAllUsersExcludeAdmin();
    public List<AppUser> findDoctorUsers();
    public AppUser findByUsername(String authName);
    public void updateLastLogin(LocalDate date, byte[] validDays, String username);
}
