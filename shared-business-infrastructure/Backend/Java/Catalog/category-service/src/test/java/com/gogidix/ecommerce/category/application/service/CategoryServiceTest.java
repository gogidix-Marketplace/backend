package com.gogidix.ecommerce.category.application.service;

import com.gogidix.ecommerce.category.application.dto.*;
import com.gogidix.ecommerce.category.application.mapper.CategoryMapper;
import com.gogidix.ecommerce.category.domain.model.Category;
import com.gogidix.ecommerce.category.domain.repository.CategoryRepository;
import com.gogidix.ecommerce.category.shared.requestcontext.RequestContext;
import com.gogidix.ecommerce.category.shared.requestcontext.RequestContextHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    @Mock private CategoryRepository repository;
    @Mock private CategoryMapper mapper;
    @InjectMocks private CategoryService service;
    @BeforeEach void setUp() { RequestContextHolder.set(RequestContext.builder().tenantId("t1").build()); }
    @AfterEach void tearDown() { RequestContextHolder.clear(); }

    private Category buildCat() {
        Category c = new Category(); c.setId("id1"); c.setTenantId("t1"); c.setCategoryCode("CAT1");
        c.setLevel(0); c.setPath("CAT1"); c.setIsActive(true);
        return c;
    }
    private CategoryResponse buildResponse() { return mock(CategoryResponse.class); }

    @Test void getAllCategories() {
        when(repository.findByTenantIdAndIsActiveOrderByDisplayOrder("t1",true)).thenReturn(List.of(buildCat()));
        when(mapper.toCategoryResponse(any())).thenReturn(buildResponse());
        assertThat(service.getAllCategories()).hasSize(1);
    }
    @Test void getRootCategories() {
        when(repository.findByTenantIdAndParentIdIsNull("t1")).thenReturn(List.of(buildCat()));
        when(mapper.toCategoryResponse(any())).thenReturn(buildResponse());
        assertThat(service.getRootCategories()).hasSize(1);
    }
    @Test void getSubCategories() {
        when(repository.findByTenantIdAndParentIdAndIsActiveOrderByDisplayOrder("t1","pid1",true)).thenReturn(List.of(buildCat()));
        when(mapper.toCategoryResponse(any())).thenReturn(buildResponse());
        assertThat(service.getSubCategories("pid1")).hasSize(1);
    }
    @Test void getCategoryById_found() {
        when(repository.findById("id1")).thenReturn(Optional.of(buildCat()));
        when(mapper.toCategoryResponse(any())).thenReturn(buildResponse());
        assertThat(service.getCategoryById("id1")).isNotNull();
    }
    @Test void getCategoryById_notFound() {
        when(repository.findById("x")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getCategoryById("x")).isInstanceOf(IllegalArgumentException.class);
    }
    @Test void getCategoryByCode_found() {
        when(repository.findByTenantIdAndCategoryCode("t1","CAT1")).thenReturn(buildCat());
        when(mapper.toCategoryResponse(any())).thenReturn(buildResponse());
        assertThat(service.getCategoryByCode("CAT1")).isNotNull();
    }
    @Test void getCategoryByCode_notFound() {
        when(repository.findByTenantIdAndCategoryCode("t1","x")).thenReturn(null);
        assertThatThrownBy(() -> service.getCategoryByCode("x")).isInstanceOf(IllegalArgumentException.class);
    }
    @Test void getCategoriesByLevel() {
        when(repository.findByTenantIdAndLevelAndIsActive("t1",0,true)).thenReturn(List.of(buildCat()));
        when(mapper.toCategoryResponse(any())).thenReturn(buildResponse());
        assertThat(service.getCategoriesByLevel(0)).hasSize(1);
    }
    @Test void getCategoryTree() {
        Category root = buildCat();
        when(repository.findByTenantIdAndParentIdIsNull("t1")).thenReturn(List.of(root));
        when(repository.findByTenantIdAndParentIdAndIsActiveOrderByDisplayOrder("t1","id1",true)).thenReturn(List.of());
        when(mapper.toCategoryResponseList(anyList())).thenReturn(List.of(buildResponse()));
        assertThat(service.getCategoryTree()).hasSize(1);
    }
    @Test void createCategory_root() {
        when(repository.existsByTenantIdAndCategoryCode("t1","NEW")).thenReturn(false);
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(mapper.toCategory(any(CreateCategoryRequest.class))).thenReturn(buildCat());
        when(mapper.toCategoryResponse(any())).thenReturn(buildResponse());
        CreateCategoryRequest req = new CreateCategoryRequest("NEW","Test","d",null,0,null,null,null,null,true,null,null,null);
        assertThat(service.createCategory(req)).isNotNull();
    }
    @Test void createCategory_withParent() {
        Category parent = buildCat(); parent.setLevel(0); parent.setPath("CAT1"); parent.setIsLeaf(true);
        Category child = buildCat(); child.setLevel(null);
        when(repository.existsByTenantIdAndCategoryCode("t1","CHILD")).thenReturn(false);
        when(repository.findById("pid1")).thenReturn(Optional.of(parent));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(mapper.toCategory(any(CreateCategoryRequest.class))).thenReturn(child);
        when(mapper.toCategoryResponse(any())).thenReturn(buildResponse());
        CreateCategoryRequest req = new CreateCategoryRequest("CHILD","Test","d","pid1",1,null,null,null,null,true,null,null,null);
        assertThat(service.createCategory(req)).isNotNull();
        verify(repository, atLeastOnce()).save(argThat(c -> c.getLevel() != null && c.getLevel() == 1));
    }
    @Test void createCategory_duplicateCode() {
        when(repository.existsByTenantIdAndCategoryCode("t1","DUP")).thenReturn(true);
        CreateCategoryRequest req = new CreateCategoryRequest("DUP","Test","d",null,0,null,null,null,null,true,null,null,null);
        assertThatThrownBy(() -> service.createCategory(req)).isInstanceOf(IllegalArgumentException.class);
    }
    @Test void updateCategory() {
        when(repository.findById("id1")).thenReturn(Optional.of(buildCat()));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(mapper.toCategoryResponse(any())).thenReturn(buildResponse());
        UpdateCategoryRequest req = new UpdateCategoryRequest("Updated",null,null,null,null,null,null,null,true,null,null);
        assertThat(service.updateCategory("id1",req)).isNotNull();
    }
    @Test void deleteCategory() {
        when(repository.findById("id1")).thenReturn(Optional.of(buildCat()));
        when(repository.findByTenantIdAndParentId("t1","id1")).thenReturn(List.of());
        service.deleteCategory("id1");
        verify(repository).deleteById("id1");
    }
    @Test void deleteCategory_withChildren() {
        when(repository.findById("id1")).thenReturn(Optional.of(buildCat()));
        when(repository.findByTenantIdAndParentId("t1","id1")).thenReturn(List.of(buildCat()));
        assertThatThrownBy(() -> service.deleteCategory("id1")).isInstanceOf(IllegalStateException.class);
    }
}