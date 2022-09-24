package com.aupas.wallet.dto;

public record AddWalletRequestDTO(
        Double balance,
        String currencyCode){}