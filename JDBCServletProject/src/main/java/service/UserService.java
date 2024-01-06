package service;

import entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface UserService {

    void getUser(HttpServletRequest request, HttpServletResponse response) throws IOException;

}
