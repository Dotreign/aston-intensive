package controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.AuthServiceImpl;

import java.io.IOException;

@WebServlet("/auth")
public class AuthServlet extends HttpServlet {

    AuthServiceImpl authService;

    public AuthServlet() {
        authService = AuthServiceImpl.getInstance();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authService.login(response, request);
    }

}
