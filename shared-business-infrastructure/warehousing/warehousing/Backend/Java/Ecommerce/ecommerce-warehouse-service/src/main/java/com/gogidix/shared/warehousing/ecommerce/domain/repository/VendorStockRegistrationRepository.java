package com.gogidix.shared.warehousing.ecommerce.domain.repository;

import com.gogidix.shared.warehousing.ecommerce.domain.entity.VendorStockRegistration;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendorStockRegistrationRepository extends MongoRepository<VendorStockRegistration, String> {
    List<VendorStockRegistration> findByVendorId(String vendorId);
    List<VendorStockRegistration> findByWarehouseId(String warehouseId);
    List<VendorStockRegistration> findByVendorIdAndWarehouseId(String vendorId, String warehouseId);
}
