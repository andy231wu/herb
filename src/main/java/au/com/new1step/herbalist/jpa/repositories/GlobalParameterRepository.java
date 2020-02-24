package au.com.new1step.herbalist.jpa.repositories;

import au.com.new1step.herbalist.jpa.model.GlobalParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GlobalParameterRepository extends JpaRepository<GlobalParameter, Long> {
    GlobalParameter findBySystem(String system);
}

