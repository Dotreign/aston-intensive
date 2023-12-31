package controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.UserServiceImpl;

import java.io.IOException;

@WebServlet("/user")
public class UserServlet extends HttpServlet {

    UserServiceImpl userService;

    public UserServlet() {
        userService = UserServiceImpl.getInstance();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        userService.getUser(request, response);
    }

}
