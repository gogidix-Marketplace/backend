package com.gogidix.corporate.website.infrastructure.web.rest;

import com.gogidix.corporate.website.application.dto.ProductDto;
import com.gogidix.corporate.website.domain.model.Language;
import com.gogidix.corporate.website.domain.model.LocalizedContent;
import com.gogidix.corporate.website.domain.model.Product;
import com.gogidix.corporate.website.domain.model.ProductFeature;
import com.gogidix.corporate.website.domain.model.PricingInfo;
import com.gogidix.corporate.website.domain.model.Region;
import com.gogidix.corporate.website.domain.service.ProductDomainService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Products", description = "Product management and serving API")
public class ProductController {

    private final ProductDomainService productDomainService;

    @GetMapping
    @Operation(summary = "Get all published products", description = "Retrieve all published products for a specific region")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved products")
    public ResponseEntity<List<ProductDto>> getPublishedProducts(
            @Parameter(description = "Region filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "NG") Region region,
            @Parameter(description = "Language filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "en") Language language
    ) {
        List<Product> products = productDomainService.getPublishedProductsByRegion(region);
        return ResponseEntity.ok(products.stream()
                .map(product -> mapToDto(product, language))
                .toList());
    }

    @GetMapping("/featured")
    @Operation(summary = "Get featured products", description = "Retrieve featured products")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved featured products")
    public ResponseEntity<List<ProductDto>> getFeaturedProducts(
            @Parameter(description = "Region filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "NG") Region region,
            @Parameter(description = "Language filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "en") Language language
    ) {
        List<Product> products = productDomainService.getFeaturedProducts(region);
        return ResponseEntity.ok(products.stream()
                .map(product -> mapToDto(product, language))
                .toList());
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Get products by category", description = "Retrieve products in a specific category")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved products")
    public ResponseEntity<List<ProductDto>> getProductsByCategory(
            @Parameter(description = "Category name", required = true, in = ParameterIn.PATH)
            @PathVariable String category,
            @Parameter(description = "Region filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "NG") Region region,
            @Parameter(description = "Language filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "en") Language language
    ) {
        List<Product> products = productDomainService.getProductsByCategory(category, region);
        return ResponseEntity.ok(products.stream()
                .map(product -> mapToDto(product, language))
                .toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID", description = "Retrieve a specific product by its ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved product")
    @ApiResponse(responseCode = "404", description = "Product not found")
    public ResponseEntity<ProductDto> getProductById(
            @Parameter(description = "Product ID", required = true, in = ParameterIn.PATH)
            @PathVariable String id,
            @Parameter(description = "Language filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "en") Language language
    ) {
        return productDomainService.getProductById(id)
                .map(product -> ResponseEntity.ok(mapToDto(product, language)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/key/{productKey}")
    @Operation(summary = "Get product by key", description = "Retrieve a specific product by its key")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved product")
    @ApiResponse(responseCode = "404", description = "Product not found")
    public ResponseEntity<ProductDto> getProductByKey(
            @Parameter(description = "Product key", required = true, in = ParameterIn.PATH)
            @PathVariable String productKey,
            @Parameter(description = "Language filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "en") Language language
    ) {
        return productDomainService.getProductByKey(productKey)
                .map(product -> ResponseEntity.ok(mapToDto(product, language)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/slug/{slug}")
    @Operation(summary = "Get product by slug", description = "Retrieve a specific product by its slug")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved product")
    @ApiResponse(responseCode = "404", description = "Product not found")
    public ResponseEntity<ProductDto> getProductBySlug(
            @Parameter(description = "Product slug", required = true, in = ParameterIn.PATH)
            @PathVariable String slug,
            @Parameter(description = "Language filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "en") Language language
    ) {
        return productDomainService.getProductBySlug(slug)
                .map(product -> ResponseEntity.ok(mapToDto(product, language)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    @Operation(summary = "Search products", description = "Search products by keyword")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved search results")
    public ResponseEntity<List<ProductDto>> searchProducts(
            @Parameter(description = "Search keyword", required = true, in = ParameterIn.QUERY)
            @RequestParam String keyword,
            @Parameter(description = "Language filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "en") Language language
    ) {
        List<Product> products = productDomainService.searchProducts(keyword, language);
        return ResponseEntity.ok(products.stream()
                .map(product -> mapToDto(product, language))
                .toList());
    }

    @GetMapping("/tag/{tag}")
    @Operation(summary = "Get products by tag", description = "Retrieve products tagged with a specific tag")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved products")
    public ResponseEntity<List<ProductDto>> getProductsByTag(
            @Parameter(description = "Tag name", required = true, in = ParameterIn.PATH)
            @PathVariable String tag,
            @Parameter(description = "Language filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "en") Language language
    ) {
        List<Product> products = productDomainService.getProductsByTag(tag);
        return ResponseEntity.ok(products.stream()
                .map(product -> mapToDto(product, language))
                .toList());
    }

    @GetMapping("/{id}/related")
    @Operation(summary = "Get related products", description = "Retrieve products related to a specific product")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved related products")
    public ResponseEntity<List<ProductDto>> getRelatedProducts(
            @Parameter(description = "Product ID", required = true, in = ParameterIn.PATH)
            @PathVariable String id,
            @Parameter(description = "Maximum number of products", in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "4") int limit,
            @Parameter(description = "Language filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "en") Language language
    ) {
        List<Product> products = productDomainService.getRelatedProducts(id, limit);
        return ResponseEntity.ok(products.stream()
                .map(product -> mapToDto(product, language))
                .toList());
    }

    private ProductDto mapToDto(Product product, Language language) {
        LocalizedContent content = product.getLocalizedContent().stream()
                .filter(lc -> lc.getLanguage() == language)
                .findFirst()
                .orElse(product.getLocalizedContent().isEmpty() ? null : product.getLocalizedContent().get(0));

        List<ProductDto.ProductFeatureDto> featureDtos = product.getFeatures().stream()
                .map(f -> ProductDto.ProductFeatureDto.builder()
                        .icon(f.getIcon())
                        .title(f.getTitle())
                        .description(f.getDescription())
                        .included(f.isIncluded())
                        .build())
                .collect(Collectors.toList());

        ProductDto.PricingInfoDto pricingDto = null;
        if (product.getPricing() != null) {
            PricingInfo p = product.getPricing();
            pricingDto = ProductDto.PricingInfoDto.builder()
                    .basePrice(p.getBasePrice())
                    .currency(p.getCurrency())
                    .billingCycle(p.getBillingCycle())
                    .displayPricing(p.isDisplayPricing())
                    .startingFromText(p.getStartingFromText())
                    .build();
        }

        return ProductDto.builder()
                .id(product.getId())
                .productKey(product.getProductKey())
                .slug(product.getSlug())
                .localizedContent(content)
                .language(language)
                .productType(product.getProductType())
                .category(product.getCategory())
                .features(featureDtos)
                .availableRegions(product.getAvailableRegions())
                .imageUrl(product.getImageUrl())
                .imageAlt(product.getImageAlt())
                .gallery(product.getGallery())
                .pricing(pricingDto)
                .requiresContact(product.isRequiresContact())
                .ctaText(product.getCtaText())
                .ctaLink(product.getCtaLink())
                .status(product.getStatus())
                .launchDate(product.getLaunchDate())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .tags(product.getTags())
                .sortOrder(product.getSortOrder())
                .featured(product.isFeatured())
                .build();
    }
}
