package au.com.new1step.herbalist.jpa.repositories;

import au.com.new1step.herbalist.jpa.model.Herb;
import au.com.new1step.herbalist.jpa.model.HerbClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HerbRepository extends JpaRepository<Herb, Long> {

   public Herb findByChineseName(String chineseName);

   @Query("select h from Herb h order by h.herbClass.name")
   public List<Herb> findAllHerbsSortByHerbClass();

   @Query("select h from Herb h where h.herbClass = :herbClass order by h.chineseName")
   public List<Herb> findAllHerbsByHerbClass(@Param("herbClass") HerbClass herbClass);

}

