package com.tms.repository;

import com.tms.model.entity.PaymentMethod;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentMethodRepository extends AbstractRepository<PaymentMethod, Integer> {
}

