package com.library_management_system.library_management_system.Controller;

import com.library_management_system.library_management_system.loans.LoansModule;
import com.library_management_system.library_management_system.loans.LoansRepository;
import com.library_management_system.library_management_system.loans.LoansService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
@Tag(name = "Loans Management", description = "API untuk mengelola peminjaman dan pengembalian buku")
public class LoansController {

    private final LoansService loansService;
    private final LoansRepository loansRepository;

    @Operation(
            summary = "Pinjam buku",
            description = "Memproses peminjaman buku oleh anggota. Otomatis mengurangi stock buku dan membuat catatan peminjaman dengan due date 7 hari."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Buku berhasil dipinjam"),
            @ApiResponse(responseCode = "400", description = "Stock habis atau data tidak valid")
    })
    @PostMapping("/borrow")
    public ResponseEntity<String> borrowBook(
            @Parameter(description = "Data peminjaman buku", required = true)
            @RequestBody BorrowRequest request) {
        try {
            loansService.borrowBook(request.getMemberId(), request.getBookId(), request.getBookId());
            return ResponseEntity.ok("Buku berhasil dipinjam oleh member ID: " + request.getMemberId());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Gagal meminjam buku: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Kembalikan buku",
            description = "Memproses pengembalian buku. Otomatis menambah stock buku dan mengirim notifikasi pengembalian."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Buku berhasil dikembalikan"),
            @ApiResponse(responseCode = "400", description = "Loan tidak ditemukan atau sudah dikembalikan")
    })
    @PutMapping("/return/{loanId}")
    public ResponseEntity<String> returnBook(
            @Parameter(description = "ID peminjaman yang akan dikembalikan", required = true)
            @PathVariable Long loanId) {
        try {
            loansService.returnBook(loanId);
            return ResponseEntity.ok("Buku berhasil dikembalikan untuk loan ID: " + loanId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Gagal mengembalikan buku: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Lihat semua peminjaman",
            description = "Menampilkan daftar semua catatan peminjaman buku"
    )
    @ApiResponse(responseCode = "200", description = "Daftar peminjaman berhasil diambil")
    @GetMapping
    public ResponseEntity<List<LoansModule>> getAllLoans() {
        return ResponseEntity.ok(loansRepository.findAll());
    }

    @Operation(
            summary = "Lihat detail peminjaman",
            description = "Menampilkan detail peminjaman berdasarkan ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Detail peminjaman ditemukan",
                    content = @Content(schema = @Schema(implementation = LoansModule.class))
            ),
            @ApiResponse(responseCode = "404", description = "Peminjaman tidak ditemukan")
    })
    @GetMapping("/{id}")
    public ResponseEntity<LoansModule> getLoanById(
            @Parameter(description = "ID peminjaman yang ingin dilihat", required = true)
            @PathVariable Long id) {
        return loansRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Lihat peminjaman per anggota",
            description = "Menampilkan semua peminjaman yang dilakukan oleh anggota tertentu"
    )
    @ApiResponse(responseCode = "200", description = "Daftar peminjaman anggota berhasil diambil")
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<LoansModule>> getLoansByMemberId(
            @Parameter(description = "ID anggota", required = true)
            @PathVariable Long memberId) {
        List<LoansModule> loans = loansRepository.findAll().stream()
                .filter(loan -> loan.getMemberId().equals(memberId))
                .toList();
        return ResponseEntity.ok(loans);
    }

    @Operation(
            summary = "Lihat peminjaman aktif",
            description = "Menampilkan semua peminjaman yang belum dikembalikan"
    )
    @ApiResponse(responseCode = "200", description = "Daftar peminjaman aktif berhasil diambil")
    @GetMapping("/active")
    public ResponseEntity<List<LoansModule>> getActiveLoans() {
        List<LoansModule> activeLoans = loansRepository.findAll().stream()
                .filter(loan -> !loan.isReturned())
                .toList();
        return ResponseEntity.ok(activeLoans);
    }

    @Schema(description = "Request untuk meminjam buku")
    public static class BorrowRequest {
        @Schema(description = "ID anggota yang meminjam", example = "1", required = true)
        private Long memberId;

        @Schema(description = "ID buku yang dipinjam", example = "1", required = true)
        private Long bookId;

        public BorrowRequest() {}

        public BorrowRequest(Long memberId, Long bookId) {
            this.memberId = memberId;
            this.bookId = bookId;
        }

        public Long getMemberId() { return memberId; }
        public void setMemberId(Long memberId) { this.memberId = memberId; }
        public Long getBookId() { return bookId; }
        public void setBookId(Long bookId) { this.bookId = bookId; }
    }
}