package com.aupas.wallet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aupas.wallet.entities.Currency;

public interface CurrencyRepository extends JpaRepository<Currency,String>{

}
