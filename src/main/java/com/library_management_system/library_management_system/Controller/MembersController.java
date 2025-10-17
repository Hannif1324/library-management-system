package com.library_management_system.library_management_system.Controller;

import com.library_management_system.library_management_system.members.MembersModule;
import com.library_management_system.library_management_system.members.MembersRepository;
import com.library_management_system.library_management_system.members.MembersService;
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
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Tag(name = "Members Management", description = "API untuk mengelola data anggota perpustakaan")
public class MembersController {

    private final MembersService membersService;
    private final MembersRepository membersRepository;

    @Operation(
            summary = "Registrasi anggota baru",
            description = "Mendaftarkan anggota baru ke sistem perpustakaan dengan nama dan email"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Anggota berhasil didaftarkan",
                    content = @Content(schema = @Schema(implementation = MembersModule.class))
            ),
            @ApiResponse(responseCode = "400", description = "Data registrasi tidak valid")
    })
    @PostMapping("/register")
    public ResponseEntity<MembersModule> registerMember(
            @Parameter(description = "Data registrasi anggota baru", required = true)
            @RequestBody RegisterRequest request) {
        try {
            MembersModule newMember = membersService.register(request.getName(), request.getEmail());
            return ResponseEntity.ok(newMember);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @Operation(
            summary = "Lihat detail anggota",
            description = "Menampilkan detail anggota berdasarkan ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Detail anggota ditemukan",
                    content = @Content(schema = @Schema(implementation = MembersModule.class))
            ),
            @ApiResponse(responseCode = "404", description = "Anggota tidak ditemukan")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MembersModule> getMemberById(
            @Parameter(description = "ID anggota yang ingin dilihat", required = true)
            @PathVariable Long id) {
        try {
            MembersModule member = membersService.findByID(id);
            return ResponseEntity.ok(member);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Lihat semua anggota",
            description = "Menampilkan daftar semua anggota perpustakaan"
    )
    @ApiResponse(responseCode = "200", description = "Daftar anggota berhasil diambil")
    @GetMapping
    public ResponseEntity<List<MembersModule>> getAllMembers() {
        return ResponseEntity.ok(membersRepository.findAll());
    }

    @Operation(
            summary = "Update data anggota",
            description = "Mengubah data anggota (nama dan email) berdasarkan ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Data anggota berhasil diupdate",
                    content = @Content(schema = @Schema(implementation = MembersModule.class))
            ),
            @ApiResponse(responseCode = "404", description = "Anggota tidak ditemukan")
    })
    @PutMapping("/{id}")
    public ResponseEntity<MembersModule> updateMember(
            @Parameter(description = "ID anggota yang akan diupdate", required = true)
            @PathVariable Long id,
            @Parameter(description = "Data baru untuk anggota", required = true)
            @RequestBody RegisterRequest request) {
        try {
            MembersModule member = membersService.findByID(id);
            member.setName(request.getName());
            member.setEmail(request.getEmail());
            MembersModule updatedMember = membersRepository.save(member);
            return ResponseEntity.ok(updatedMember);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Hapus anggota",
            description = "Menghapus anggota dari sistem perpustakaan berdasarkan ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Anggota berhasil dihapus"),
            @ApiResponse(responseCode = "404", description = "Anggota tidak ditemukan")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMember(
            @Parameter(description = "ID anggota yang akan dihapus", required = true)
            @PathVariable Long id) {
        try {
            membersService.findByID(id);
            membersRepository.deleteById(id);
            return ResponseEntity.ok("Member dengan ID " + id + " berhasil dihapus");
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Schema(description = "Request untuk registrasi atau update anggota")
    public static class RegisterRequest {
        @Schema(description = "Nama lengkap anggota", example = "John Doe", required = true)
        private String name;

        @Schema(description = "Email anggota", example = "john.doe@example.com", required = true)
        private String email;

        public RegisterRequest() {}

        public RegisterRequest(String name, String email) {
            this.name = name;
            this.email = email;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }
}