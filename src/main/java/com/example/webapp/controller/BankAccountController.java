package com.example.webapp.controller;

import com.example.webapp.dto.PartialBankAccountInformation;
import com.example.webapp.entity.BankAccount;
import com.example.webapp.repository.BankAccountRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BankAccountController {
    @Autowired
    private BankAccountRepository bankAccountRepository;

    @PostMapping("/account") // Create
    public Long createBankAccount(@RequestBody BankAccount bankAccount) {
        BankAccount savedAccount = bankAccountRepository.save(bankAccount);
        return savedAccount.getId();
    }

    @GetMapping("/accounts")
    public List<PartialBankAccountInformation> getAllBankAccounts() {
        List<BankAccount> allAccounts = bankAccountRepository.findAll();

        // Instantiate list of type we wish to return
        List<PartialBankAccountInformation> listToReturn = new ArrayList<>();

        for (BankAccount accountToConvert : allAccounts) {
            // Instantiate new partial object
            PartialBankAccountInformation dto = new PartialBankAccountInformation();

            // Create new object of interest from object that had ALL info
            dto.setType(accountToConvert.getType());
            dto.setAccountHolder(accountToConvert.getAccountHolder());

            // Add partial object to list that we wish to return
            listToReturn.add(dto);
        }

        return listToReturn;
    }

    @PutMapping("/account")
    public void updateAccount(@RequestBody BankAccount bankAccountClientPassed) {
        // Retrieve existing bankAccount in the database or throw an error if it does not exist
        Optional<BankAccount> existingBankAccount = bankAccountRepository.findById(bankAccountClientPassed.getId());
        if (existingBankAccount.isEmpty()) {
            throw new RuntimeException(String.format("Bank account with id: %s does not exist!", bankAccountClientPassed.getId()));
        }

        BankAccount bankAccountInTheDatabase = existingBankAccount.get();

        // Transfer all data from client to the object retrieved from database
        bankAccountInTheDatabase.setAccountHolder(bankAccountClientPassed.getAccountHolder());
        bankAccountInTheDatabase.setType(bankAccountClientPassed.getType());
        bankAccountInTheDatabase.setOpen(bankAccountClientPassed.isOpen());
        bankAccountInTheDatabase.setLocation(bankAccountClientPassed.getLocation());
        bankAccountInTheDatabase.setAmount(bankAccountClientPassed.getAmount());

        bankAccountRepository.save(bankAccountInTheDatabase);
    }

    @DeleteMapping("/account")
    public void deleteUserById(@RequestParam Long id) {
        bankAccountRepository.deleteById(id);
    }

    @PatchMapping("/account/location")
    public void updateLocation(@RequestParam String newLocation, @RequestParam Long idWeWishToUpdate) {
        // Retrieve existing bankAccount in the database or throw an error if it does not exist
        Optional<BankAccount> existingBankAccount = bankAccountRepository.findById(idWeWishToUpdate);
        if (existingBankAccount.isEmpty()) {
            throw new RuntimeException(String.format("Bank account with id: %s does not exist!", idWeWishToUpdate));
        }

        BankAccount bankAccountInTheDatabase = existingBankAccount.get();

        // Transfer all data from client to the object retrieved from database
        bankAccountInTheDatabase.setLocation(newLocation);

        bankAccountRepository.save(bankAccountInTheDatabase);
    }

    @GetMapping("/account/{holder}")
    public BankAccount getBankAccountByHolder(@PathVariable String holder) {
        return bankAccountRepository.findByAccountHolder(holder);
    }

    @GetMapping("/accounts/balanceLessThan/{amount}")
    public List<BankAccount> getBankAccountsWithBalanceLessThan(@PathVariable double amount) {
        return bankAccountRepository.findAllByAmountLessThan(amount);
    }

    @GetMapping("/exampleBankAccount")
    public BankAccount getBankAccount() {
        // Instantiating an object of type BankAccount
        BankAccount exampleBankAccount = new BankAccount();

        // Setting details on the instantiated object
        exampleBankAccount.setAmount(25.0);
        exampleBankAccount.setAccountHolder("John Smith");
        exampleBankAccount.setOpen(true);
        exampleBankAccount.setType("SAVINGS");
        exampleBankAccount.setLocation("AUS");

        // returning the instantiated object to the client
        return exampleBankAccount;
    }
}
