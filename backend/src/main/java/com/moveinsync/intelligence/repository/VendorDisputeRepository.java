package com.moveinsync.intelligence.repository;

import com.moveinsync.intelligence.entity.VendorDisputeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendorDisputeRepository extends JpaRepository<VendorDisputeEntity, String> {
    List<VendorDisputeEntity> findByVendorNameIgnoreCase(String vendorName);
    List<VendorDisputeEntity> findByStatusIgnoreCase(String status);
    List<VendorDisputeEntity> findByVendorNameIgnoreCaseAndStatusIgnoreCase(String vendorName, String status);
}
