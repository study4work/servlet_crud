package com.lysenko;

import com.lysenko.controller.UserController;
import com.lysenko.entity.User;
import com.lysenko.repository.UserRepository;
import com.lysenko.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserTest {

    private final UserService userService = mock(UserService.class);

    private final UserController userController = new UserController();
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);

    @Test
    public void saveUser()  throws ServletException, IOException {
        String jsonData = "{\"name\": \"Alex\"}";

        when(request.getMethod()).thenReturn("POST");
        when(request.getContentType()).thenReturn("application/json");
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonData)));

        userController.doPost(request, response);

        verify(userService, times(1)).save(any(User.class));
    }
}
