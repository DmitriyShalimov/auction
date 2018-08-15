package ua.auction.bidme.web.servlets;

import org.thymeleaf.context.WebContext;
import ua.auction.bidme.service.security.AuthenticationService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.*;
import static ua.auction.bidme.web.templater.PageGenerator.instance;

public class LoginServlet extends HttpServlet {
    private final AuthenticationService authenticationService;

    public LoginServlet(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WebContext context = new WebContext(req, resp, req.getServletContext(), req.getLocale());
        resp.getWriter().println(instance().getPage(context, "login"));
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        if (isNullOrEmpty(email) || isNullOrEmpty(password)) {
            resp.setStatus(SC_BAD_REQUEST);
            return;
        }
        String sessionId = req.getSession().getId();
        if (authenticationService.tryAuthenticate(email, password, sessionId)) {
            resp.setStatus(SC_OK);
        } else {
            resp.setStatus(SC_UNAUTHORIZED);
        }

    }

    private boolean isNullOrEmpty(String email) {
        return email == null || email.trim().length() == 0;
    }
}
