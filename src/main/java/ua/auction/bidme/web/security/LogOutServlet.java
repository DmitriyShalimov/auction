package ua.auction.bidme.web.security;

import org.slf4j.Logger;
import ua.auction.bidme.service.security.LoggedUserStorage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.slf4j.LoggerFactory.getLogger;

public class LogOutServlet extends HttpServlet {
    private final Logger logger = getLogger(getClass());
    private final LoggedUserStorage storage;

    public LogOutServlet(LoggedUserStorage storage) {
        this.storage = storage;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sessionId = req.getSession().getId();
        logger.info("logout user with session id {}", sessionId);
        storage.logOut(sessionId);
        resp.setStatus(SC_OK);
        resp.sendRedirect("/");
    }
}
