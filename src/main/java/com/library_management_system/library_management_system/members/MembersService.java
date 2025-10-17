package com.library_management_system.library_management_system.members;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MembersService {

    private final MembersRepository repo;

    // Registrasi Anggota Baru
    public MembersModule register(String nama, String email){
        return repo.save(new MembersModule (null, nama, email)); // Simpan data anggota baru
    }

    // Ambil Data anggota berdasarkan id
    public MembersModule findByID(Long id){
        return repo.findById(id).orElseThrow();
    }
}
