package com.library_management_system.library_management_system.catalog;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CatalogService {

    public final CatalogRepository repository; // Akses ke Db

    // Mengurangi Stock Buku dari Catalog ketika di pinjam
    public void reduceStock(Long id){
        var book = repository.findById(id).orElseThrow(); // Ambil Book Berdasarkan Id
        if (book.getAvailableCopies() <= 0)
            throw new IllegalStateException("# Stock Habis!");
        book.setAvailableCopies(book.getAvailableCopies() - 1); // kurangi stock
        repository.save(book);
    }

    // Menambahkan Stock Buku ketika di kembalikan
    public void increaseStock(Long Id){
        var book = repository.findById(Id).orElseThrow(); // Ambil Book Berdasarkan Id
        book.setAvailableCopies(book.getAvailableCopies() + 1); // Tambahkan Stock
        repository.save(book);
    }

}
