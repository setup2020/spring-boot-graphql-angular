package com.aupas.wallet.entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Currency {

	@Id
	private String code;
	private String name;
	private String symbol;
	private Double salePrice;
	private Double puchasePrice;
	
}
