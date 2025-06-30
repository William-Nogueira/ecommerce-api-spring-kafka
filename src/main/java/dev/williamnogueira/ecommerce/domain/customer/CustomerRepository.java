package dev.williamnogueira.ecommerce.domain.customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, UUID> {

    Page<CustomerEntity> findAllByActiveTrue(Pageable pageable);

    @EntityGraph(attributePaths = "address")
    Optional<CustomerEntity> findByIdAndActiveTrue(UUID id);

    boolean existsByEmailIgnoreCase(String email);
}
