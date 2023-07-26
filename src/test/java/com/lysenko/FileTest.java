package com.lysenko;

import com.lysenko.controller.FileController;
import com.lysenko.entity.File;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.ServletException;

import java.io.IOException;

import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class FileTest {

    FileController fileController = mock(FileController.class);

    @Test
    public void saveFile() throws ServletException, IOException {
    }
}
