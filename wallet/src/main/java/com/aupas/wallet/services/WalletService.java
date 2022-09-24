package com.aupas.wallet.services;

import com.aupas.wallet.dto.AddWalletRequestDTO;
import com.aupas.wallet.entities.Currency;
import com.aupas.wallet.entities.Wallet;
import com.aupas.wallet.entities.WalletTransaction;
import com.aupas.wallet.enums.TransactionType;
import com.aupas.wallet.repositories.CurrencyRepository;
import com.aupas.wallet.repositories.WalletRepository;
import com.aupas.wallet.repositories.WalletTransactionRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@Transactional
public class WalletService {
    private CurrencyRepository currencyRepository;
    private WalletRepository walletRepository;
    private WalletTransactionRepository walletTransactionRepository;

    public WalletService(CurrencyRepository currencyRepository, WalletRepository walletRepository, WalletTransactionRepository walletTransactionRepository) {
        this.currencyRepository = currencyRepository;
        this.walletRepository = walletRepository;
        this.walletTransactionRepository = walletTransactionRepository;
    }

    public void loadData() throws IOException {
        URI uri=new ClassPathResource("codes-all.csv").getURI();
        Path path= Paths.get(uri);
        List<String> lines= Files.readAllLines(path);
        for (int i=0; i<lines.size();i++){
            String[] line=lines.get(i).split(",");
            System.out.println(Arrays.toString(line));
            Currency currency=Currency.builder()
                    .code(line[2])
                    .name(line[1])
                    .salePrice(0.0)
                    .puchasePrice(0.0)
                    .build();

            currencyRepository.save(currency);

        }
        Stream.of("XAF","USD","EUR","CAD").forEach(currencyCode->{
            Currency currency1=currencyRepository.findById(currencyCode)
                    .orElseThrow(
                            ()->new RuntimeException(String.format("Currency %s not found",currencyCode)));
            Wallet wallet=new Wallet();
            wallet.setBalance(1000.0);
            wallet.setCurrency(currency1);
            wallet.setCreatedAt(System.currentTimeMillis());
            wallet.setUserId("user1");
            wallet.setId(UUID.randomUUID().toString());
            walletRepository.save(wallet);
        });
        walletRepository.findAll().forEach(wallet -> {
            for (int i=0; i<10; i++){
                WalletTransaction debitWT=WalletTransaction.builder()
                        .amount(Math.random()*1000)
                        .wallet(wallet)
                        .type(TransactionType.DEBIT)
                        .timestamps(System.currentTimeMillis())
                        .build();
                walletTransactionRepository.save(debitWT);
                wallet.setBalance(wallet.getBalance()-debitWT.getAmount());
                walletRepository.save(wallet);

                WalletTransaction creditWT=WalletTransaction.builder()
                        .amount(Math.random()*1000)
                        .wallet(wallet)
                        .timestamps(System.currentTimeMillis())
                        .type(TransactionType.CREDIT).build();
                walletTransactionRepository.save(creditWT);
                wallet.setBalance(wallet.getBalance()+debitWT.getAmount());
                walletRepository.save(wallet);

            }
        });
    }

   public  Wallet save(AddWalletRequestDTO addWalletRequestDTO){
        Currency currency=currencyRepository.findById(addWalletRequestDTO.currencyCode()).orElseThrow(()->new RuntimeException(String.format("Currency %s not found",addWalletRequestDTO.currencyCode())));
         Wallet wallet=Wallet.builder()
                 .balance(addWalletRequestDTO.balance())
                 .id(UUID.randomUUID().toString())
                 .createdAt(System.currentTimeMillis())
                 .userId("user1")
                 .currency(currency)
                 .build();
         return  walletRepository.save(wallet);
   }

   public List<WalletTransaction> walletTransfer(String sourceWalletId,String destinationWalletId,Double amount){
        Wallet sourceWallet=walletRepository.findById(sourceWalletId).orElseThrow(()->
            new RuntimeException("Wallet "+sourceWalletId+" Not found")
        );

       Wallet destinationWallet=walletRepository.findById(destinationWalletId).orElseThrow(()->
               new RuntimeException("Wallet "+destinationWalletId+" Not found")
       );


       WalletTransaction sourceWalletTransaction=WalletTransaction.builder()
               .timestamps(System.currentTimeMillis())
               .type(TransactionType.DEBIT)
               .amount(amount)
               .currentSaleCurrencyPrice(sourceWallet.getCurrency().getSalePrice())
               .currentPurchaseCurrencyPrice(sourceWallet.getCurrency().getPuchasePrice())

               .wallet(sourceWallet)
               .build();
        walletTransactionRepository.save(sourceWalletTransaction);
        sourceWallet.setBalance(sourceWallet.getBalance()-amount);

       Double destinationAmount=amount*(sourceWallet.getCurrency().getSalePrice()/destinationWallet.getCurrency().getPuchasePrice());

       WalletTransaction destinationWalletTransaction=WalletTransaction.builder()
               .timestamps(System.currentTimeMillis())
               .type(TransactionType.CREDIT)
               .amount(destinationAmount)
               .currentSaleCurrencyPrice(destinationWallet.getCurrency().getSalePrice())
               .currentPurchaseCurrencyPrice(destinationWallet.getCurrency().getPuchasePrice())
               .wallet(destinationWallet)
               .build();
       walletTransactionRepository.save(destinationWalletTransaction);
       destinationWallet.setBalance(sourceWallet.getBalance()+destinationAmount);

       return Arrays.asList(sourceWalletTransaction,destinationWalletTransaction);
   }

}
