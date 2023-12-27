package service;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface LinkService {
    void createLink(HttpServletRequest request, HttpServletResponse response) throws IOException;

    void redirect(String pathInfo, HttpServletRequest request, HttpServletResponse response) throws IOException;
}
