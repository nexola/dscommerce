package com.devsuperior.dscommerce.services;

import com.devsuperior.dscommerce.dto.ProductDTO;
import com.devsuperior.dscommerce.dto.ProductMinDTO;
import com.devsuperior.dscommerce.entities.Product;
import com.devsuperior.dscommerce.factories.ProductFactory;
import com.devsuperior.dscommerce.repositories.ProductRepository;
import com.devsuperior.dscommerce.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductService service;

    private Long existingId;
    private Long nonExistingId;
    private Product product;
    private ProductDTO productDTO;
    private String productName;
    private PageImpl<Product> page;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 2L;
        product = ProductFactory.createProduct();
        productDTO = new ProductDTO(ProductFactory.createProduct());
        productName = "Computer";
        page = new PageImpl<>(List.of(product));

        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));
        Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        Mockito.when(repository.searchByName(ArgumentMatchers.anyString(), (Pageable) ArgumentMatchers.any())).thenReturn(page);
        Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);
    }

    @Test
    public void findByIdShouldReturnProductDTOWhenIdExists() {
        ProductDTO result = service.findById(existingId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), existingId);
        Assertions.assertEquals(result.getName(), product.getName());
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(nonExistingId);
        });
    }

    @Test
    public void findAllShouldReturnPagedProductMinDTO() {
        Pageable pageable = PageRequest.of(0, 12);
        String name = "Computer";

        Page<ProductMinDTO> result = service.findAll(name, pageable);

        Assertions.assertNotNull(result);
    }

    @Test
    public void insertShouldReturnProductDTO() {
        ProductDTO result = service.insert(productDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getName(), productDTO.getName());
    }
}
