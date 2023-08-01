package com.lysenko;

import com.lysenko.entity.User;
import com.lysenko.rest.UserRestControllerV1;
import com.lysenko.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserTest {

    private final UserService userService = mock(UserService.class);

    private final UserRestControllerV1 userRestControllerV1 = new UserRestControllerV1(userService);
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);

    @Test
    public void saveUser()  throws IOException {
        String jsonData = "{\"name\": \"Alex\"}";

        when(request.getContentType()).thenReturn("application/json");
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonData)));

        userRestControllerV1.doPost(request, response);

        verify(userService, times(1)).save(any(User.class));
    }

    @Test
    public void getUser() throws IOException {
    when(request.getPathInfo()).thenReturn("user/1");
    when(userService.findById(1)).thenReturn(any(User.class));

    userRestControllerV1.doGet(request, response);

    verify(userService, times(1)).findById(1);
    }

    @Test
    public void getAllUser() throws IOException {
        User user = new User();
        user.setName("Name");
        List<User> users = List.of(user);

        when(request.getPathInfo()).thenReturn(null);
        when(userService.findAll()).thenReturn(users);

        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        userRestControllerV1.doGet(request, response);

        verify(userService, times(1)).findAll();
    }
}
