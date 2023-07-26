package com.lysenko.service;

import com.lysenko.entity.Event;
import com.lysenko.entity.File;
import com.lysenko.entity.User;
import com.lysenko.exception.MyHibernateException;
import com.lysenko.repository.FileRepository;
import com.lysenko.repository.hibernateImpl.FileRepositoryImpl;

import java.io.IOException;
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

    public File save(File file, Integer userId) {
        writeFile(file);
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

    public File update(File file, Integer id) {
        File fileFromBd = findById(id);
        writeFile(file);
        deleteFile(fileFromBd);
        return fileRepository.update(id, file);
    }

    public File findById(Integer id) {
        return fileRepository.findById(File.class, id);
    }

    public List<File> findAll() {
        return fileRepository.findAll();
    }

    public void delete(Integer id) {
        File fileFromBd = findById(id);
        deleteFile(fileFromBd);
        fileRepository.delete(File.class, id);
    }

    private void writeFile(File file) {
        try {
            java.io.File fileToSave = new java.io.File(filePath + file.getName());
            file.setFilePath(filePath + file.getName());
            fileToSave.createNewFile();
        } catch (IOException e) {
            throw new  MyHibernateException("An error occurred while writing to the file: " + e.getMessage());
        }
    }

    private void deleteFile(File file) {
        java.io.File fileToDelete = new java.io.File(filePath + file.getName());
        fileToDelete.delete();
    }
}
