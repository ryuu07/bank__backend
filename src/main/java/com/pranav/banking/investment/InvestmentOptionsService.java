package com.pranav.banking.investment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InvestmentOptionsService {

    private final InvestmentOptionsRepository repository;

    public List<InvestmentOptions> getAllInvestmentOptions() {
        return repository.findAll();
    }
}
