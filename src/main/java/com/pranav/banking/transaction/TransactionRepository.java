package com.pranav.banking.transaction;

import com.pranav.banking.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    List<Transaction> findByUserOrderByTimestampDesc(User user);
    List<Transaction> findByUserOrRecipientOrderByTimestampDesc(User user, User recipient);
}
