package com.bookstoremanagement.catalog_service.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(
        properties = {
                "spring.tet.database.replace=none",
                "spring.datasource.url=jdbc:tc:postgresl:1-alpine:///db",
        })
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void shouldGetAllProducts(){
        List<ProductEntity> product = productRepository.findAll();
        assertThat(product).hasSize(30);
    }

    @Test
    void shouldGetProductByCode() {
        ProductEntity product = productRepository.findByCode("P100").orElseThrow();
        assertThat(product.getCode()).isEqualTo("P100");
        assertThat(product.getName()).isEqualTo("The Hunger Games");
        assertThat(product.getDescription()).isEqualTo("Winning will make you famous. Losing means certain death...");
        assertThat(product.getPrice()).isEqualTo(new BigDecimal("34.0"));
    }

    @Test
    void shouldReturnEmptyWhenProductCodeNotExists(){
        assertThat(productRepository.findByCode("invalid_product_code")).isEmpty();
    }

}