package temp;

import com.example.bugyourspot.reservation.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttributeRepository extends JpaRepository<Attribute, Long> {

   // TODO: find custom field by client ID and field name.
}

