package au.com.new1step.herbalist.jpa.repositories;

import au.com.new1step.herbalist.jpa.model.AppUser;
import au.com.new1step.herbalist.jpa.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    public List<Doctor> findByMobile(String mobile);
    public List<Doctor> findByFirstName(String firstName);

    @Query("select d from Doctor d where d.firstName = :firstName and d.lastName = :lastName")
    public List<Doctor> findByFirstNameAndLastName(@Param("firstName") String firstName, @Param("lastName") String lastName);

    public Doctor findByUser(AppUser user);
}

