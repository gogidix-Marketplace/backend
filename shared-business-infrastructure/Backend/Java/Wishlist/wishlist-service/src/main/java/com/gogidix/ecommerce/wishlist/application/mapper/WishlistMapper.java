package com.gogidix.ecommerce.wishlist.application.mapper;

import com.gogidix.ecommerce.wishlist.application.dto.*;
import com.gogidix.ecommerce.wishlist.domain.model.Wishlist;
import org.springframework.stereotype.Component;

@Component
public class WishlistMapper {

    public WishlistResponse toResponse(Wishlist entity) {
        if (entity == null) return null;
        return new WishlistResponse(
            entity.getId(),
            entity.getName(),
            entity.getDescription(),
            entity.getType(),
            entity.getWishlistName(),
            entity.getItemCount(),
            entity.getIsPublic(),
            entity.getIsActive(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    public Wishlist toEntity(CreateWishlistRequest request) {
        Wishlist entity = new Wishlist();
        entity.setName(request.name());
        entity.setDescription(request.description());
        entity.setType(request.type());
        entity.setWishlistName(request.wishlistName());
        entity.setItemCount(request.itemCount());
        entity.setIsPublic(request.isPublic());
        entity.setIsActive(true);
        return entity;
    }

    public void updateFromRequest(Wishlist entity, UpdateWishlistRequest request) {
        if (request.name() != null) entity.setName(request.name());
        if (request.description() != null) entity.setDescription(request.description());
        if (request.type() != null) entity.setType(request.type());
        if (request.wishlistName() != null) entity.setWishlistName(request.wishlistName());
        if (request.itemCount() != null) entity.setItemCount(request.itemCount());
        if (request.isPublic() != null) entity.setIsPublic(request.isPublic());
        if (request.isActive() != null) entity.setIsActive(request.isActive());
    }
}
