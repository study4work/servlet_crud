package com.lysenko;

import com.lysenko.rest.FileRestControllerV1;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.ServletException;

import java.io.IOException;

import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class FileTest {

    FileRestControllerV1 fileRestControllerV1 = mock(FileRestControllerV1.class);

    @Test
    public void saveFile() throws ServletException, IOException {
    }
}
