package com.pranav.banking.investment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/investments")
@RequiredArgsConstructor
public class InvestmentOptionsController {

    private final InvestmentOptionsService service;

    @GetMapping
    public ResponseEntity<List<InvestmentOptions>> getAllInvestmentOptions() {
        return ResponseEntity.ok(service.getAllInvestmentOptions());
    }

}
