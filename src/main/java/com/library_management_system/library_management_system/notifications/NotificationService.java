package com.library_management_system.library_management_system.notifications;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    // Simulasi pengiriman pesan (di dunia nyata bisa pakai email/WA API)
    public void sendReturnNotification(Long id) {
        System.out.println("📢 Notifikasi: Buku dengan Loan ID " + id + " telah dikembalikan!");
    }
}
