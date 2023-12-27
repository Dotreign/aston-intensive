package controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.AuthServiceImpl;

import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    AuthServiceImpl authService;

    public RegisterServlet() {
        authService = AuthServiceImpl.getInstance();
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authService.register(response, request);
    }

}
