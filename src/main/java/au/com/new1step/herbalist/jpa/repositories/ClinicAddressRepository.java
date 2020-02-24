package au.com.new1step.herbalist.jpa.repositories;

import au.com.new1step.herbalist.jpa.model.ClinicAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClinicAddressRepository extends JpaRepository<ClinicAddress, Long> {
    public List<ClinicAddress> findBySuburb(String suburb);
    public ClinicAddress findByAddrId(Long addrId);
}

