package ua.com.alevel.service;

import ua.com.alevel.persistence.entity.Product;

import java.util.List;

public interface ProductService extends BaseService<Product> {

    Product createProduct(Product product);

    Product updateProduct(Product product);

    void deleteProduct(Long productId);

    Product findProductById(Long productId);

    List<Product> findAllProducts();

    Boolean doesProductExist(Long productId);
}
