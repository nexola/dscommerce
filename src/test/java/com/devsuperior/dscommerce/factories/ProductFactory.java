package com.devsuperior.dscommerce.factories;

import com.devsuperior.dscommerce.dto.ProductDTO;
import com.devsuperior.dscommerce.entities.Category;
import com.devsuperior.dscommerce.entities.Product;

public class ProductFactory {

    public static Product createProduct() {
        Category category = CategoryFactory.createCategory();
        Product product = new Product(1L, "Produto", "Descrição", 250.0, "urlImagem");
        product.getCategories().add(category);
        return product;
    }

    public static Product createProduct(String name) {
        Product product = createProduct();
        product.setName(name);
        return product;
    }

    public static ProductDTO createProductDTO() {
        Product product = new Product(null, "PlayStation 5", "Lorem Ipsum Dolor sit amet", 3999.90, "imgUrl");
        Category category = new Category(2L, "Eletrônicos");
        product.getCategories().add(category);
        return new ProductDTO(product);
    }
}
