package au.com.new1step.herbalist.jpa.repositories;

import au.com.new1step.herbalist.jpa.model.HerbClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HerbClassRepository extends JpaRepository<HerbClass, Long> {

   public HerbClass findByName(String name);

}

