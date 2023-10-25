package temp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IntegerTypeRepository extends JpaRepository<Integer, Long> {

}

