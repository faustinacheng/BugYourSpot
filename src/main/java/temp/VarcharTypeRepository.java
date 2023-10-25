package temp;

import com.example.bugyourspot.reservation.VarcharType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VarcharTypeRepository extends JpaRepository<VarcharType, Long> {

}

