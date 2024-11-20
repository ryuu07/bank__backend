package com.pranav.banking.investment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2/investments")
public class InvestmentController {

    private final InvestmentService investmentService;

    @GetMapping("/user")
    public ResponseEntity<List<InvestmentListDTO>> getUserInvestments(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(investmentService.getInvestmentsByUserId(userDetails.getUsername()));
    }

    @PostMapping
    public ResponseEntity<InvestmentResponseDTO>createInvestment(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody InvestmentRequestBody request
    ){
        return ResponseEntity.ok(investmentService.createInvestment(userDetails.getUsername(),request));
    }

    @PutMapping(path = "/withdraw")
    public ResponseEntity<String> withdrawInvestment(
            @AuthenticationPrincipal UserDetails userdetails,
            @RequestBody InvestmentWithdrawDTO request
    ){
        return ResponseEntity.ok(investmentService.withdrawInvestment(userdetails.getUsername(), request));
    }


}
