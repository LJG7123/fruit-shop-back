package com.web.service;

import com.web.domain.Product;
import com.web.dto.ProductRequest;

import java.util.List;

public interface ProductService {

	List<Product> findAll();

	Product findById(Long id);

	Product addProduct(ProductRequest productRequest);

	Product updateProduct(Long id, ProductRequest productRequest);

	void deleteProduct(Long id);
}
