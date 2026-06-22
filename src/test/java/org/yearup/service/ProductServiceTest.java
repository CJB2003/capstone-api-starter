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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    // No filter search should return all products whether they are featured or not
    @Test
    public void search_withNoFilters_shouldReturnAllProducts() {

        Product featuredProduct = new Product(1, "Featured Product", 25.00, 2, "Newly featured product", "low-fat", 100, true, "image.url");
        Product nonFeaturedProduct = new Product(1, "Non-Featured Product", 5.00, 1, "Newly featured product", "organic", 100, false, "image.url");

        when(productRepository.findAll()).thenReturn(Arrays.asList(featuredProduct, nonFeaturedProduct));

        List<Product> productList = productService.search(null,null,null,null,null);

        assertEquals(2, productList.size());
    }

    // Search with feature filter should only return featured products
    @Test
    public void search_withIsFeaturedFilter_shouldReturnAllFeaturedProducts() {

        Product featuredProduct = new Product(1, "Featured Product", 25.00, 2, "Newly featured product", "low-fat", 100, true, "image.url");
        Product nonFeaturedProduct = new Product(2, "Non-Featured Product", 5.00, 1, "Newly featured product", "organic", 100, false, "image.url");

        when(productRepository.findAll()).thenReturn(Arrays.asList(featuredProduct, nonFeaturedProduct));

        List<Product> productList = productService.search(null,null,null,null,true);

        assertEquals(1, productList.size());
    }

    // Updating the stock value of existing product to test if updated results are returned
    @Test
    public void updateProduct_shouldReturnUpdatedProduct() {

        Product existingProduct = new Product(5, "Old Product", 60.00, 1, "Old product", "organic", 25, true, "image.url" );

        Product updatedProduct = new Product(5, "Updated Product", 60.00, 1, "Updated product", "organic", 100, true, "image.url" );

        when(productRepository.findById(5)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(existingProduct);

        Product result = productService.update(5, updatedProduct);
        assertEquals(100, result.getStock());
    }
}
