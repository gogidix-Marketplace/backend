package com.gogidix.ecommerce.wishlist.domain.port.in;

import com.gogidix.ecommerce.wishlist.application.dto.*;
import java.util.List;

public interface WishlistUseCase {
    WishlistResponse create(CreateWishlistRequest request);
    WishlistResponse update(String id, UpdateWishlistRequest request);
    void delete(String id);
    WishlistResponse getById(String id);
    List<WishlistResponse> getAll();
}
