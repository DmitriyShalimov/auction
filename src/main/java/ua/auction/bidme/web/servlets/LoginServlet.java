package ua.auction.bidme.web.servlets;

import org.slf4j.Logger;
import org.thymeleaf.context.WebContext;
import ua.auction.bidme.service.security.AuthenticationService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.*;
import static org.slf4j.LoggerFactory.getLogger;
import static ua.auction.bidme.web.templater.PageGenerator.instance;

//todo replace domain with dto
public class LoginServlet extends HttpServlet {
    private final Logger logger = getLogger(LoginServlet.class);

    private final AuthenticationService authenticationService;

    public LoginServlet(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        WebContext context = new WebContext(req, resp, req.getServletContext(), req.getLocale());
        resp.getWriter().println(instance().getPage(context, "login"));
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        if (isNullOrEmpty(email) || isNullOrEmpty(password)) {
            resp.setStatus(SC_BAD_REQUEST);
            return;
        }
        String sessionId = req.getSession().getId();
        if (authenticationService.tryAuthenticate(email, password, sessionId)) {
            logger.info("user with email {} successfully logged in ", email);
            resp.setStatus(SC_OK);
        } else {
            logger.warn("invalid user data for email {}, unnavble to login", email);
            resp.setStatus(SC_UNAUTHORIZED);
        }

    }

    private boolean isNullOrEmpty(String email) {
        return email == null || email.trim().length() == 0;
    }
}
