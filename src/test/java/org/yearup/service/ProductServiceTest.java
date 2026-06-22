package org.yearup.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProductService.class)
public class ProductServiceTest {

    @Autowired
    private MockMvc mockMvc;

}
