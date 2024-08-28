package com.ecom.ecomProject.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ecom.ecomProject.model.Product;
import com.ecom.ecomProject.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@CrossOrigin
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return service.getAllProducts();
        
    }

    @GetMapping("/product/{id}")
    public Product geProduct(@PathVariable int id){
        return service.getProductById(id);
    }

    @PostMapping("/product")
    public ResponseEntity<?> addProduct( @RequestPart Product product,@RequestPart MultipartFile imageFile) {

        try{
            Product product1 = service.addProduct(product, imageFile);
            return new ResponseEntity<>(product1, HttpStatus.CREATED);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/product/{productId}/image")
    public ResponseEntity<byte[]> addImageByProductId(@PathVariable int productId) {

        Product product = service.getProductById(productId);
        byte[] imageFile = product.getImageData();
        return ResponseEntity.ok().contentType(MediaType.valueOf(product.getImageType())).body(imageFile);
    }
    
    @PutMapping("/product/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable int id, @RequestPart Product product,@RequestPart MultipartFile imageFile) throws IOException{

        Product product1 = service.updateProduct(id, product, imageFile);
        if ( product1!= null) {
            return new ResponseEntity<>("Product Updated", HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Failed to update", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id){
        Product product = service.getProductById(id);
        if( product != null){
            service.deleteProduct(id);
            return new ResponseEntity<>("Product Deleted! Please Refresh", HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Product Not Found", HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping("/products/search")
    public ResponseEntity<List<Product>> searchProduct(@RequestParam String keyword) {
        List<Product> products = service.searchProduct(keyword);

        return new ResponseEntity<>(products, HttpStatus.OK);
    }
    
    

}
