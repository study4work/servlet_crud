package com.lysenko.controller;

import com.google.gson.Gson;
import com.lysenko.config.GsonConfig;
import com.lysenko.entity.Event;
import com.lysenko.exception.MyHibernateException;
import com.lysenko.service.EventService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public class EventController extends HttpServlet {
    private final Gson gson = GsonConfig.getGson();
    private final EventService eventService = new EventService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        String headerValue = request.getHeader("id");

        if (headerValue == null) {
            List<Event> events = eventService.findAll();
            String answer = gson.toJson(events);
            response.getWriter().write(answer);
        } else {
            Integer id = Integer.valueOf(headerValue);
            Event event = eventService.findById(id);
            if (event != null) {
                String answer = gson.toJson(event);
                response.getWriter().write(answer);
            } else
                response.sendError(HttpServletResponse.SC_NOT_FOUND, String.format("Cant find event by %s", id));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        String jsonData = readLineFromRequest(request);

        Event event = gson.fromJson(jsonData, Event.class);
        if (event != null) {
            eventService.save(event);
            response.sendError(HttpServletResponse.SC_OK);
        } else
            throw new MyHibernateException("Something was wrong with event parsing");
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String headerValue = request.getHeader("id");
        response.setContentType("application/json");

        String jsonData = readLineFromRequest(request);

        if (headerValue != null) {
            Event user = gson.fromJson(jsonData, Event.class);
            if (user != null) {
                eventService.update(user, Integer.valueOf(headerValue));
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
            eventService.delete(Integer.valueOf(headerValue));
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
