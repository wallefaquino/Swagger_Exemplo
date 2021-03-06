package com.ais.swagger.resource;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ais.swagger.model.Product;
import com.ais.swagger.repository.ProductRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value="StockOnline")
@RestController
@RequestMapping("/products")
public class ProductResource {
	
	@Autowired
	private ProductRepository repository;
	
	@ApiOperation(value = "Return a list of products.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "List return with success"),
							@ApiResponse(code = 401, message = "User Unauthorized"),
							@ApiResponse(code = 403, message = "Access Forbidden"),
							@ApiResponse(code = 404, message = "Products not found"),
							@ApiResponse(code = 500, message = "Error on Server")})
	
	@GetMapping(produces="application/json")
	public Iterable<Product> list() {
		return repository.findAll();
	}
	
	@ApiOperation(value = "Find products by id.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Product return with success"),
							@ApiResponse(code = 401, message = "User Unauthorized"),
							@ApiResponse(code = 403, message = "Access Forbidden"),
							@ApiResponse(code = 404, message = "Product not found"),
							@ApiResponse(code = 500, message = "Error on Server")})
	
	@GetMapping(value = "/{id}" ,produces="application/json")
	public Product find(@PathVariable Long id) {
		
		return repository.findOne(id);
	}
	
	@ApiOperation(value = "Save products.")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Product saved with success"),
							@ApiResponse(code = 401, message = "User Unauthorized"),
							@ApiResponse(code = 403, message = "Access Forbidden"),
							@ApiResponse(code = 500, message = "Error on Server")})
	
	@PostMapping
	public ResponseEntity<Void> save(@RequestBody Product product) {
		repository.save(product);
		
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@ApiOperation(value = "Delete products.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Product deleted with success"),
							@ApiResponse(code = 401, message = "User Unauthorized"),
							@ApiResponse(code = 403, message = "Access Forbidden"),
							@ApiResponse(code = 404, message = "Product not found"),
							@ApiResponse(code = 500, message = "Error on Server")})
	
	@DeleteMapping(value="/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		Product product = repository.findOne(id);
		
		if(product == null) 
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		
		repository.delete(product);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@ApiOperation(value = "Update products.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Product uptaded with success"),
							@ApiResponse(code = 401, message = "User Unauthorized"),
							@ApiResponse(code = 403, message = "Access Forbidden"),
							@ApiResponse(code = 404, message = "Product not found"),
							@ApiResponse(code = 500, message = "Error on Server")})
	
	@PutMapping(value="/{id}")
	public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody Product actual) {
		Product product = repository.findOne(id);
		
		if(product == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		
		BeanUtils.copyProperties(actual, product, "id");
		
		repository.save(actual);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
