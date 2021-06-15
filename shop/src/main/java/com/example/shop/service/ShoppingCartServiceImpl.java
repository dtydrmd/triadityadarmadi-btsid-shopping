package com.example.shop.service;

import com.example.shop.dao.ShoppingCartDao;
import com.example.shop.entity.ShoppingCart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService{
    @Autowired
    ShoppingCartDao shoppingCartDao;

    @Override
    public List<ShoppingCart> getAllShoppingCart() {
        return shoppingCartDao.getAllShoppingCart();
    }

    @Override
    public ShoppingCart getShoppingCart(Integer id) {
        return shoppingCartDao.getShoppingCart(id);
    }

    @Override
    public ShoppingCart saveShoppingCart(ShoppingCart shoppingCart) {
        return shoppingCartDao.saveShoppingCart(shoppingCart);
    }

    @Override
    public ShoppingCart deleteShoppingCart(ShoppingCart shoppingCart) {
        return shoppingCartDao.deleteShoppingCart(shoppingCart);
    }
}
