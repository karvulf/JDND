package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartRepository cartRepository = mock(CartRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);

    private CartController cartController;

    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void testAddTocart() {
        int quantity = 2;
        String username = "test";

        User user = getUser(username, "test");
        user.setCart(new Cart());
        Item item = getItem("name", "desc", new BigDecimal(2.99));
        item.setId(0L);

        when(userRepository.findByUsername(username)).thenReturn(user);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        ModifyCartRequest modifyCartRequest = getModifyCartRequest(item.getId(), quantity, username);

        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertTrue(response.getBody().getItems().size() == quantity);
        BigDecimal expected = item.getPrice().add(item.getPrice());
        assertTrue(response.getBody().getTotal().compareTo(expected) == 0);
    }

    @Test
    public void testRemoveFromcart() {
        String username = "test";


        // mock item
        Item item = getItem("name", "desc", new BigDecimal(2.99));
        item.setId(0L);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        // mock user with cart including one item
        User user = getUser(username, "test");
        Cart cart = new Cart();
        cart.addItem(item);
        user.setCart(cart);
        when(userRepository.findByUsername(username)).thenReturn(user);

        ModifyCartRequest modifyCartRequest = getModifyCartRequest(item.getId(), 1, username);
        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertTrue(response.getBody().getItems().size() == 0);
        assertTrue(response.getBody().getTotal().intValue() == 0);
    }

    private Item getItem(String name, String description, BigDecimal price) {
        Item item = new Item();
        item.setName(name);
        item.setDescription(description);
        item.setPrice(price);
        return item;
    }

    private User getUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        return user;
    }

    private ModifyCartRequest getModifyCartRequest(long itemId, int quantity, String username) {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(itemId);
        modifyCartRequest.setQuantity(quantity);
        modifyCartRequest.setUsername(username);
        return modifyCartRequest;
    }
}
