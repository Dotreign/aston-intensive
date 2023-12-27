package filter;

import context.UserAuthContext;
import entity.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import repository.UserRepository;
import util.JwtUtil;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

@WebFilter(urlPatterns = "/*")
public class AuthFilter implements Filter {

    private final Logger logger = Logger.getLogger(AuthFilter.class.getName());

    JwtUtil jwtUtil;
    UserAuthContext userAuthContext;
    UserRepository userRepository;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        jwtUtil = JwtUtil.getInstance();
        userAuthContext = UserAuthContext.getInstance();
        userRepository = UserRepository.getInstance();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        logger.info("auth");
        String jwtoken = extractJwt(request);
        String path = ((HttpServletRequest) request).getRequestURI();
        if (path.equals("/auth") || path.equals("/register")) {
            chain.doFilter(request, response);
            return;
        }
        String username = jwtUtil.extractUsername(jwtoken);
        Optional<User> user = userRepository.get(username);
        if (!user.isPresent()) {
            chain.doFilter(request, response);
            return;
        }

        if (jwtoken != null) {
            userAuthContext.authenticate(jwtUtil.extractUsername(jwtoken));
        }
        chain.doFilter(request, response);
    }

    private String extractJwt(ServletRequest request) {
        Cookie[] cookies = ((HttpServletRequest) request).getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    System.out.println(cookie);
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
