package com.aupas.wallet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aupas.wallet.entities.Wallet;

public interface WalletRepository extends JpaRepository<Wallet,String>{

}
