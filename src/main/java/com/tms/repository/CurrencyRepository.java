package com.tms.repository;

import com.tms.model.entity.Currency;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends AbstractRepository<Currency, Integer> {
}
