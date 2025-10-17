package com.library_management_system.library_management_system.catalog;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CatalogRepository extends JpaRepository<CatalogModule, Long> {
}
