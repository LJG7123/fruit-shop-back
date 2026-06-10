package com.web.service;

import com.web.domain.Product;
import com.web.dto.ProductRequest;
import com.web.exception.ErrorCode;
import com.web.exception.CustomException;
import com.web.repository.ProductRepository;
import com.web.util.PageValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;

	@Transactional(readOnly = true)
	@Override
	public Page<Product> findAll(Pageable pageable) {
		Page<Product> products =  productRepository.findAll(pageable);
		PageValidator.validatePageRange(products, pageable);

		return products;
	}

	@Transactional(readOnly = true)
	@Override
	public Product findById(Long id) {
		return productRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
	}

	@Transactional(readOnly = true)
	@Override
	public Page<Product> findAllByKeyword(String keyword, Pageable pageable) {
		Page<Product> products = productRepository.findAllByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword, keyword, pageable);
		PageValidator.validatePageRange(products, pageable);

		return products;
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
