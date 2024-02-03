package com.devsuperior.dscommerce.factories;

import com.devsuperior.dscommerce.entities.Category;

public class CategoryFactory {

    public static Category createCategory() {
        return new Category(1L, "Categoria");
    }

    public static Category createCategory(Long id, String name) {
        return new Category(id, name);
    }
}
