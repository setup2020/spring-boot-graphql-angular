package com.aupas.wallet.entities;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.aupas.wallet.enums.TransactionType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class WalletTransaction {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private Long timestamps;
	private Double amount;
	private Double currentSaleCurrencyPrice;
	private Double currentPurchaseCurrencyPrice;
	@ManyToOne
	private Wallet wallet;
	@Enumerated(EnumType.STRING)
	private TransactionType type;

}
