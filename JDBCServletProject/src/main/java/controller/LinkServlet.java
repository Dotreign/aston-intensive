package controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.LinkServiceImpl;

import java.io.IOException;

@WebServlet("/link")
public class LinkServlet extends HttpServlet {

    LinkServiceImpl linkService;

    public LinkServlet() {
        linkService = LinkServiceImpl.getInstance();
    }


    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        linkService.createLink(request, response);
    }
}
