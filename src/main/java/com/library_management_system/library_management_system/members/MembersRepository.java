package com.library_management_system.library_management_system.members;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MembersRepository extends JpaRepository<MembersModule, Long> {
}
