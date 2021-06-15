package com.example.shop.dao;

import com.example.shop.entity.ShoppingCartDetail;

import java.util.List;

public interface ShoppingCartDetailDao {
    public List<ShoppingCartDetail> getAllShoppingCartDetail();
    public ShoppingCartDetail getShoppingCartDetail(Integer id);
    public ShoppingCartDetail saveShoppingCartDetail(ShoppingCartDetail shoppingCartDetail);
    public ShoppingCartDetail deleteShoppingCartDetail(ShoppingCartDetail shoppingCartDetail);
}
