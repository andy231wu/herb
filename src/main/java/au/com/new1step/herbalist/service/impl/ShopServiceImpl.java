package au.com.new1step.herbalist.service.impl;

import au.com.new1step.herbalist.common.ProcessingStatus;
import au.com.new1step.herbalist.jpa.model.*;
import au.com.new1step.herbalist.jpa.repositories.*;
import au.com.new1step.herbalist.service.ShopService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class ShopServiceImpl implements ShopService {

	private Logger log = LoggerFactory.getLogger(ShopServiceImpl.class);

	@Autowired
	private GlobalParameterRepository globalParameterRepository;

	@Autowired
    private HerbClassRepository herbClassRepository;

    @Autowired
    private  HerbRepository herbRepository;

	@Autowired
	private ClinicRepository clinicRepository;

	@Autowired
	private PrescriptionRepository prescriptionRepository;

	@Autowired
	private ClinicAddressRepository clinicAddressRepository;

	public Clinic saveClinic(Clinic clinic){
		if(clinicRepository.findAll().size() < globalParameterRepository.findBySystem("Herb").fetchNoOfClinic()) {
			return clinicRepository.save(clinic);
		}else {
			//throw new ????
			return null;
		}
	}

	public Clinic updateClinic(Clinic clinic){
		if(clinic.getCID() == null){
			//throw new EntityNotHaveIdException("Must have Id");
			return null;
		}
		// todo: alw
		//clinicRepository.findById(clinic.getCID());

		return clinicRepository.save(clinic);
	}

	public void deleteClinic(Clinic clinic){
		if(clinic.getCID() != null){
			clinicRepository.delete(clinic);
		}
	}

	public HerbClass updateHerbClass(HerbClass herbClass){
		if(herbClass.getCID() == null){
			return null;
		}

		return herbClassRepository.save(herbClass);
	}

	public List<Herb> findAllHerbsSortByHerbClass(){
		return herbRepository.findAllHerbsSortByHerbClass();
	}

	public Prescription updatePrescription(Prescription prescription){
		if(prescription.getPsID() == null){
			return null; // should throw exceeption
		}
		return prescriptionRepository.save(prescription);
	}

	public Prescription findFirstUnprocessedPrescription() {
		return prescriptionRepository.findFirstByProcessingStatusOrderByUpdateDateAsc(ProcessingStatus.UNPROCESSED);
	}

	public Prescription findLastUnprocessedPrescription() {
		return prescriptionRepository.findFirstByProcessingStatusOrderByUpdateDateDesc(ProcessingStatus.UNPROCESSED);
	}

	public Prescription findPrescriptionById(Long id){
		return prescriptionRepository.findByPsID(id);
	}

	public List<Clinic> findAllClinics(){
		return clinicRepository.findAll();
	}
	public Clinic findClinicByChineseName(String chineseName){
		return clinicRepository.findByChineseName(chineseName);
	}
	public HerbClass findHerbClassByName(String name){
		return herbClassRepository.findByName(name);
	}
	public List<HerbClass> findAllHerbClasses(){
		return herbClassRepository.findAll();
	}
	public List<Herb> findAllHerbsByHerbClass(HerbClass herbClass){
		return herbRepository.findAllHerbsByHerbClass(herbClass);
	}
	public Herb findHerbByChineseName(String herbName){
		return herbRepository.findByChineseName(herbName);
	}

	public ClinicAddress findAddressByAddrId(Long addrId){
		return clinicAddressRepository.findByAddrId(addrId);
	}
}