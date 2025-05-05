package br.com.ifpe.ratemaster.controller;

import br.com.ifpe.ratemaster.entity.CategoryModel;
import br.com.ifpe.ratemaster.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@CrossOrigin(origins = "http://localhost:5173")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    private List<CategoryModel> listAllCategories() { return categoryService.listAllCategories();}

    @GetMapping("/{id}")
    private ResponseEntity findCategoryById(@PathVariable Long id){
        return categoryService.findCategoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    private CategoryModel registerCategory(@RequestBody CategoryModel categoryModel) { return categoryService.saveCategory(categoryModel);}

    @DeleteMapping("/{id}")
    private ResponseEntity<Void> deleteCategoryById(@PathVariable Long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

}
