package com.web.service;

import com.web.domain.Product;
import com.web.dto.ProductRequest;
import com.web.exception.ErrorCode;
import com.web.exception.CustomException;
import com.web.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;

	@Transactional(readOnly = true)
	@Override
	public Page<Product> findAll(Pageable pageable) {
		return productRepository.findAll(pageable);
	}

	@Transactional(readOnly = true)
	@Override
	public Product findById(Long id) {
		return productRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
	}

	@Transactional
	@Override
	public Product addProduct(ProductRequest productRequest) {
		Product product = productRequest.toProduct();
		return productRepository.save(product);
	}

	@Transactional
	@Override
	public Product updateProduct(Long id, ProductRequest productRequest) {
		Product product = findById(id);

		product.setTitle(productRequest.title());
		product.setContent(productRequest.content());
		product.setImgUrl(productRequest.imgUrl());
		product.setPrice(productRequest.price());

		return product;
	}

	@Transactional
	@Override
	public void deleteProduct(Long id) {
		productRepository.deleteById(id);
	}
}
