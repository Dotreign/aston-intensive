package controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.LinkServiceImpl;

import java.io.IOException;
import java.util.logging.Logger;

@WebServlet("/*")
public class RedirectServlet extends HttpServlet {

    LinkServiceImpl linkService;
    private final Logger logger = Logger.getLogger(RedirectServlet.class.getName());

    public RedirectServlet() {
        linkService = LinkServiceImpl.getInstance();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo().substring(1);
        logger.info("Path info: " + pathInfo);
        linkService.redirect(pathInfo, request, response);
    }

}
