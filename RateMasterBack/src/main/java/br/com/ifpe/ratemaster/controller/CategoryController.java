package br.com.ifpe.ratemaster.controller;

import br.com.ifpe.ratemaster.entity.CategoryModel;
import br.com.ifpe.ratemaster.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public List<CategoryModel> listAllCategories() { return categoryService.listAllCategories();}

    @GetMapping("/{id}")
    public ResponseEntity findCategoryById(@PathVariable Long id){
        return categoryService.findCategoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/register")
    public CategoryModel registerCategory(@RequestBody CategoryModel categoryModel) { return categoryService.saveCategory(categoryModel);}

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCategoryById(@PathVariable Long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<CategoryModel> updateCategory(@PathVariable Long id, @RequestBody CategoryModel categoryModel){
        return categoryService.findCategoryById(id)
                .map(newCategory ->{
                    newCategory.setName(categoryModel.getName());

                    CategoryModel updatedCategory = categoryService.saveCategory(newCategory);
                    return ResponseEntity.ok(updatedCategory);
                }).orElse(ResponseEntity.notFound().build());
    }
}
