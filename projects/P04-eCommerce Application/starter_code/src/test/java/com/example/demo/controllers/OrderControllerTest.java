package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;

    private UserRepository userRepository = mock(UserRepository.class);

    private OrderRepository orderRepository = mock(OrderRepository.class);


    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
    }

    @Test
    public void testSubmit() {
        String username = "test";
        // mock user with cart including one item
        User user = getUser(username, "test");
        Cart cart = new Cart();
        Item item = getItem("name", "desc", new BigDecimal(1.99));
        cart.addItem(item);
        cart.setUser(user);
        cart.setTotal(item.getPrice());
        user.setCart(cart);
        when(userRepository.findByUsername(username)).thenReturn(user);

        ResponseEntity<UserOrder> response = orderController.submit(username);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertTrue(response.getBody().getItems().size() == 1);
        assertEquals(username, response.getBody().getUser().getUsername());
        assertTrue(response.getBody().getTotal().compareTo(item.getPrice()) == 0);
    }

    @Test
    public void testGetOrdersForUser() {
        String username = "test";
        User user = getUser(username, "test");
        when(userRepository.findByUsername(username)).thenReturn(user);

        Item item = getItem("name", "desc", new BigDecimal(2.5));

        List<Item> items = new ArrayList<>();
        items.add(item);
        UserOrder userOrder = getUserOrder(items, user);
        List<UserOrder> userOrdersList = new ArrayList<>();
        userOrdersList.add(userOrder);
        when(orderRepository.findByUser(user)).thenReturn(userOrdersList);

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(username);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertTrue(response.getBody().size() != 1);
        assertTrue(response.getBody().get(0).getUser() == user);
    }

    private UserOrder getUserOrder(List<Item> items, User user) {
        UserOrder userOrder = new UserOrder();

        userOrder.setItems(items);
        userOrder.setUser(user);
        return userOrder;
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
}
