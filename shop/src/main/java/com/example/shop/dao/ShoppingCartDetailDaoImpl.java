package com.example.shop.dao;

import com.example.shop.entity.ShoppingCartDetail;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ShoppingCartDetailDaoImpl implements ShoppingCartDetailDao{
    @Autowired
    SessionFactory sessionFactory;

    @Override
    public List<ShoppingCartDetail> getAllShoppingCartDetail() {
        return sessionFactory.getCurrentSession().createQuery("select l from ShoppingCartDetail l").list();
    }

    @Override
    public ShoppingCartDetail getShoppingCartDetail(Integer id) {
        return (ShoppingCartDetail) sessionFactory.getCurrentSession().get(ShoppingCartDetail.class, id);
    }

    @Override
    public ShoppingCartDetail saveShoppingCartDetail(ShoppingCartDetail shoppingCartDetail) {
        sessionFactory.getCurrentSession().save(shoppingCartDetail);
        return shoppingCartDetail;
    }

    @Override
    public ShoppingCartDetail deleteShoppingCartDetail(ShoppingCartDetail shoppingCartDetail) {
        sessionFactory.getCurrentSession().save(shoppingCartDetail);
        return shoppingCartDetail;
    }
}
