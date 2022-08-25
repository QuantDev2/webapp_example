package com.example.webapp.repository;

import com.example.webapp.dto.IPartialBankAccountInformation;
import com.example.webapp.entity.BankAccount;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountRepository extends CrudRepository<BankAccount, Long> {
    BankAccount findByAccountHolder(String accountHolder);
    List<BankAccount> findAllByAmountLessThan(double amount);
    List<BankAccount> findAll();
}
