package com.example.shop.service;

import com.example.shop.entity.ShoppingCartDetail;

import java.util.List;

public interface ShoppingCartDetailService {
    public List<ShoppingCartDetail> getAllShoppingCartDetail();
    public ShoppingCartDetail getShoppingCartDetail(Integer id);
    public ShoppingCartDetail saveShoppingCartDetail(ShoppingCartDetail shoppingCartDetail);
    public ShoppingCartDetail deleteShoppingCartDetail(ShoppingCartDetail shoppingCartDetail);
}
