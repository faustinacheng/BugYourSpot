package temp;

import com.example.bugyourspot.reservation.BooleanType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BooleanTypeRepository extends JpaRepository<BooleanType, Long> {

}

