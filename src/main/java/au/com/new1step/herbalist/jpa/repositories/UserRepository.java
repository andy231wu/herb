package au.com.new1step.herbalist.jpa.repositories;

import au.com.new1step.herbalist.jpa.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {

    public AppUser findByUsername(String username);

   @Query("UPDATE AppUser u SET u.lastLogin=:lastLogin, u.availableDays=:availableDays  WHERE u.username =:username")
   @Modifying
   @Transactional
   public void updateLastLogin(@Param("lastLogin") LocalDate lastLogin, @Param("availableDays") byte[] availableDays, @Param("username") String username);

   @Query("from AppUser u WHERE u.role <> 'ROLE_ADMIN' ")
   public List<AppUser> findAllUsersExcludeAdmin();

   @Query("from AppUser u WHERE u.role = 'ROLE_DOCTOR' ")
   public List<AppUser> findDoctorUsers();

}

