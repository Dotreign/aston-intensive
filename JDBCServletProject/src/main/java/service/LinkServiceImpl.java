package service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import context.UserAuthContext;
import entity.Link;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import repository.LinkRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.logging.Logger;

public class LinkServiceImpl implements LinkService {

    private final Logger logger = Logger.getLogger(LinkServiceImpl.class.getName());
    private static LinkServiceImpl instance;
    UserAuthContext userAuthContext;
    LinkRepository linkRepository;

    public LinkServiceImpl() {
        this.linkRepository = LinkRepository.getInstance();
        this.userAuthContext = UserAuthContext.getInstance();
    }

    public static LinkServiceImpl getInstance() {
        if (instance == null) {
            instance = new LinkServiceImpl();
        }
        return instance;
    }

    @Override
    public void createLink(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!userAuthContext.checkIsAuthenticated()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not authenticated");
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = request.getReader();
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append('\n');
            }
        } finally {
            reader.close();
        }
        String body = stringBuilder.toString();
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(body).getAsJsonObject();
        String fullUrl = jsonObject.get("full_url").getAsString();
        logger.info("Full url: " + fullUrl);
        if (fullUrl == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Full url not found in request body");
            return;
        }
        Link link = new Link();
        String shortLink = generateShortLink();
        link.setFullUrl(fullUrl);
        link.setShortUrl(shortLink);
        linkRepository.create(link);
        PrintWriter writer = response.getWriter();
        writer.write("http://localhost:8080/" + shortLink);
        writer.close();
    }

    @Override
    public void redirect(String pathInfo, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String fullUrl = linkRepository.getFullUrlByShortLink(pathInfo).orElseThrow(() -> new RuntimeException("Link not found"));
        logger.info("Full url: " + fullUrl);
        try {
            response.sendRedirect(fullUrl);
        } catch (IOException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Can't send redirect");
        } catch (RuntimeException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Link not found");
        }
    }

    private String generateShortLink() {
        int leftLimit = 97;
        int rightLimit = 122;
        int targetStringLength = 10;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }
}
