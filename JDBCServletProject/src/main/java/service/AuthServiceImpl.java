package service;

import context.UserAuthContext;
import entity.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import repository.UserRepository;
import util.JwtUtil;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AuthServiceImpl implements AuthService {

    private static AuthServiceImpl instance;
    UserRepository userRepository;
    JwtUtil jwtUtil;
    UserAuthContext userAuthContext;

    private AuthServiceImpl() {
        this.userRepository = UserRepository.getInstance();
        this.jwtUtil = JwtUtil.getInstance();
        this.userAuthContext = UserAuthContext.getInstance();
    }

    public static AuthServiceImpl getInstance() {
        if (instance == null) {
            instance = new AuthServiceImpl();
        }
        return instance;
    }

    @Override
    public void login(HttpServletResponse response, HttpServletRequest request) throws IOException {
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Basic ")) {
                throw new RuntimeException("Invalid Authorization header");
            }
            String encodedCredentials = authHeader.substring(6);
            byte[] decodedBytes = Base64.getDecoder().decode(encodedCredentials);
            String decodedCredentials = new String(decodedBytes, StandardCharsets.UTF_8);
            String[] credentials = decodedCredentials.split(":");
            User user = userRepository.get(credentials[0]).orElseThrow(() -> new RuntimeException("User not found"));
            if (!user.getPassword().equals(credentials[1])) {
                throw new RuntimeException("Invalid credentials");
            }
            String jwtoken = jwtUtil.generateToken(user.getUsername());
            Cookie cookie = new Cookie("token", jwtoken);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            response.addCookie(cookie);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        }
    }

    @Override
    public void register(HttpServletResponse response, HttpServletRequest request) throws IOException {
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Basic ")) {
                throw new AuthenticationException("Invalid Authorization header");
            }
            String encodedCredentials = authHeader.substring(6);
            byte[] decodedBytes = Base64.getDecoder().decode(encodedCredentials);
            String decodedCredentials = new String(decodedBytes, StandardCharsets.UTF_8);
            String[] credentials = decodedCredentials.split(":");
            User user = new User();
            user.setUsername(credentials[0]);
            user.setPassword(credentials[1]);
            userRepository.create(user);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (AuthenticationException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        }
    }

    @Override
    public boolean checkAuth() {
        return userAuthContext.checkIsAuthenticated();
    }
}
