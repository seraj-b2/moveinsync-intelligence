package com.moveinsync.intelligence.repository;

import com.moveinsync.intelligence.entity.BillingDiscrepancyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillingDiscrepancyRepository extends JpaRepository<BillingDiscrepancyEntity, Long> {
    List<BillingDiscrepancyEntity> findByVendorNameIgnoreCase(String vendorName);
    boolean existsByTripIdAndVendorName(String tripId, String vendorName);
}
