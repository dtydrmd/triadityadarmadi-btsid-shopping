package com.example.shop.dao;

import com.example.shop.entity.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDaoImpl implements  UserDao{

    @Autowired
    SessionFactory sessionFactory;

    @Override
    public List<User> getAllUser() {
        return sessionFactory.getCurrentSession().createQuery("select l from User l").list();
    }

    @Override
    public User getUser(Integer id) {
        return (User) sessionFactory.getCurrentSession().get(User.class, id);
    }

    @Override
    public User saveUser(User user) {
        sessionFactory.getCurrentSession().save(user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        sessionFactory.getCurrentSession().update(user);
        return user;
    }

    @Override
    public User deleteUser(User user) {
        sessionFactory.getCurrentSession().update(user);
        return user;
    }
}
