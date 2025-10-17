package com.library_management_system.library_management_system;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.modulith.core.ApplicationModules;


@SpringBootTest
class LibraryManagementSystemApplicationTests {

    @Test
    void contextLoads() {
        // Tes ini hanya untuk memastikan seluruh aplikasi dapat memulai tanpa error.
    }

    @Test
    void verifyModuleStructure() {
        // Tes ini memverifikasi bahwa tidak ada dependensi ilegal antar modul.
        ApplicationModules.of(LibraryManagementSystemApplication.class).verify();
    }

}
