package com.example.shop.service;

import com.example.shop.entity.Item;

import java.util.List;

public interface ItemService {
    public List<Item> getAllItem();
    public Item getItem(Integer id);
    public Item saveItem(Item item);
    public Item updateItem(Item item);
    public Item deleteItem(Item item);
}
