package com.gogidix.ecommerce.coupon.domain.port.in;

import com.gogidix.ecommerce.coupon.application.dto.*;
import java.util.List;

public interface CouponUseCase {
    CouponResponse create(CreateCouponRequest request);
    CouponResponse update(String id, UpdateCouponRequest request);
    void delete(String id);
    CouponResponse getById(String id);
    List<CouponResponse> getAll();
}
