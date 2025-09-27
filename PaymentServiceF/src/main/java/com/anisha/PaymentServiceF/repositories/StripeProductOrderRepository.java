package com.anisha.PaymentServiceF.repositories;

import com.anisha.PaymentServiceF.models.StripeProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StripeProductOrderRepository extends JpaRepository<StripeProductOrder, Long> {
    Optional<StripeProductOrder> findByProductId(Long productId);
}