package br.com.ifpe.ratemaster.service;

import br.com.ifpe.ratemaster.repository.ShopRepository;
import br.com.ifpe.ratemaster.entity.ShopModel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShopService {

    @Autowired
    private ShopRepository shopRepository;

    public List<ShopModel> listAllShops() {
        return shopRepository.findAll();
    }

    public ShopModel saveShop(ShopModel shopModel) {
        return shopRepository.save(shopModel);
    }

    public Optional<ShopModel> findShopById(Long id) {
        return shopRepository.findById(id);
    }

    public void deleteShop(Long id) {
        shopRepository.deleteById(id);
    }
}


