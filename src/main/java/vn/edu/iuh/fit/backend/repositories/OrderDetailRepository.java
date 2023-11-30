package vn.edu.iuh.fit.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.fit.backend.models.OrderDetail;
@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
}
