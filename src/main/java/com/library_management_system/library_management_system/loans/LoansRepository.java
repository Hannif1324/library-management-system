package com.library_management_system.library_management_system.loans;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LoansRepository extends JpaRepository<LoansModule, Long> {
}
