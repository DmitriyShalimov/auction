package ua.auction.bidme.web.servlets;

import org.slf4j.Logger;
import org.thymeleaf.context.WebContext;
import ua.auction.bidme.entity.UserData;
import ua.auction.bidme.service.UserService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.parseInt;
import static java.lang.System.currentTimeMillis;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.slf4j.LoggerFactory.getLogger;
import static ua.auction.bidme.web.templater.PageGenerator.instance;

public class UserServlet extends HttpServlet {
    private final UserService userService;
    private final Logger logger = getLogger(getClass());

    public UserServlet(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        WebContext context = new WebContext(request, response, request.getServletContext(), request.getLocale());
        long start = currentTimeMillis();
        int id = parseInt(request.getParameter("id"));
        logger.info("start getting user page with id {} and user messages ", id);
        UserData userData = userService.get(id);
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("user", userData.getUser());
        pageVariables.put("userData", userData);
        context.setVariables(pageVariables);
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(instance().getPage(context, "user"));
        response.setStatus(SC_OK);
        logger.info("user {} has been received. it took {}ms",
                userData.getUser().getEmail(), currentTimeMillis() - start);
    }
}
