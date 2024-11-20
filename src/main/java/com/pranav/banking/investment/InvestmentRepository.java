package com.pranav.banking.investment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InvestmentRepository extends JpaRepository<Investment, Long> {
    List<Investment> findByUserId(Long userId);
    List<Investment> findByUser_Username(String username);
    Optional<Investment> findByReferenceId(Long referenceId);
}
