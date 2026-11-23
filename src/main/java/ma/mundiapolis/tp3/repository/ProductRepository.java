package ma.mundiapolis.tp3.repository;

import ma.mundiapolis.tp3.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
