package org.yearup.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.yearup.models.Product;
import org.yearup.repository.ProductRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    public void search_withNoFilters_shouldReturnAllProducts() {
        Product featuredProduct = new Product(1, "Featured Product", 25.00, 2, "Newly featured product", "low-fat", 100, true, "image.url");
        Product nonFeaturedProduct = new Product(1, "Non-Featured Product", 5.00, 1, "Newly featured product", "organic", 100, false, "image.url");

        when(productRepository.findAll()).thenReturn(Arrays.asList(featuredProduct, nonFeaturedProduct));

        List<Product> productList = productService.search(null,null,null,null,null);

        assertEquals(2, productList.size());
    }
}
