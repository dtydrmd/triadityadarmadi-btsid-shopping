package com.example.shop.controller;

import com.example.shop.entity.Item;
import com.example.shop.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController

public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping("/item")
    public Iterable<Item> getCustomers(){
        return itemService.getAllItem();
    }

    @PostMapping("/add-item")
    public String add(@RequestParam String code, @RequestParam String name, @RequestParam String info, @RequestParam Integer stock){
        Item item = new Item();
        item.setCode(code);
        item.setName(name);
        item.setInfo(info);
        item.setStock(stock);
        itemService.saveItem(item);
        return "Added new Item";
    }

    @GetMapping("/find-item/{id}")
    public Item getItemById(@PathVariable Integer id){
        return itemService.getItem(id);
    }

    @PutMapping("/update-item/{id}")
    public String update(@RequestBody Item newItem, @PathVariable Integer id) {

        Item updatedEmployee = itemService.updateItem(newItem);
        return "Item has successfully updated";
    }

    @DeleteMapping("/delete-item/{id}")
    public String delete(@RequestBody Item item, @PathVariable Integer id) {

        itemService.deleteItem(item);

        return "Item has successfully deleted";
    }
}
