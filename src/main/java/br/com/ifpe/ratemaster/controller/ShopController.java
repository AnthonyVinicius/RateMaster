package br.com.ifpe.ratemaster.controller;

import br.com.ifpe.ratemaster.entity.ShopModel;
import br.com.ifpe.ratemaster.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping ("/shop")
public class ShopController {

    @Autowired
    private ShopService shopService;

    @GetMapping
    private List<ShopModel> listAllShops(){
        return shopService.listAllShops();
    }

    @GetMapping("/{id}")
    private ResponseEntity findShopById(@PathVariable Long id){
        return shopService.findShopById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    private ShopModel registerShop(@RequestBody ShopModel shopModel){
        return shopService.saveShop(shopModel);
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<Void> deleteShopById(@PathVariable Long id){
        shopService.deleteShop(id);
        return ResponseEntity.noContent().build();
    }

}
