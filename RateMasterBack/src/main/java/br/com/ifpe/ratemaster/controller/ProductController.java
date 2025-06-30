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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
    public List<ProductModel> listAllProducts() {
        return productService.listAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity findProductById(@PathVariable Long id) {
        return productService.findProductByIdWithReviewsAndResponses(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/myProducts")
    public ResponseEntity<List<ProductModel>> listMyProducts() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            System.out.println("auth null");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String email = auth.getName();
        UserModel user = (UserModel) userService.loadUserByUsername(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        List<ProductModel> products = productService.findByUserId(user.getId());
        return ResponseEntity.ok(products);
    }

    @PostMapping("/register")
    public ProductModel registerProduct(@RequestBody ProductDTO dto) {

        BrandModel brand = brandService.findBrandById(dto.brandModel).orElseThrow();
        CategoryModel category = categoryService.findCategoryById(dto.categoryModel).orElseThrow();
        UserModel user = userService.findUserById(dto.userId).orElseThrow();

        ProductModel product = new ProductModel();
        product.setName(dto.name);
        product.setDescription(dto.description);
        product.setPrice(dto.price);
        product.setBrandModel(brand);
        product.setCategoryModel(category);
        product.setType(dto.type);
        product.setImage(dto.image);
        product.setUserModel(user);

        return productService.saveProduct(product);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/disable/{id}")
    public ResponseEntity<Object> disableProductById(@PathVariable Long id) {
        return productService.findProductById(id).map(product ->{
            if(product.getDisabled()){
                product.setDisabled(false);
            } else {
                product.setDisabled(true);
            }
            productService.saveProduct(product);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ProductModel> updateProduct(@PathVariable Long id, @RequestBody ProductDTO dto) {
        return productService.findProductById(id).map(existingProduct -> {
            existingProduct.setName(dto.name);
            existingProduct.setDescription(dto.description);
            existingProduct.setType(dto.type);
            existingProduct.setPrice(dto.price);
            existingProduct.setImage(dto.image);

            BrandModel brand = brandService.findBrandById(dto.brandModel).orElseThrow();
            CategoryModel category = categoryService.findCategoryById(dto.categoryModel).orElseThrow();
            UserModel user = userService.findUserById(dto.userId).orElseThrow();

            existingProduct.setBrandModel(brand);
            existingProduct.setCategoryModel(category);
            existingProduct.setUserModel(user);

            ProductModel savedProduct = productService.saveProduct(existingProduct);
            return ResponseEntity.ok(savedProduct);
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/reviewedBy/{id}")
    public List<ProductModel> getProductsReviewedByUser(@PathVariable String id) {
        return productService.getProductsReviewedByUser(id);
    }

}