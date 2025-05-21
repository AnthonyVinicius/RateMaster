/*
package br.com.ifpe.ratemaster.service;

import br.com.ifpe.ratemaster.entity.ProductModel;
import br.com.ifpe.ratemaster.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<ProductModel> listAllProducts() {return productRepository.findAll();}

    public ProductModel saveProduct(ProductModel productModel) {return productRepository.save(productModel);}

    public Optional<ProductModel> findProductById(Long id) {return productRepository.findById(id);}

    public void deleteProduct(Long id) {productRepository.deleteById(id);}

    public List<ProductModel> findProductByCategory(String name) {return productRepository.findByCategoryModelName(name); }

}
 */
