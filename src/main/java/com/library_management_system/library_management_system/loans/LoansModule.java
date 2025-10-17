package com.library_management_system.library_management_system.loans;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "loans")
public class LoansModule {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long memberId;
    private Long bookId;

    private LocalDate loanDate;
    private LocalDate dueDate;
    private boolean returned;

}
