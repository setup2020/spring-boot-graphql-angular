package com.aupas.wallet.entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Wallet {

	@Id
	private String id;
	private Double balance;
	private Long createdAt;
	private String userId;
	@ManyToOne
	private Currency currency;
	@OneToMany(mappedBy="wallet")
	private List<WalletTransaction> walletTransactions;


}
