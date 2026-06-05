package com.web.controller;

import com.web.dto.ProductRequest;
import com.web.dto.ProductResponse;
import com.web.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;

	@GetMapping
	public ResponseEntity<?> findAll() {
		return new ResponseEntity<>(
				productService.findAll().stream().map(ProductResponse::from).toList(),
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
