package com.aupas.wallet.web;

import com.aupas.wallet.dto.AddWalletRequestDTO;
import com.aupas.wallet.entities.Currency;
import com.aupas.wallet.entities.Wallet;
import com.aupas.wallet.entities.WalletTransaction;
import com.aupas.wallet.repositories.CurrencyRepository;
import com.aupas.wallet.repositories.WalletRepository;
import com.aupas.wallet.services.WalletService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class WalletGraphQLController {
    private WalletRepository walletRepository;
    private WalletService walletService;

    private CurrencyRepository currencyRepository;

    public WalletGraphQLController(WalletRepository walletRepository, WalletService walletService, CurrencyRepository currencyRepository) {
        this.walletRepository = walletRepository;
        this.walletService = walletService;
        this.currencyRepository = currencyRepository;
    }

    @QueryMapping
    public List<Wallet> userWallets(){
        return walletRepository.findAll();
    }
    @QueryMapping
    public Wallet walletById(@Argument String id){
        return walletRepository.findById(id).orElseThrow(()->new RuntimeException(String.format("Wallet %s not found",id)));
    }

    @MutationMapping
    public Wallet addWallet(@Argument AddWalletRequestDTO walletRequestDTO){

        return  walletService.save(walletRequestDTO);
    }

    @MutationMapping
    public List<WalletTransaction> walletTransfer(
           @Argument String sourceWalletId,
          @Argument String destinationWalletId,
          @Argument Double amount){
        return  walletService.walletTransfer(sourceWalletId,destinationWalletId,amount);
    }

    @QueryMapping
    public List<Currency> currencies(){
        return  currencyRepository.findAll();
    }
}


