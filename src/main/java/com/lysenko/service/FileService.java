package com.lysenko.service;

import com.lysenko.entity.Event;
import com.lysenko.entity.File;
import com.lysenko.entity.User;
import com.lysenko.repository.FileRepository;
import com.lysenko.repository.hibernateImpl.FileRepositoryImpl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileService {

    private final FileRepository fileRepository;
    private final EventService eventService;
    private final UserService userService;
    private final String filePath = "src\\main\\resources\\store\\";

    public FileService() {
        this.userService = new UserService();
        this.eventService = new EventService();
        this.fileRepository = new FileRepositoryImpl();
    }

    public File save(InputStream fileInputStream, String fileName, Integer userId) {
        File file = new File();
        file.setName(fileName);
        file.setFilePath(filePath + fileName);
        writeFile(fileInputStream, fileName);
        File savedFile = fileRepository.save(file);

        if (userId != null) {
            User user = userService.findById(userId);

            Event event = new Event();
            event.setUser(user);
            event.setFile(file);
            eventService.save(event);
        }

        return savedFile;
    }

    public File update(InputStream newFileInputStream, Integer id, String fileName, String oldFileName) {
        File fileToUpdate = new File();
        fileToUpdate.setName(fileName);
        writeFile(newFileInputStream, fileName);
        deleteFile(oldFileName);
        return fileRepository.update(id, fileToUpdate);
    }

    public File findById(Integer id) {
        return fileRepository.findById(File.class, id);
    }

    public List<File> findAll() {
        return fileRepository.findAll();
    }

    public void delete(Integer id) {
        File file = fileRepository.delete(File.class, id);
        deleteFile(file.getName());
    }

    private void writeFile(InputStream inputStream, String fileName) {
        try (inputStream) {
            Path path = Paths.get(filePath + fileName);
            Files.copy(inputStream, path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteFile(String fileName) {
        java.io.File fileToDelete = new java.io.File(filePath + fileName);
        fileToDelete.delete();
    }
}
