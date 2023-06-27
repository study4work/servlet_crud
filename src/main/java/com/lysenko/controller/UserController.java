package com.lysenko.controller;

import com.google.gson.Gson;
import com.lysenko.entity.User;
import com.lysenko.exception.MyHibernateException;
import com.lysenko.repository.UserRepository;
import com.lysenko.repository.hibernateImpl.UserRepositoryImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/user")
public class UserController extends HttpServlet {

    private final Gson gson;
    private final UserRepository userRepository;

    public UserController() {
        this.gson = new Gson();
        this.userRepository = new UserRepositoryImpl();
    }

    private String readRequest(HttpServletRequest request) {
        StringBuilder builder = new StringBuilder();
        try {
            BufferedReader bufferedReader = request.getReader();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            throw new MyHibernateException("Something wrong while parsing http request");
        }
        return builder.toString();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        String requestString = readRequest(request);
        request.setCharacterEncoding("UTF-8");

        String id = gson.fromJson(requestString, String.class);
        User user = userRepository.findById(User.class, Integer.valueOf(id));
        if (user != null) {
            String answer = gson.toJson(user);

            response.setContentType("application/json");
            response.getWriter().write(answer);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, String.format("Cant find user by %s", id));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        String requestString = readRequest(request);
        request.setCharacterEncoding("UTF-8");
        User user = gson.fromJson(requestString, User.class);
        if (user != null) {
        userRepository.save(user);
        String answer = gson.toJson(user);

        response.setContentType("application/json");
        response.getWriter().write(answer);
        } else throw new MyHibernateException("Something was wrong with user parsing");
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        // Обработка PATCH-запроса
        response.getWriter().println("Привет, это PATCH-запрос!");
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        // Обработка PATCH-запроса
        response.getWriter().println("Привет, это PATCH-запрос!");
    }
}
