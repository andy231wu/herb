package au.com.new1step.herbalist.service.impl;

import au.com.new1step.herbalist.exception.EntityHasIdException;
import au.com.new1step.herbalist.exception.EntityNotFoundException;
import au.com.new1step.herbalist.jpa.model.AppUser;
import au.com.new1step.herbalist.jpa.model.GlobalParameter;
import au.com.new1step.herbalist.jpa.repositories.GlobalParameterRepository;
import au.com.new1step.herbalist.jpa.repositories.UserRepository;
import au.com.new1step.herbalist.service.AdminService;
import au.com.new1step.herbalist.util.ThrowExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Transactional
@Service
@Slf4j
public class AdminServiceImpl implements AdminService {

    @Autowired
    private GlobalParameterRepository globalParameterRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MessageSource messageSource;

    @NotNull
    @Value(("${app.system}"))
    private String appSystem;

    @Transactional(readOnly = true)
    @Override
    public GlobalParameter initialFindGlobalParameterByDefaultSystem()  {
        GlobalParameter globalParameter = globalParameterRepository.findBySystem(appSystem);
        if(globalParameter == null){
            return null;
        }
        return globalParameter;
    }

    @Transactional(rollbackFor = {EntityNotFoundException.class})
    @Override
    public GlobalParameter createGlobalParameter(GlobalParameter globalParameter) throws EntityHasIdException {
        log.debug("Creat a Global Parameter Entry");
        if(globalParameter.getSID() != null){
            throw new EntityHasIdException("Adding Global Parameter should not have ID");
        }
        return globalParameterRepository.save(globalParameter);
    }

    @Transactional(rollbackFor = {EntityNotFoundException.class})
    @Override
    public GlobalParameter updateGlobalParameter(GlobalParameter globalParameter) throws EntityNotFoundException {
        findGlobalParameterById(globalParameter.getSID());
        return globalParameterRepository.save(globalParameter);
    }

    @Transactional(rollbackFor = {EntityNotFoundException.class})
    @Override
    public GlobalParameter deleteGlobalParameterById(Long id) throws EntityNotFoundException {
        log.debug("Deleting a GlobalParameter entry with id: {}", id);

        GlobalParameter deleted = findGlobalParameterById(id);
        log.debug("Deleting Global Parameter entry: {}", deleted);

        globalParameterRepository.delete(deleted); // alw: this need
        return deleted;
    }


    @Override
    public GlobalParameter findGlobalParameterById(Long id) throws EntityNotFoundException {
        log.debug("Finding a Global Parameter entry with id: {}", id);

        GlobalParameter globalParameter = globalParameterRepository.findById(id).get();

        log.debug("Found Global Parameter entry: {}", globalParameter.getSID());

        if (globalParameter == null) {
            ThrowExceptionUtil throwExceptionUtil = new ThrowExceptionUtil(messageSource);
            throwExceptionUtil.throwEntityNotFoundException("globalParameter.entity.name", id.toString());
        }

        return globalParameter;
    }


    @Override
    public GlobalParameter findGlobalParameterByDefaultSystem() throws EntityNotFoundException {
        GlobalParameter globalParameter = globalParameterRepository.findBySystem(appSystem);
        if(globalParameter == null){
            throw new EntityNotFoundException("No GlobalParameter entry found for " + appSystem);
        }
        return globalParameter;
    }

    //--------- below need to check later -------
/*
    public GlobalParameterRepository findGlobalParameterRepository() {
        return globalParameterRepository;
    }*/

    public void insertUser(AppUser user){
        userRepository.save(user);
    }



    public List<AppUser> findAllUsersExcludeAdmin(){
        return userRepository.findAllUsersExcludeAdmin();
    }

    public List<AppUser> findDoctorUsers(){
        return userRepository.findDoctorUsers();
    }

    public AppUser findByUsername(String authName){
       return userRepository.findByUsername(authName);
    }

    public void updateLastLogin(LocalDate date, byte[] validDays, String username){
        userRepository.updateLastLogin(date, validDays, username);
    }
}
