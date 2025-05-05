package br.com.ifpe.ratemaster.controller;

import br.com.ifpe.ratemaster.entity.ProductModel;
import br.com.ifpe.ratemaster.repository.ProductRepository;
import br.com.ifpe.ratemaster.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping
    private List<ProductModel> listAllProducts(){
        return productService.listAllProducts();
    }

    @GetMapping("/{id}")
    private ResponseEntity findProductById(@PathVariable Long id){
        return productService.findProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/register")
    private ProductModel registerProduct(@RequestBody ProductModel productModel){
        return productService.saveProduct(productModel);
    }

    @DeleteMapping("/delete/{id}")
    private ResponseEntity<Void> deleteProductById(@PathVariable Long id){
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

//    @GetMapping("/category/{name}")
//    private List<ProductModel> findByCategory(@PathVariable String name) {
//        return productService.findProductByCategory(name);}
}
