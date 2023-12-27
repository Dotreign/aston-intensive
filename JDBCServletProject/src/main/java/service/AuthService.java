package service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthService {

    void login(HttpServletResponse response, HttpServletRequest request) throws IOException;

    void register(HttpServletResponse response, HttpServletRequest request) throws IOException;

    boolean checkAuth();
}
