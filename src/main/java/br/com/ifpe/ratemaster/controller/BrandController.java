package br.com.ifpe.ratemaster.controller;

import br.com.ifpe.ratemaster.entity.BrandModel;
import br.com.ifpe.ratemaster.entity.ProductModel;
import br.com.ifpe.ratemaster.service.BrandService;
import br.com.ifpe.ratemaster.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    @GetMapping
    private List<BrandModel> listAllBrands(){
        return brandService.listAllBrands();
    }

    @GetMapping("/{id}")
    private ResponseEntity findBrandById(@PathVariable Long id){
        return brandService.findBrandById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    private BrandModel registerBrand(@RequestBody BrandModel brandModel){
        return brandService.saveBrand(brandModel);
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<Void> deleteBrandById(@PathVariable Long id){
        brandService.deleteBrand(id);
        return ResponseEntity.noContent().build();
    }
}
