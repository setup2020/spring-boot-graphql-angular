package com.aupas.wallet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aupas.wallet.entities.WalletTransaction;

public interface WalletTransactionRepository extends JpaRepository<WalletTransaction,Long>{

}
