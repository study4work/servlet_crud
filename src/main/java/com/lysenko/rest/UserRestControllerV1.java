package com.lysenko.rest;

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

@WebServlet("/api/v1/users/*")
public class UserRestControllerV1 extends HttpServlet {

    private final Gson gson = GsonConfig.getGson();
    private final UserService userService;

    public UserRestControllerV1() {
        userService = new UserService();
    }

    public UserRestControllerV1(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        String path = request.getPathInfo();

        if (path == null || path.equals("/")) {
            List<User> users = userService.findAll();
            String answer = gson.toJson(users);
            response.getWriter().write(answer);
        } else {
            String[] idFromPath = path.split("/");
            Integer id = Integer.valueOf(idFromPath[1]);
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
    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getPathInfo();
        response.setContentType("application/json");

        String jsonData = readLineFromRequest(request);

        if (path != null && !path.equals("/")) {
           User user = gson.fromJson(jsonData, User.class);
            String[] idFromPath = path.split("/");
            Integer id = Integer.valueOf(idFromPath[1]);
           if (user != null) {
               userService.update(user, id);
               response.sendError(HttpServletResponse.SC_OK, "Successful save user" + user);
           } else
               throw new MyHibernateException("Something was wrong with user saving");
        }
    }

    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getPathInfo();
        response.setContentType("application/json");

        if (path != null && !path.equals("/")) {
            String[] idFromPath = path.split("/");
            Integer id = Integer.valueOf(idFromPath[1]);
            userService.delete(id);
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
