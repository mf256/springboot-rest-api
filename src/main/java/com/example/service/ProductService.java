package com.example.service;

import com.example.model.Product;

import java.util.List;

public interface ProductService
{

    Product createProduct(Product product);

    List<Product> getAllProduct();

    Product getProductById(long productId);

    Product updateProduct(Product product);

    void deleteProduct(long id);

}
