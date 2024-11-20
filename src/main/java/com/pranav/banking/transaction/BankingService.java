package com.pranav.banking.transaction;

import com.pranav.banking.config.JWTService;
import com.pranav.banking.exception.InsufficientFundsException;
import com.pranav.banking.exception.InvalidPinException;
import com.pranav.banking.exception.NoTransactionFoundException;
import com.pranav.banking.exception.UserNotFoundException;
import com.pranav.banking.user.User;
import com.pranav.banking.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BankingService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final JWTService jwtService;

    @Transactional
    public CreditResponseDTO credit(CreditRequestDTO request) {
        // Extract the authentication object from SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserNotFoundException("Unauthorized");// Return 401 Unauthorized if no authenticated user
        }
        String username = authentication.getName();
        //fetch user from db
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UserNotFoundException("User not found")
        );
        // If PINs do not match, throw an exception
        if (!verifyCreditPin(request.pin(), user)) {
            throw new InvalidPinException("Invalid PIN");
        }

        //Update the balance
        BigDecimal newBalance = user.getBalance().add(request.amount());
        user.setBalance(newBalance);
        userRepository.save(user);

        //create Transaction
        Transaction transaction = Transaction.builder()
                .user(user)
                .transactionType("CREDIT")
                .amount(request.amount())
                .timestamp(LocalDateTime.now())
                .description(request.description())
                .build();
        transaction = transactionRepository.save(transaction);
        return new CreditResponseDTO(
                transaction.getId(),
                user.getId(),
                newBalance,
                transaction.getTimestamp(),
                transaction.getDescription()
        );

    }
    private boolean verifyCreditPin(Integer enteredPin, User user) {
        // Directly compare the PIN from the request with the stored PIN
        return enteredPin.equals(user.getPin());
    }


    public TransferResponseDTO transfer(TransferRequestDTO request) {
        // Extract the authentication object from SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserNotFoundException("Unauthorized");// Return 401 Unauthorized if no authenticated user
        }
        String username = authentication.getName();

        User fromUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Sender not found"));
        User toUser = userRepository.findById(request.recipientId())
                .orElseThrow(() -> new UserNotFoundException("Recipient not found"));

        if (!verifyTransferPin(request.pin(), fromUser.getPin())) {
            throw new InvalidPinException("Invalid PIN");
        }
        if(Objects.equals(fromUser.getId(), toUser.getId())){
            throw new UserNotFoundException("Cant transfer to Own Account");
        }

        if (fromUser.getBalance().compareTo(request.amount()) < 0) {
            throw new InsufficientFundsException("Insufficient funds");
        }

        fromUser.setBalance(fromUser.getBalance().subtract(request.amount()));
        toUser.setBalance(toUser.getBalance().add(request.amount()));
        userRepository.save(fromUser);
        userRepository.save(toUser);

        // Create a single transaction record
        Transaction transaction = Transaction.builder()
                .user(fromUser) // This represents the sender
                .recipient(toUser) // This represents the recipient
                .transactionType("TRANSFER")
                .amount(request.amount()) // Positive amount
                .timestamp(LocalDateTime.now())
                .description(request.description())
                .build();
        transactionRepository.save(transaction);

        // Return response with transaction details
        return new TransferResponseDTO(
                transaction.getId(),
                fromUser.getId(),
                toUser.getId(),
                fromUser.getBalance(),
                toUser.getBalance(),
                request.amount(),
                request.description(),
                transaction.getTimestamp()
        );

    }

    private boolean verifyTransferPin(Integer enteredPin, Integer FromPin){
        return enteredPin.equals(FromPin);
    }

    public List<TransactionDTO> getTransactionHistory(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        List<Transaction> transactions = transactionRepository.findByUserOrRecipientOrderByTimestampDesc(user, user);

        if (transactions.isEmpty()) {
            throw new NoTransactionFoundException("No transactions found for user: " + username);
        }

        return transactions.stream()
                .map(transaction -> new TransactionDTO(
                        transaction.getId(),
                        transaction.getTransactionType(),
                        transaction.getAmount(),
                        transaction.getTimestamp(),
                        transaction.getDescription(),
                        transaction.getUser() != null ? transaction.getUser().getId() : null,
                        transaction.getRecipient() != null ? transaction.getRecipient().getId() : null
                ))
                .toList();
    }
}
