package com.gogidix.ecommerce.category.interfaces.rest;

import com.gogidix.ecommerce.category.application.dto.*;
import com.gogidix.ecommerce.category.application.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {
    @Mock private CategoryService service;
    @InjectMocks private CategoryController controller;

    private CategoryResponse buildResponse() { return mock(CategoryResponse.class); }

    @Test void getAllCategories() {
        when(service.getAllCategories()).thenReturn(List.of(buildResponse()));
        assertThat(controller.getAllCategories().getBody()).hasSize(1);
    }
    @Test void getRootCategories() {
        when(service.getRootCategories()).thenReturn(List.of(buildResponse()));
        assertThat(controller.getRootCategories().getBody()).hasSize(1);
    }
    @Test void getCategoryTree() {
        when(service.getCategoryTree()).thenReturn(List.of(buildResponse()));
        assertThat(controller.getCategoryTree().getBody()).hasSize(1);
    }
    @Test void getCategory() {
        when(service.getCategoryById("id1")).thenReturn(buildResponse());
        assertThat(controller.getCategory("id1").getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test void getCategoryByCode() {
        when(service.getCategoryByCode("C1")).thenReturn(buildResponse());
        assertThat(controller.getCategoryByCode("C1").getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test void getSubCategories() {
        when(service.getSubCategories("pid1")).thenReturn(List.of(buildResponse()));
        assertThat(controller.getSubCategories("pid1").getBody()).hasSize(1);
    }
    @Test void getCategoriesByLevel() {
        when(service.getCategoriesByLevel(0)).thenReturn(List.of(buildResponse()));
        assertThat(controller.getCategoriesByLevel(0).getBody()).hasSize(1);
    }
    @Test void createCategory() {
        when(service.createCategory(any())).thenReturn(buildResponse());
        CreateCategoryRequest req = new CreateCategoryRequest("NEW","T","d",null,0,null,null,null,null,true,null,null,null);
        assertThat(controller.createCategory(req).getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test void updateCategory() {
        when(service.updateCategory(eq("id1"),any())).thenReturn(buildResponse());
        UpdateCategoryRequest req = new UpdateCategoryRequest("U",null,null,null,null,null,null,null,true,null,null);
        assertThat(controller.updateCategory("id1",req).getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test void deleteCategory() {
        assertThat(controller.deleteCategory("id1").getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(service).deleteCategory("id1");
    }
}