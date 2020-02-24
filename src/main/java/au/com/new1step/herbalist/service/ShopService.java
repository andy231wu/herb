package au.com.new1step.herbalist.service;

import au.com.new1step.herbalist.jpa.model.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ShopService {

    public Clinic saveClinic(Clinic clinic);
    public Clinic updateClinic(Clinic clinic);

    public HerbClass updateHerbClass(HerbClass herbClass);
    public HerbClass findHerbClassByName(String name);
    public List<HerbClass> findAllHerbClasses();

    public Prescription updatePrescription(Prescription prescription);
    public Clinic findClinicByChineseName(String chineseName);

    public Herb findHerbByChineseName(String herbName);
    public List<Herb> findAllHerbsSortByHerbClass();
    public List<Herb> findAllHerbsByHerbClass(HerbClass herbClass);

    public Prescription findFirstUnprocessedPrescription();
    public Prescription findPrescriptionById(Long id);
    public List<Clinic> findAllClinics();
    public ClinicAddress findAddressByAddrId(Long addrId);

}
