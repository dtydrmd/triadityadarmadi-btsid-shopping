package com.example.shop.service;

import com.example.shop.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {
    public List<ShoppingCart> getAllShoppingCart();
    public ShoppingCart getShoppingCart(Integer id);
    public ShoppingCart saveShoppingCart(ShoppingCart shoppingCart);
    public ShoppingCart deleteShoppingCart(ShoppingCart shoppingCart);
}
