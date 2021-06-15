package com.example.shop.service;

import com.example.shop.dao.ShoppingCartDetailDao;
import com.example.shop.entity.ShoppingCartDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ShoppingCartDetailServiceImpl implements ShoppingCartDetailService{
    @Autowired
    ShoppingCartDetailDao shoppingCartDetailDao;

    @Override
    public List<ShoppingCartDetail> getAllShoppingCartDetail() {
        return shoppingCartDetailDao.getAllShoppingCartDetail();
    }

    @Override
    public ShoppingCartDetail getShoppingCartDetail(Integer id) {
        return shoppingCartDetailDao.getShoppingCartDetail(id);
    }

    @Override
    public ShoppingCartDetail saveShoppingCartDetail(ShoppingCartDetail shoppingCartDetail) {
        return shoppingCartDetailDao.saveShoppingCartDetail(shoppingCartDetail);
    }

    @Override
    public ShoppingCartDetail deleteShoppingCartDetail(ShoppingCartDetail shoppingCartDetail) {
        return shoppingCartDetailDao.deleteShoppingCartDetail(shoppingCartDetail);
    }
}
