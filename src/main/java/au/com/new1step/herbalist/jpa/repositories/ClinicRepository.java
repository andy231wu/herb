package au.com.new1step.herbalist.jpa.repositories;

import au.com.new1step.herbalist.jpa.model.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClinicRepository extends JpaRepository<Clinic, Long> {

    public Clinic findByChineseName(String chineseName);
}

