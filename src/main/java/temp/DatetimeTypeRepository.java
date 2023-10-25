package temp;

import com.example.bugyourspot.reservation.DatetimeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DatetimeTypeRepository extends JpaRepository<DatetimeType, Long> {

}

