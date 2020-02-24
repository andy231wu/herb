package au.com.new1step.herbalist.jpa.repositories;


import au.com.new1step.herbalist.jpa.model.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    public Patient findByPhone(String phone);
    public Patient findByFirstName(String firstName);

    @Query("from Patient where firstName = :firstName and lastName = :lastName")
    public List<Patient> findByFirstNameAndLastName(@Param("firstName") String firstName, @Param("lastName") String lastName);

    @Query("from Patient p order by p.lastName")
    public List<Patient> findAllPatients();

    @Query("from Patient p order by p.lastName")
    public Page<Patient> findAll(Pageable pageable);
}


