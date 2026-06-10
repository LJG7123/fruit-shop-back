package com.web.controller;

import com.web.dto.ProductRequest;
import com.web.dto.ProductResponse;
import com.web.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@Tag(name = "Product API")
public class ProductController {

	@Value("${PAGE_COUNT}")
	private int PAGE_SIZE;
	private final ProductService productService;

	@GetMapping
	public ResponseEntity<?> findAll(@RequestParam(defaultValue = "1") int page,
	@RequestParam(required = false) String keyword) {
		Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE);

		if (keyword == null || keyword.isBlank()) {
			return new ResponseEntity<>(
					productService.findAll(pageable).map(ProductResponse::from).toList(),
					HttpStatus.OK
			);
		}

		return new ResponseEntity<>(
				productService.findAllByKeyword(keyword, pageable).stream().map(ProductResponse::from).toList(),
				HttpStatus.OK
		);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> findById(@PathVariable Long id) {
		return new ResponseEntity<>(ProductResponse.from(productService.findById(id)), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<?> save(@RequestBody ProductRequest productRequest) {
		return new ResponseEntity<>(ProductResponse.from(productService.addProduct(productRequest)), HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ProductRequest productRequest) {
		return new ResponseEntity<>(ProductResponse.from(productService.updateProduct(id, productRequest)), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		productService.deleteProduct(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
