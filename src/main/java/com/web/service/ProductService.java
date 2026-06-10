package com.web.service;

import com.web.domain.Product;
import com.web.dto.ProductRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

	Page<Product> findAll(Pageable pageable);

	Product findById(Long id);

	Page<Product> findAllByKeyword(String keyword,  Pageable pageable);

	Product addProduct(ProductRequest productRequest);

	Product updateProduct(Long id, ProductRequest productRequest);

	void deleteProduct(Long id);
}
