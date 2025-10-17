package com.library_management_system.library_management_system.loans;

import com.library_management_system.library_management_system.catalog.CatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class LoansService {

    private final LoansRepository repo;
    private final CatalogService service;

    private final ApplicationEventPublisher event;

    // Proses peminjaman buku
    public void borrowBook(Long memberId, Long bookId, Long id) {
        service.reduceStock(id); // kurangi stok di modul catalog

        // Buat catatan peminjaman baru
        var loan = new LoansModule(null, memberId, bookId, LocalDate.now(),
                LocalDate.now().plusDays(7), false);


        repo.save(loan); // simpan ke database
        System.out.println("📦 Buku berhasil dipinjam oleh memberId " + memberId);
    }

    // Proses pengembalian buku
    public void returnBook(Long id) {
        var loan = repo.findById(id).orElseThrow();

        if (loan.isReturned()) return; // jika sudah dikembalikan, abaikan

        loan.setReturned(true);
        repo.save(loan); // update status di DB

        service.increaseStock(loan.getBookId()); // kembalikan stok

        // Publikasikan event ke modul lain (misal: notifikasi)
        event.publishEvent(new LoansEvent(id));

        System.out.println("📦 Buku berhasil dikembalikan dengan loanId " + id);
    }
}
