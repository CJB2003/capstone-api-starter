package org.yearup.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.yearup.models.Product;
import org.yearup.service.CategoryService;
import org.yearup.service.ProductService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductsController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private CategoryService categoryService;

    @Test
    void search_withNoFilters_returnsAllProducts_includingNonFeatured() throws Exception {

        Product featuredProduct = new Product(1, "Featured Widget", 19.99, 1,
                "A featured product", "Red", 10, true, "featured.jpg");

        Product nonFeaturedProduct = new Product(2, "Regular Widget", 9.99, 1,
                "A non-featured product", "Blue", 20, false, "regular.jpg");

        List<Product> products = Arrays.asList(featuredProduct, nonFeaturedProduct);

        when(productService.search(isNull(), isNull(), isNull(), isNull(), isNull()))
                .thenReturn(products);

        mockMvc.perform(get("/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].productId").value(1))
                .andExpect(jsonPath("$[0].featured").value(true))
                .andExpect(jsonPath("$[1].productId").value(2))
                .andExpect(jsonPath("$[1].featured").value(false));
    }
}
