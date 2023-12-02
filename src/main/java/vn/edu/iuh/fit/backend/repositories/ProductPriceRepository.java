package vn.edu.iuh.fit.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.fit.backend.models.ProductPrice;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ProductPriceRepository extends JpaRepository<ProductPrice, Long> {

    @Query("SELECT pp FROM ProductPrice pp " +
            "WHERE pp.product.product_id = :productId " +
            "ORDER BY pp.price_date_time DESC")
    Optional<ProductPrice> findNearestPrice(@Param("productId") Long productId);
}
