package com.example.shop.dao;

import com.example.shop.entity.ShoppingCart;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ShoppingCartDaoImpl implements ShoppingCartDao{
    @Autowired
    SessionFactory sessionFactory;

    @Override
    public List<ShoppingCart> getAllShoppingCart() {
        return sessionFactory.getCurrentSession().createQuery("select l from ShoppingCart l").list();
    }

    @Override
    public ShoppingCart getShoppingCart(Integer id) {
        return (ShoppingCart) sessionFactory.getCurrentSession().get(ShoppingCart.class, id);
    }

    @Override
    public ShoppingCart saveShoppingCart(ShoppingCart shoppingCart) {
        sessionFactory.getCurrentSession().save(shoppingCart);
        return shoppingCart;
    }

    @Override
    public ShoppingCart deleteShoppingCart(ShoppingCart shoppingCart) {
        sessionFactory.getCurrentSession().delete(shoppingCart);
        return shoppingCart;
    }
}
