package ua.auction.bidme.web.security;

import org.slf4j.Logger;
import org.thymeleaf.context.WebContext;
import ua.auction.bidme.service.security.AuthenticationService;
import ua.auction.bidme.service.security.LoggedUserStorage;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.*;
import static org.slf4j.LoggerFactory.getLogger;
import static ua.auction.bidme.web.templater.PageGenerator.instance;

//todo replace domain with dto
public class LoginServlet extends HttpServlet {
    private final Logger logger = getLogger(getClass());

    private final AuthenticationService authenticationService;
    private final LoggedUserStorage storage;

    public LoginServlet(AuthenticationService authenticationService, LoggedUserStorage storage) {
        this.authenticationService = authenticationService;
        this.storage = storage;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        WebContext context = new WebContext(req, resp, req.getServletContext(), req.getLocale());
        if (storage.isLogged(req.getSession().getId())) {
            resp.setStatus(SC_METHOD_NOT_ALLOWED);
            return;
        }
        resp.setContentType("text/html;charset=utf-8");
        resp.getWriter().println(instance().getPage(context, "login"));
        resp.setStatus(SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String sessionId = req.getSession().getId();
        if (setNotAllowedIfLogged(sessionId, resp)) {
            return;
        }
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        if (setBadBadRequestIfNonValidParam(email, password, resp)) {
            return;
        }
        if (authenticationService.tryAuthenticate(email, password, sessionId)) {
            setOkAndRedirectToHome(resp, email);
        } else {
            logger.warn("invalid user data for email {}, unnavble to login", email);
            resp.sendRedirect("/login");
            resp.setStatus(SC_UNAUTHORIZED);
        }
    }

    private void setOkAndRedirectToHome(HttpServletResponse resp, String email) throws IOException {
        logger.info("user with email {} successfully logged in ", email);
        resp.setStatus(SC_OK);
        resp.sendRedirect("/");
    }

    private boolean setBadBadRequestIfNonValidParam(String email, String password, HttpServletResponse resp) {
        if (isNullOrEmpty(email) || isNullOrEmpty(password)) {
            resp.setStatus(SC_BAD_REQUEST);
            return true;
        }
        return false;
    }

    private boolean setNotAllowedIfLogged(String sessionId, HttpServletResponse resp) {
        if (storage.isLogged(sessionId)) {
            resp.setStatus(SC_METHOD_NOT_ALLOWED);
            return true;
        }
        return false;
    }

    private boolean isNullOrEmpty(String email) {
        return email == null || email.trim().length() == 0;
    }
}
