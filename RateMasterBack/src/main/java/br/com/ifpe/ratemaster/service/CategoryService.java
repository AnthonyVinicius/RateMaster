package br.com.ifpe.ratemaster.service;

import br.com.ifpe.ratemaster.entity.CategoryModel;
import br.com.ifpe.ratemaster.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<CategoryModel> listAllCategories() { return categoryRepository.findAll();}

    public CategoryModel saveCategory(CategoryModel categoryModel) {return categoryRepository.save(categoryModel);}

    public Optional<CategoryModel> findCategoryById(Long id) {return categoryRepository.findById(id);}

    public void deleteCategory(Long id) {categoryRepository.deleteById(id);}
    }
