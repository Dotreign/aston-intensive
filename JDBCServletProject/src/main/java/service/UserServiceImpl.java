package service;

import context.UserAuthContext;
import entity.Link;
import entity.User;
import entity.UserLinks;
import exception.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import repository.LinkRepository;
import repository.UserRepository;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class UserServiceImpl implements UserService {

    private static UserServiceImpl instance;
    UserRepository userRepository;
    LinkRepository linkRepository;
    UserAuthContext userAuthContext;

    public UserServiceImpl() {
        this.userRepository = UserRepository.getInstance();
        this.userAuthContext = UserAuthContext.getInstance();
        this.linkRepository = LinkRepository.getInstance();
    }

    public static UserServiceImpl getInstance() {
        if (instance == null) {
            instance = new UserServiceImpl();
        }
        return instance;
    }

    @Override
    public void getUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            if (!userAuthContext.checkIsAuthenticated()) {
                throw new AuthException("User not authenticated");
            }
            String username = userAuthContext.getAuthenticatedUsername();
            User user = userRepository.get(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            List<Link> links = linkRepository.getAll(username)
                    .orElseThrow(() -> new RuntimeException("Links not found"));
            UserLinks userLinks = new UserLinks();
            userLinks.setUsername(user.getUsername());
            userLinks.setLinks(links);
            response.setStatus(HttpServletResponse.SC_OK);
            PrintWriter writer = response.getWriter();
            writer.write(userLinks.getUsername());
            writer.write("\nUser links: ");
            writer.write(userLinks.getLinks().toString());
            writer.close();
        } catch (RuntimeException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (AuthException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        }
    }
}
