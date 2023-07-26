package com.lysenko.controller;

import com.google.gson.Gson;
import com.lysenko.config.GsonConfig;
import com.lysenko.entity.User;
import com.lysenko.exception.MyHibernateException;
import com.lysenko.service.UserService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/v1/users")
public class UserController extends HttpServlet {

    private final Gson gson = GsonConfig.getGson();
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        String headerValue = request.getHeader("id");

        if (headerValue == null) {
            List<User> users = userService.findAll();
            String answer = gson.toJson(users);
            response.getWriter().write(answer);
        } else {
            Integer id = Integer.valueOf(headerValue);
            User user = userService.findById(id);
            if (user != null) {
                String answer = gson.toJson(user);
                response.getWriter().write(answer);
            } else
                response.sendError(HttpServletResponse.SC_NOT_FOUND, String.format("Cant find user by %s", id));
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        String jsonData = readLineFromRequest(request);

        User user = gson.fromJson(jsonData, User.class);
        if (user != null) {
            userService.save(user);
            response.sendError(HttpServletResponse.SC_OK);
        } else
            throw new MyHibernateException("Something was wrong with user parsing");
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String headerValue = request.getHeader("id");
        response.setContentType("application/json");

        String jsonData = readLineFromRequest(request);

        if (headerValue != null) {
           User user = gson.fromJson(jsonData, User.class);
           if (user != null) {
               userService.update(user, Integer.valueOf(headerValue));
               response.sendError(HttpServletResponse.SC_OK, "Successful save user" + user);
           } else
               throw new MyHibernateException("Something was wrong with user parsing");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String headerValue = request.getHeader("id");
        response.setContentType("application/json");

        if (!headerValue.isEmpty()) {
            userService.delete(Integer.valueOf(headerValue));
            response.sendError(HttpServletResponse.SC_OK);
        } else
            throw new MyHibernateException("Value of user id is empty");
    }

    private String readLineFromRequest(HttpServletRequest request) throws IOException {
        BufferedReader reader = request.getReader();
        StringBuilder jsonBody = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonBody.append(line);
        }
        return jsonBody.toString();
    }
}
