package com.lysenko.service;

import com.lysenko.entity.File;
import com.lysenko.repository.FileRepository;
import com.lysenko.repository.hibernateImpl.FileRepositoryImpl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

public class FileService {

    private final FileRepository fileRepository;

    public FileService() {
        this.fileRepository = new FileRepositoryImpl();
    }

    public File save(File file) {
        String path = "C:\\projects\\Suleimanov\\Servlet_CRUD\\src\\main\\resources\\store\\" + file.getName();
        file.setFilePath(path);
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(path))) {
            outputStream.writeObject(file.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileRepository.save(file);
    }

    public File update(File file, Integer id) {
        return fileRepository.update(id, file);
    }

    public File findById(Integer id) {
        return fileRepository.findById(File.class, id);
    }

    public List<File> findAll() {
        return fileRepository.findAll();
    }

    public void delete(Integer id) {
        fileRepository.delete(File.class, id);
    }
}
