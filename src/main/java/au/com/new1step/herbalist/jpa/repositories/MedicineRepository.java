package au.com.new1step.herbalist.jpa.repositories;

import au.com.new1step.herbalist.jpa.model.Herb;
import au.com.new1step.herbalist.jpa.model.HerbClass;
import au.com.new1step.herbalist.jpa.model.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {


}

