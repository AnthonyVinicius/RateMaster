package br.com.ifpe.ratemaster.service;

import br.com.ifpe.ratemaster.entity.BrandModel;
import br.com.ifpe.ratemaster.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


@Service
public class BrandService {
    @Autowired
    private BrandRepository brandRepository;

    public List<BrandModel> listAllBrands() {return brandRepository.findAll();}

    public BrandModel saveBrand(BrandModel brandModel) {return brandRepository.save(brandModel);}

    public Optional<BrandModel> findBrandById(Long id) {return brandRepository.findById(id);}

    public void deleteBrand(Long id) {brandRepository.deleteById(id);}
}
