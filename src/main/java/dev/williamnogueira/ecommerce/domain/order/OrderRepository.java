package dev.williamnogueira.ecommerce.domain.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
interface OrderRepository extends JpaRepository<OrderEntity, UUID> {
    Page<OrderEntity> findAllByCustomerId(UUID customerId, Pageable pageable);
}
