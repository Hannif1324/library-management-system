package com.library_management_system.library_management_system.notifications;

import com.library_management_system.library_management_system.loans.LoansEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationListener {
    private final NotificationService service;

    @ApplicationModuleListener
    void on(LoansEvent event) {
        // Panggil service notifikasi
        service.sendReturnNotification(event.id());
    }
}
