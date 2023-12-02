package vn.edu.iuh.fit.backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.fit.backend.enums.ProductStatus;
import vn.edu.iuh.fit.backend.models.Product;
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAllByStatus(ProductStatus status, Pageable pageable);
}
