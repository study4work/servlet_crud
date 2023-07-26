package com.lysenko.controller;

import com.google.gson.Gson;
import com.lysenko.config.GsonConfig;
import com.lysenko.entity.File;
import com.lysenko.exception.MyHibernateException;
import com.lysenko.service.FileService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/v1/file")
public class FileController extends HttpServlet {

    private final Gson gson = GsonConfig.getGson();
    private final FileService fileService = new FileService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        String headerValue = request.getHeader("id");

        if (headerValue == null) {
            List<File> files = fileService.findAll();
            String answer = gson.toJson(files);
            response.getWriter().write(answer);
        } else {
            Integer id = Integer.valueOf(headerValue);
            File file = fileService.findById(id);
            if (file != null) {
                String answer = gson.toJson(file);
                response.getWriter().write(answer);
            } else
                response.sendError(HttpServletResponse.SC_NOT_FOUND, String.format("Cant find file by %s", id));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        String headerValue = request.getHeader("user_id");
        Integer id = null;

        String jsonData = readLineFromRequest(request);

        File file = gson.fromJson(jsonData, File.class);
        if (file != null) {
            if (headerValue != null) {
                id = Integer.valueOf(headerValue);
            }
            fileService.save(file, id);
            response.sendError(HttpServletResponse.SC_OK);
        } else
            throw new MyHibernateException("Something was wrong with file parsing");
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String headerValue = request.getHeader("id");
        response.setContentType("application/json");

        String jsonData = readLineFromRequest(request);

        if (headerValue != null) {
            File file = gson.fromJson(jsonData, File.class);
            if (file != null) {
                fileService.update(file, Integer.valueOf(headerValue));
                response.sendError(HttpServletResponse.SC_OK, "Successful save file" + file);
            } else
                throw new MyHibernateException("Something was wrong with user parsing");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String headerValue = request.getHeader("id");
        response.setContentType("application/json");

        if (!headerValue.isEmpty()) {
            fileService.delete(Integer.valueOf(headerValue));
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
