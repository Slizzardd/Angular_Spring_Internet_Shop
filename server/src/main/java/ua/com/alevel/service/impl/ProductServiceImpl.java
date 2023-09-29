package ua.com.alevel.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import ua.com.alevel.exception.EntityNotFoundException;
import ua.com.alevel.persistence.entity.Product;
import ua.com.alevel.persistence.repository.ProductRepository;
import ua.com.alevel.service.ProductService;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }

    @Override
    public Product findProductById(Long productId) {
        if (doesProductExist(productId)){
            return productRepository.findById(productId).orElse(null);
        }else {
            throw new EntityNotFoundException("You do send incorrect data");
        }
    }

    @Override
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Boolean doesProductExist(Long productId) {
        return productRepository.existsById(productId);
    }
}
