package com.pranav.banking.transaction;

import com.pranav.banking.config.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping( path = "/api/v1/banking")
public class BankingController {

    private final BankingService bankingService;
    private final JWTService jwtService;

    @PostMapping(path = "/credit")
    public ResponseEntity<CreditResponseDTO> credit(
            @RequestBody CreditRequestDTO request
    ){
        return ResponseEntity.ok(bankingService.credit(request));
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransferResponseDTO> transfer(
            @RequestBody TransferRequestDTO request) {

        return ResponseEntity.ok(bankingService.transfer(request));
    }
    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionDTO>> getTransactions() {
        // Extract the authentication object from SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build(); // Return 401 Unauthorized if no authenticated user
        }

        String username = authentication.getName();

        return ResponseEntity.ok(bankingService.getTransactionHistory(username));
    }
}
