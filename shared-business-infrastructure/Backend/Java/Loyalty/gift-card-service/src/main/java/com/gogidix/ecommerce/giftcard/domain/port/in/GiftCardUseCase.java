package com.gogidix.ecommerce.giftcard.domain.port.in;

import com.gogidix.ecommerce.giftcard.application.dto.*;
import java.util.List;

public interface GiftCardUseCase {
    GiftCardResponse create(CreateGiftCardRequest request);
    GiftCardResponse update(String id, UpdateGiftCardRequest request);
    void delete(String id);
    GiftCardResponse getById(String id);
    List<GiftCardResponse> getAll();
}
