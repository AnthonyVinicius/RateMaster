package br.com.ifpe.ratemaster.controller;

import br.com.ifpe.ratemaster.entity.BrandModel;
import br.com.ifpe.ratemaster.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @GetMapping
    public List<BrandModel> listAllBrands() {
        return brandService.listAllBrands();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BrandModel> findBrandById(@PathVariable Long id) {
        return brandService.findBrandById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/register")
    @PreAuthorize("hasRole('BUSINESS')")
    public BrandModel registerBrand(@RequestBody BrandModel brandModel) {
        return brandService.saveBrand(brandModel);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteBrandById(@PathVariable Long id) {
        brandService.deleteBrand(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<BrandModel> updateBrand(@PathVariable Long id, @RequestBody BrandModel brandModel){
        return brandService.findBrandById(id)
                .map(newBrand ->{
                    newBrand.setName(brandModel.getName());

                    BrandModel updatedBrand = brandService.saveBrand(newBrand);
                    return ResponseEntity.ok(updatedBrand);
                }).orElse(ResponseEntity.notFound().build());
    }
}

