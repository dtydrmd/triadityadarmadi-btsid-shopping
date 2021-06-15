package com.example.shop.dao;

import com.example.shop.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartDao {
    public List<ShoppingCart> getAllShoppingCart();
    public ShoppingCart getShoppingCart(Integer id);
    public ShoppingCart saveShoppingCart(ShoppingCart shoppingCart);
    public ShoppingCart deleteShoppingCart(ShoppingCart shoppingCart);
}
