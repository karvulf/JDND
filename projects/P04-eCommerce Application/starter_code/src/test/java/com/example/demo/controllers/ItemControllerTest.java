package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemRepository itemRepository = mock(ItemRepository.class);

    private ItemController itemController;

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void testGetItems() {
        ResponseEntity<List<Item>> response = itemController.getItems();

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    public void testGetItemById() {
        Long id = 0L;
        String name = "name";
        String description = "test";
        BigDecimal price = new BigDecimal(2.99);

        Item item = getItem(name, description, price);

        when(itemRepository.findById(id)).thenReturn(Optional.of(item));
        ResponseEntity<Item> response = itemController.getItemById(id);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(name, response.getBody().getName());
        assertEquals(description, response.getBody().getDescription());
        assertEquals(price, response.getBody().getPrice());
    }

    @Test
    public void testGetItemsByName() {
        Long id = 0L;
        String name = "name";
        String description = "test";
        BigDecimal price = new BigDecimal(2.99);

        Item item = getItem(name, description, price);
        Item item2 = getItem(name, description, price);
        List<Item> itemsList = new ArrayList<>();
        itemsList.add(item);
        itemsList.add(item2);

        when(itemRepository.findByName(name)).thenReturn(itemsList);
        ResponseEntity<List<Item>> response = itemController.getItemsByName(name);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertTrue(response.getBody().size() == 2);
    }


    private Item getItem(String name, String description, BigDecimal price) {
        Item item = new Item();
        item.setName(name);
        item.setDescription(description);
        item.setPrice(price);
        return item;
    }
}
