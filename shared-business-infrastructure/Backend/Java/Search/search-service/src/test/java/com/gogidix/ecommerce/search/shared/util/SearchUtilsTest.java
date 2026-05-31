package com.gogidix.ecommerce.search.shared.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("SearchUtils Tests")
class SearchUtilsTest {

    @Test
    @DisplayName("Should generate non-null ID")
    void shouldGenerateId() {
        assertThat(SearchUtils.generateId()).isNotNull().isNotEmpty();
    }
}
