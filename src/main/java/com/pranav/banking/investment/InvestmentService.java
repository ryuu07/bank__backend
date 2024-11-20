package com.pranav.banking.investment;

import com.pranav.banking.exception.InsufficientFundsException;
import com.pranav.banking.exception.NoTransactionFoundException;
import com.pranav.banking.exception.UserNotFoundException;
import com.pranav.banking.user.User;
import com.pranav.banking.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvestmentService {

    private final InvestmentRepository repository;
    private final InvestmentOptionsRepository investmentOptionsRepository;
    private final UserRepository userRepository;

    public List<InvestmentListDTO> getInvestmentsByUserId(String username) {
        try {
            List<Investment> investments = repository.findByUser_Username(username);
            return investments.stream()
                    .map(investment -> new InvestmentListDTO(
                            investment.getId(),
                            investment.getInvestmentOption().getName(),
                            investment.getAmount(),
                            investment.getCreatedAt(),
                            investment.getEndTime(),
                            investment.getPayout(),
                            investment.getReferenceId()
                    ))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new NoTransactionFoundException("Error fetching investments for user: " + username);
        }
    }

    public InvestmentResponseDTO createInvestment(String username, InvestmentRequestBody request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("user Not found"));

        InvestmentOptions option = investmentOptionsRepository.findById(request.investmentOptionId())
                .orElseThrow(() -> new NoTransactionFoundException("Investment Option not found"));

        if (request.amount().compareTo(option.getMinimumAmount()) < 0) {
            throw new InsufficientFundsException("Investment amount is less than the minimum required");
        }

        if (user.getBalance().compareTo(request.amount()) < 0) {
            throw new InsufficientFundsException("Insufficient balance");
        }
        user.setBalance(user.getBalance().subtract(request.amount()));
        userRepository.save(user);

        Investment investment = Investment.builder()
                .user(user)
                .investmentOption(option)
                .amount(request.amount())
                .createdAt(LocalDateTime.now())
                .referenceId(generateUniqueReferenceId())// Set the timestamp here
                .build();

        Investment savedInvestment = repository.save(investment);
        return new InvestmentResponseDTO(
                savedInvestment.getId(),
                savedInvestment.getUser().getUsername(),
                savedInvestment.getInvestmentOption().getName(),
                savedInvestment.getAmount(),
                savedInvestment.getCreatedAt()
        );

    }

    public String withdrawInvestment(String username, InvestmentWithdrawDTO request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Investment investment = repository.findByReferenceId(request.investmentReferenceId())
                .orElseThrow(() -> new NoTransactionFoundException("Investment not found"));

        if (!investment.getUser().getId().equals(user.getId())) {
            throw new NoTransactionFoundException("User does not own this investment");
        }

        if (investment.getEndTime() != null && investment.getPayout() != null) {
            throw new NoTransactionFoundException("Investment has already been withdrawn.");
        }


        InvestmentOptions option = investment.getInvestmentOption();
        Integer durationInDays = option.getDurationInDays();

        // Calculate the duration of the investment
        LocalDateTime startTime = investment.getCreatedAt();
        LocalDateTime endTime = LocalDateTime.now();
        long daysHeld = Duration.between(startTime, endTime).toDays();

        if (durationInDays != null && daysHeld < durationInDays) {
            throw new InsufficientFundsException("Investment has not yet matured. Remaining days: " + (durationInDays - daysHeld));
        }

        BigDecimal payout = getBigDecimal(investment, daysHeld);

        user.setBalance(user.getBalance().add(payout));
        userRepository.save(user);

        investment.setPayout(payout);
        investment.setEndTime(endTime);
        repository.save(investment);

        return "Successfully withdrawn Investment";
    }

    private static BigDecimal getBigDecimal(Investment investment, long daysHeld) {
        BigDecimal interestRate = investment.getInvestmentOption().getInterestRate();
        BigDecimal amount = investment.getAmount();

        // Calculate payout: interest = principal * (rate/100) * (daysHeld/365)
        BigDecimal yearlyInterest = amount.multiply(interestRate).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
        return yearlyInterest.multiply(BigDecimal.valueOf(daysHeld))
                .divide(BigDecimal.valueOf(365), RoundingMode.HALF_UP)
                .add(amount);
    }

    private Long generateUniqueReferenceId() {
        Random random = new Random();
        Long referenceId;
        do {
            // Generate a 10-digit number between 1000000000 and 9999999999
            referenceId = 1000000000L + random.nextLong(9000000000L);
        } while (repository.findByReferenceId(referenceId).isPresent());

        return referenceId;
    }
}
