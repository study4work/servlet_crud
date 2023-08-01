package com.lysenko.rest;

import com.google.gson.Gson;
import com.lysenko.config.GsonConfig;
import com.lysenko.entity.Event;
import com.lysenko.exception.MyHibernateException;
import com.lysenko.service.EventService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/v1/event/*")
public class EventRestControllerV1 extends HttpServlet {
    private final Gson gson = GsonConfig.getGson();
    private final EventService eventService = new EventService();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        String path = request.getPathInfo();

        if (path == null || path.equals("/")) {
            List<Event> events = eventService.findAll();
            String answer = gson.toJson(events);
            response.getWriter().write(answer);
        } else {
            String[] idFromPath = path.split("/");
            Integer id = Integer.valueOf(idFromPath[1]);
            Event event = eventService.findById(id);
            if (event != null) {
                String answer = gson.toJson(event);
                response.getWriter().write(answer);
            } else
                response.sendError(HttpServletResponse.SC_NOT_FOUND, String.format("Cant find event by %s", id));
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getPathInfo();
        response.setContentType("application/json");

        String jsonData = readLineFromRequest(request);

        if (path != null && !path.equals("/")) {
            Event user = gson.fromJson(jsonData, Event.class);
            String[] idFromPath = path.split("/");
            Integer id = Integer.valueOf(idFromPath[1]);
            if (user != null) {
                eventService.update(user, id);
                response.sendError(HttpServletResponse.SC_OK, "Successful save user" + user);
            } else
                throw new MyHibernateException("Something was wrong with user parsing");
        }
    }

    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getPathInfo();
        response.setContentType("application/json");

        if (path != null && !path.equals("/")) {
            String[] idFromPath = path.split("/");
            Integer id = Integer.valueOf(idFromPath[1]);
            eventService.delete(id);
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
