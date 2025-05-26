package br.com.ifpe.ratemaster.controller;

import br.com.ifpe.ratemaster.dto.ProductDTO;
import br.com.ifpe.ratemaster.entity.BrandModel;
import br.com.ifpe.ratemaster.entity.CategoryModel;
import br.com.ifpe.ratemaster.entity.ProductModel;
import br.com.ifpe.ratemaster.entity.UserModel;
import br.com.ifpe.ratemaster.service.BrandService;
import br.com.ifpe.ratemaster.service.CategoryService;
import br.com.ifpe.ratemaster.service.ProductService;
import br.com.ifpe.ratemaster.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

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
    private ProductModel registerProduct(@RequestBody ProductDTO dto) {

        BrandModel brand = brandService.findBrandById(dto.brandModel).orElseThrow();
        CategoryModel category = categoryService.findCategoryById(dto.categoryModel).orElseThrow();
        UserModel user = productService.findProductById(dto.userId).orElseThrow().getUserModel();

        ProductModel product = new ProductModel();
        product.setName(dto.name);
        product.setDescription(dto.description);
        product.setPrice(dto.price);
        product.setBrandModel(brand);
        product.setCategoryModel(category);
        product.setImage(dto.image);
        product.setUserModel(user);

        return productService.saveProduct(product);
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