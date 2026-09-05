package com.moveinsync.intelligence.repository;

import com.moveinsync.intelligence.entity.ComplianceAlertEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplianceAlertRepository extends JpaRepository<ComplianceAlertEntity, Long> {
    List<ComplianceAlertEntity> findByVendorNameIgnoreCase(String vendorName);
    boolean existsByAlertId(String alertId);
}
