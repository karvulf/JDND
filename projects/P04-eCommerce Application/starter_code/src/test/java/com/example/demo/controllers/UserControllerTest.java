package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void create_user_create_path() throws Exception {
        String username = "test";
        String password = "testPassword";
        String hash = "thisIsHashed";
        when(encoder.encode(password)).thenReturn(hash);

        ResponseEntity<User> response = createUser(username, password);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals(username, u.getUsername());
        assertEquals(hash, u.getPassword());
    }

    @Test
    public void testFindByUserName() {
        String username = "test";
        String password = "testPassword";

        User user = getUser(username, password);

        when(userRepository.findByUsername(username)).thenReturn(user);
        ResponseEntity<User> response = userController.findByUserName(username);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(username, response.getBody().getUsername());
        assertEquals(password, response.getBody().getPassword());
    }

    @Test
    public void testFindById() {
        String username = "test";
        String password = "testPassword";
        Long id = 0L;

        User user = getUser(username, password);

        when(userRepository.findById(0L)).thenReturn(java.util.Optional.of(user));
        ResponseEntity<User> response = userController.findById(id);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(username, response.getBody().getUsername());
        assertEquals(password, response.getBody().getPassword());

    }

    private ResponseEntity<User> createUser(String username, String password) {
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername(username);
        r.setPassword(password);
        r.setConfirmPassword(password);

        return userController.createUser(r);
    }

    private User getUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        return user;
    }

}
