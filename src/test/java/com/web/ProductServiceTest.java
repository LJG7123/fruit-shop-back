package com.web;

import com.web.domain.Product;
import com.web.dto.ProductRequest;
import com.web.exception.CustomException;
import com.web.exception.ErrorCode;
import com.web.repository.ProductRepository;
import com.web.service.ProductServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

	@Mock
	ProductRepository productRepository;

	@InjectMocks
	ProductServiceImpl productService;

	@Test
	@DisplayName("findAll returns paged products")
	void findAll_returnsPagedProducts() {
		Pageable pageable = PageRequest.of(0, 10);
		Product product = product(1L, "Apple", "Fresh apple", "apple.jpg", 1000L);
		Page<Product> page = new PageImpl<>(List.of(product), pageable, 1);
		when(productRepository.findAll(pageable)).thenReturn(page);

		Page<Product> result = productService.findAll(pageable);

		assertEquals(1, result.getTotalElements());
		assertSame(product, result.getContent().get(0));
		verify(productRepository).findAll(pageable);
	}

	@Test
	@DisplayName("findById returns product when product exists")
	void findById_existingProduct_returnsProduct() {
		Product product = product(1L, "Apple", "Fresh apple", "apple.jpg", 1000L);
		when(productRepository.findById(1L)).thenReturn(Optional.of(product));

		Product result = productService.findById(1L);

		assertSame(product, result);
		verify(productRepository).findById(1L);
	}

	@Test
	@DisplayName("findById throws PRODUCT_NOT_FOUND when product does not exist")
	void findById_missingProduct_throwsException() {
		when(productRepository.findById(1L)).thenReturn(Optional.empty());

		CustomException exception = assertThrows(CustomException.class, () -> productService.findById(1L));

		assertEquals(ErrorCode.PRODUCT_NOT_FOUND, exception.getErrorCode());
		verify(productRepository).findById(1L);
	}

	@Test
	@DisplayName("findAllByKeyword searches title and content")
	void findAllByKeyword_returnsPagedProducts() {
		Pageable pageable = PageRequest.of(0, 10);
		String keyword = "apple";
		Product product = product(1L, "Apple", "Fresh fruit", "apple.jpg", 1000L);
		Page<Product> page = new PageImpl<>(List.of(product), pageable, 1);
		when(productRepository.findAllByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword, keyword, pageable))
				.thenReturn(page);

		Page<Product> result = productService.findAllByKeyword(keyword, pageable);

		assertEquals(1, result.getTotalElements());
		assertSame(product, result.getContent().get(0));
		verify(productRepository).findAllByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword, keyword, pageable);
	}

	@Test
	@DisplayName("addProduct saves product from request")
	void addProduct_savesProduct() {
		ProductRequest request = new ProductRequest("Apple", "Fresh apple", "apple.jpg", 1000L);
		Product savedProduct = product(1L, request.title(), request.content(), request.imgUrl(), request.price());
		when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

		Product result = productService.addProduct(request);

		assertSame(savedProduct, result);
		verify(productRepository).save(any(Product.class));
	}

	@Test
	@DisplayName("updateProduct updates existing product fields")
	void updateProduct_updatesFields() {
		Product product = product(1L, "Apple", "Fresh apple", "apple.jpg", 1000L);
		ProductRequest request = new ProductRequest("Banana", "Sweet banana", "banana.jpg", 2000L);
		when(productRepository.findById(1L)).thenReturn(Optional.of(product));

		Product result = productService.updateProduct(1L, request);

		assertSame(product, result);
		assertEquals("Banana", result.getTitle());
		assertEquals("Sweet banana", result.getContent());
		assertEquals("banana.jpg", result.getImgUrl());
		assertEquals(2000L, result.getPrice());
		verify(productRepository).findById(1L);
	}

	@Test
	@DisplayName("deleteProduct deletes by id")
	void deleteProduct_deletesById() {
		productService.deleteProduct(1L);

		verify(productRepository).deleteById(1L);
	}

	private Product product(Long id, String title, String content, String imgUrl, Long price) {
		return Product.builder()
				.id(id)
				.title(title)
				.content(content)
				.imgUrl(imgUrl)
				.price(price)
				.build();
	}
}
