package com.library_management_system.library_management_system.Controller;

import com.library_management_system.library_management_system.catalog.CatalogModule;
import com.library_management_system.library_management_system.catalog.CatalogRepository;
import com.library_management_system.library_management_system.catalog.CatalogService;
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
@RequestMapping("/api/catalog")
@RequiredArgsConstructor
@Tag(name = "Catalog Management", description = "API untuk mengelola katalog buku perpustakaan")
public class CatalogController {

    private final CatalogService catalogService;
    private final CatalogRepository catalogRepository;

    @Operation(
            summary = "Kurangi stock buku",
            description = "Mengurangi jumlah stock buku ketika dipinjam oleh anggota. Stock akan berkurang 1."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock berhasil dikurangi"),
            @ApiResponse(responseCode = "400", description = "Stock habis atau buku tidak ditemukan")
    })
    @PutMapping("/reduce-stock/{id}")
    public ResponseEntity<String> reduceStock(
            @Parameter(description = "ID buku yang akan dikurangi stocknya", required = true)
            @PathVariable Long id) {
        try {
            catalogService.reduceStock(id);
            return ResponseEntity.ok("Stock buku berhasil dikurangi untuk ID: " + id);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Buku dengan ID " + id + " tidak ditemukan");
        }
    }

    @Operation(
            summary = "Tambah stock buku",
            description = "Menambah jumlah stock buku ketika dikembalikan oleh anggota. Stock akan bertambah 1."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock berhasil ditambah"),
            @ApiResponse(responseCode = "400", description = "Buku tidak ditemukan")
    })
    @PutMapping("/increase-stock/{id}")
    public ResponseEntity<String> increaseStock(
            @Parameter(description = "ID buku yang akan ditambah stocknya", required = true)
            @PathVariable Long id) {
        try {
            catalogService.increaseStock(id);
            return ResponseEntity.ok("Stock buku berhasil ditambah untuk ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Buku dengan ID " + id + " tidak ditemukan");
        }
    }

    @Operation(
            summary = "Lihat semua buku",
            description = "Menampilkan daftar semua buku yang ada di katalog perpustakaan"
    )
    @ApiResponse(responseCode = "200", description = "Daftar buku berhasil diambil")
    @GetMapping
    public ResponseEntity<List<CatalogModule>> getAllBooks() {
        return ResponseEntity.ok(catalogRepository.findAll());
    }

    @Operation(
            summary = "Lihat detail buku",
            description = "Menampilkan detail lengkap buku berdasarkan ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Detail buku ditemukan"),
            @ApiResponse(responseCode = "404", description = "Buku tidak ditemukan")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CatalogModule> getBookById(
            @Parameter(description = "ID buku yang ingin dilihat", required = true)
            @PathVariable Long id) {
        return catalogRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Tambah buku baru",
            description = "Menambahkan buku baru ke dalam katalog perpustakaan"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Buku berhasil ditambahkan",
                    content = @Content(schema = @Schema(implementation = CatalogModule.class))
            ),
            @ApiResponse(responseCode = "400", description = "Data tidak valid")
    })
    @PostMapping
    public ResponseEntity<CatalogModule> addBook(
            @Parameter(description = "Data buku yang akan ditambahkan", required = true)
            @RequestBody CatalogModule book) {
        CatalogModule savedBook = catalogRepository.save(book);
        return ResponseEntity.ok(savedBook);
    }
}
