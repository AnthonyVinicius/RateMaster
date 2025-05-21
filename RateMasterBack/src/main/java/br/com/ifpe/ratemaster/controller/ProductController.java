/*
package br.com.ifpe.ratemaster.controller;

import br.com.ifpe.ratemaster.dto.ProductDTO;
import br.com.ifpe.ratemaster.entity.BrandModel;
import br.com.ifpe.ratemaster.entity.CategoryModel;
import br.com.ifpe.ratemaster.entity.ProductModel;
import br.com.ifpe.ratemaster.service.BrandService;
import br.com.ifpe.ratemaster.service.CategoryService;
import br.com.ifpe.ratemaster.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    private ShopService shopService;

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
        ShopModel shop = shopService.findShopById(dto.shopModel).orElseThrow();
        CategoryModel category = categoryService.findCategoryById(dto.categoryModel).orElseThrow();

        ProductModel product = new ProductModel();
        product.setName(dto.name);
        product.setDescription(dto.description);
        product.setPrice(dto.price);
        product.setBrandModel(brand);
        product.setUserModel(shop);
        product.setCategoryModel(category);
        product.setImage(dto.image);

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
 */