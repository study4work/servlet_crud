package com.lysenko.rest;

import com.google.gson.Gson;
import com.lysenko.config.GsonConfig;
import com.lysenko.entity.File;
import com.lysenko.exception.MyHibernateException;
import com.lysenko.service.FileService;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@WebServlet("/api/v1/file/*")
@MultipartConfig
public class FileRestControllerV1 extends HttpServlet {

    private final Gson gson = GsonConfig.getGson();
    private final FileService fileService = new FileService();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        String path = request.getPathInfo();

        if (path == null || path.equals("/")) {
            List<File> files = fileService.findAll();
            String answer = gson.toJson(files);
            response.getWriter().write(answer);
        } else {
            String[] idFromPath = path.split("/");
            Integer id = Integer.valueOf(idFromPath[1]);
            File file = fileService.findById(id);
            if (file != null) {
                String answer = gson.toJson(file);
                response.getWriter().write(answer);
            } else
                response.sendError(HttpServletResponse.SC_NOT_FOUND, String.format("Cant find file by %s", id));
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("application/json");
        String headerValue = request.getHeader("user_id");

        Part filePart = request.getPart("file");
        String fileName = extractFileName(filePart);
        InputStream fileContent = filePart.getInputStream();

        fileService.save(fileContent, fileName, Integer.valueOf(headerValue));
        response.sendError(HttpServletResponse.SC_OK);
    }

    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String path = request.getPathInfo();

        if (path != null && !path.equals("/")) {
            String[] idFromPath = path.split("/");
            Integer id = Integer.valueOf(idFromPath[1]);
            String secondHeaderValue = request.getHeader("old_file_name");

            Part filePart = request.getPart("file");
            String fileName = extractFileName(filePart);
            InputStream fileContent = filePart.getInputStream();

            fileService.update(fileContent, id, fileName, secondHeaderValue);
            response.sendError(HttpServletResponse.SC_OK);
        } else throw new MyHibernateException("Something was wrong with update file");
    }

    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        String path = request.getPathInfo();

        if (path != null && !path.equals("/")) {
            String[] idFromPath = path.split("/");
            Integer id = Integer.valueOf(idFromPath[1]);
            fileService.delete(id);
            response.sendError(HttpServletResponse.SC_OK);
        } else
            throw new MyHibernateException("Value of user id is empty");
    }

    private String extractFileName(Part part) {
        String contentDispositionHeader = part.getHeader("content-disposition");
        String[] parts = contentDispositionHeader.split(";");

        for (String contentDisposition : parts) {
            if (contentDisposition.trim().startsWith("filename")) {
                return contentDisposition.substring(contentDisposition.indexOf('=') + 1).trim().replace("\"", "");
            }
        }

        return "unknown";
    }
}
