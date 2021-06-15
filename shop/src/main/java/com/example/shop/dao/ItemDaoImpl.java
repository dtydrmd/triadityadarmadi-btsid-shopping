package com.example.shop.dao;

import com.example.shop.entity.Item;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ItemDaoImpl implements ItemDao{
    @Autowired
    SessionFactory sessionFactory;

    @Override
    public List<Item> getAllItem() {
        return sessionFactory.getCurrentSession().createQuery("select l from Item l").list();
    }

    @Override
    public Item getItem(Integer id) {
        return (Item) sessionFactory.getCurrentSession().get(Item.class, id);
    }

    @Override
    public Item saveItem(Item item) {
        sessionFactory.getCurrentSession().save(item);
        return item;
    }

    @Override
    public Item updateItem(Item item) {
        sessionFactory.getCurrentSession().update(item);
        return item;
    }

    @Override
    public Item deleteItem(Item item) {
        sessionFactory.getCurrentSession().update(item);
        return item;
    }
}
