package com.example.shop.service;

import com.example.shop.dao.ItemDao;
import com.example.shop.entity.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ItemServiceImpl implements ItemService{
    @Autowired
    ItemDao itemDao;

    @Override
    public List<Item> getAllItem() {
        return itemDao.getAllItem();
    }

    @Override
    public Item getItem(Integer id) {
        return itemDao.getItem(id);
    }

    @Override
    public Item saveItem(Item item) {
        return itemDao.saveItem(item);
    }

    @Override
    public Item updateItem(Item item) {
        return itemDao.updateItem(item);
    }

    @Override
    public Item deleteItem(Item item) {
        return itemDao.deleteItem(item);
    }
}
